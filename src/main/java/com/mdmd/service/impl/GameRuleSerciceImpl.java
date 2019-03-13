package com.mdmd.service.impl;

import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.Manager.SysPropManager;
import com.mdmd.controller.GameAction;
import com.mdmd.custom.RedisCustom;
import com.mdmd.custom.UserCustom;
import com.mdmd.dao.CommonDao;
import com.mdmd.dao.GameRuleDao;
import com.mdmd.entity.*;
import com.mdmd.entity.JO.GameRecordJO;
import com.mdmd.entity.JO.GameResultJO;
import com.mdmd.entity.RO.SuperCommRO;
import com.mdmd.entity.bean.FishProbabilityBean;
import com.mdmd.entity.bean.GameResultBean;
import com.mdmd.enums.RedisChannelEnum;
import com.mdmd.service.GameRuleService;
import com.mdmd.service.SysPropService;
import com.mdmd.util.CommonUtil;
import com.mdmd.util.DateFormatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mdmd.constant.ActionConstant.MSG;
import static com.mdmd.constant.GameConstant.*;
import static com.mdmd.constant.RedisConstant.*;
import static com.mdmd.util.WeiXinPayUtil.companyPayToUser;

@Service
public class GameRuleSerciceImpl implements GameRuleService {
    @Autowired
    private GameRuleDao gameRuleDao;
    @Autowired
    private RedisCustom redisCustom;
    @Autowired
    private UserCustom userCustom;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private SysPropService sysPropService;
  
    @Autowired
    private CommonDao commonDao;


    private Map<String,FishProbabilityBean>  fishRules = null;

    private static final Logger LOGGER = LogManager.getLogger(GameRuleSerciceImpl.class);


    public List<FishRuleEntity> getAllFishRules() {
        return null;
    }

    /**
     * 初始化捕鱼规则
     */
    public void initFishRules(){
        List<FishRuleEntity> fishRuleEntities = commonDao.listAllEntity(FishRuleEntity.class);
        if(fishRuleEntities.size() == 0){
            LOGGER.error("捕鱼规则初始化失败(无规则)");
            System.exit(0);
        }
        Map<String,Object> fishRuleMap = new HashMap<>();
        fishRules = new HashMap<>();
        for (int i = 0; i < fishRuleEntities.size(); i++) {
            FishRuleEntity fishRuleEntity = fishRuleEntities.get(i);
            String key= fishRuleEntity.getPrice() + REDIS_KEY_fishRules_itemkey + fishRuleEntity.getTargetValue();
            fishRuleMap.put(key, fishRuleEntity);
            fishRules.put(key,new FishProbabilityBean(fishRuleEntity));
        }
        setFishRuleEntityMap(fishRuleMap);
        LOGGER.info("捕鱼规则初始化完成");
    }

    public FishRuleEntity getFishRulesWithPriceAndTargetValue(int price, int targetValue) {
        FishRuleEntity fishRuleEntity = gameRuleDao.getFishRuleWithPriceAndTargetValue(price, targetValue);
        //如果为空 设置一个概率为1的新规则
        if(fishRuleEntity == null)
        {
            FishRuleEntity newFishRuleEntity = new FishRuleEntity();
            newFishRuleEntity.setPrice(price);
            newFishRuleEntity.setTargetValue(targetValue);
            newFishRuleEntity.setProbability((byte)1);
            fishRuleEntity = newFishRuleEntity;
        }
        return fishRuleEntity;
    }

    public GameResultJO getFishResult(int price, int targetValue, int userId) throws RuntimeException{

        //如果这个倍率或者目标红包金额不在库中
        if(!hasPriceTargetValue(price, targetValue))
        {
            LOGGER.error("用户"+userId+"不存在的倍率或者红包金额："+price+"，"+targetValue+"!");
            String msg = "不存在的倍率或者红包金额："+price+"，"+targetValue+"!";
            return new GameResultJO(msg);
        }
        //查看这个用户有没有金币玩游戏
        UserEntity userEntity = (UserEntity) commonDao.getEntity(UserEntity.class, userId);
        double balance = userEntity.getGold();
        System.out.println("用户余额："+balance+",倍率:"+price+",红包数值："+targetValue);
        if(balance < price)
        {
            String msg = "您的金币不足哦~";
            return new GameResultJO(msg);
        }

        FishProbabilityBean fishProbabilityBean = getFishProbabilityBean(price, targetValue);

        GameResultBean gameResultBean = null;
        //将对象锁住
        synchronized (fishProbabilityBean){
            int index = fishProbabilityBean.getIndex();

            //>=为了防止意外情况
            if(index >= fishProbabilityBean.getVolume())
            {
                //更新值
                fishProbabilityBean.updateValue(getFishRuleEntityBean(price,targetValue));
            }
            // 0就是输，
            gameResultBean = fishProbabilityBean.getResult();
        }
        double gameCost = gameResultBean.getGameCost();
        boolean gameResult = gameResultBean.getGameResult();
        //根据输赢计算金币总额
        GoldEntity goldEntity = userEntity.singleGoldEntity();

        double currentGold = CommonUtil.formatDouble_two(balance - price + gameCost);
        //金币余额
        userEntity.setGold(currentGold);

        //更新金币类记录
        userCustom.calcuGoldForGoldEntity(price,gameCost,goldEntity);

        //记录游戏记录
        GameRecordEntity gameRecordEntity = new GameRecordEntity();
        gameRecordEntity.setGameType(GAME_FISH_RECORD + "/" + price );
        gameRecordEntity.setGameContent("目标：" + targetValue);
        gameRecordEntity.setGameCost(gameCost);
        gameRecordEntity.setGameResult(gameResult ? "赢" : "输");
        gameRecordEntity.setPrincipal(price);
        gameRecordEntity.setUserEntity(userEntity);
        commonDao.addEntity(gameRecordEntity);
        //将新记录加进缓存
        redisCustom.addRecordListForRedis(new GameRecordJO(gameRecordEntity),userId);

        //佣金结算 记录佣金明细 每个人佣金总表 和 用户表中的余额
        //this.calcuCommission(userId,price);
        commonDao.updateEntity(userEntity);
        //将上面这一步交给了redis todo 缓存服务器停止运作时应使用如上方法（后期应整体可脱离redis，万全解耦）
        redisCustom.publish(RedisChannelEnum.channel_superComm,new SuperCommRO(userId,price));
        return new GameResultJO(gameResultBean.getGameResult(),gameResultBean.getGameCost(),currentGold,0);

    }

    public GameResultJO getTreasureResult(int type, int num, int userId) {
        //总计花费
        int allCost = num * 11 + TREASURE_INFORMCOST;
        //查看这个用户有没有金币玩游戏
        UserEntity userEntity = (UserEntity) commonDao.getEntity(UserEntity.class, userId);
        String openid = userEntity.getUserOpenid();
        double balance = userEntity.getGold();
        if(balance < allCost)
        {
            String msg = "您的金币不足哦~";
            return new GameResultJO(msg);
        }
        //订单号
        String orderNumber ;
        //支付给用户一块钱并获取订单编号
        Map<String, String> record = companyPayToUser(userId,openid, 100, "夺宝凭证");
        //是否有订单编号，没有则为失败
        if(record.containsKey("payment_no"))
        {
            orderNumber = record.get("payment_no");
        }
        else
        {
            return new GameResultJO(record.get(MSG));
        }
        //charAt返回char 变为int型的必要转换
        int resultNumber = orderNumber.charAt(orderNumber.length() - 1) - '0';
        int get = 0;
        String gameType = "",gameContent = "";
        switch (type) {
            case 10 ://大
                if(resultNumber >= 5)
                {
                    get = num * 20;

                }
                gameType = TREASURE_TYPE_BIGSMALL_1;
                gameContent = TREASURE_CONTENT_BIG;
                break;
            case 11 ://小
                if(resultNumber < 5)
                {
                    get = num * 20;
                }
                gameType = TREASURE_TYPE_BIGSMALL_1;
                gameContent = TREASURE_CONTENT_SMALL;
                break;
            case 12 ://单
                if(resultNumber % 2 == 1)
                {
                    get = num * 20;
                }
                gameType = TREASURE_TYPE_BIGSMALL_1;
                gameContent = TREASURE_CONTENT_SINGLE;
                break;
            case 13 ://双
                if(resultNumber % 2 == 0)
                {
                    get = num * 20;
                }
                gameType = TREASURE_TYPE_BIGSMALL_1;
                gameContent = TREASURE_CONTENT_DOUBLE;
                break;
            case 14 : //大单
                if(resultNumber == 7 || resultNumber == 9)
                {
                    get = num * 45;
                }
                gameType = TREASURE_TYPE_BIGSMALL_2;
                gameContent = TREASURE_CONTENT_BIG_SINGLE;
                break;
            case 15 : //小单
                if(resultNumber == 1 || resultNumber == 3)
                {
                    get = num * 45;
                }
                gameType = TREASURE_TYPE_BIGSMALL_2;
                gameContent = TREASURE_CONTENT_SMALL_SINGLE;
                break;
            case 16 : //大双
                if(resultNumber == 6 || resultNumber == 8)
                {
                    get = num * 45;
                }
                gameType = TREASURE_TYPE_BIGSMALL_2;
                gameContent = TREASURE_CONTENT_BIG_DOUBLE;
                break;
            case 17 : //小双
                if(resultNumber == 2 || resultNumber == 4)
                {
                    get = num * 45;
                }
                gameType = TREASURE_TYPE_BIGSMALL_2;
                gameContent = TREASURE_CONTENT_SMALL_DOUBLE;
                break;
            case 18 : //合
                if(resultNumber == 0 || resultNumber == 5)
                {
                    get = num * 45;
                }
                gameType = TREASURE_TYPE_BIGSMALL_2;
                gameContent = TREASURE_CONTENT_OTHER;
                break;
            default://一赔五 0-9
               //todo 没有这个
                break;
        }
        boolean win = get > 0 ;
        //更新user金币数
        double goldQuantity = CommonUtil.formatDouble_two(balance - allCost + get);
        //金币余额
        userEntity.setGold(goldQuantity);
        //更新金币类记录
        userCustom.calcuGoldForGoldEntity(allCost,get, userEntity.singleGoldEntity());
        //记录游戏记录

        GameRecordEntity gameRecordEntity = new GameRecordEntity();
        gameRecordEntity.setGameType(GAME_TREASURE + "/" + gameType );
        gameRecordEntity.setGameContent(gameContent + "，"+ num +"手");
        gameRecordEntity.setGameCost(get);
        gameRecordEntity.setGameResult(win ? "赢" : "输");
        gameRecordEntity.setPrincipal(allCost);
        gameRecordEntity.setGameOrder(orderNumber);
        gameRecordEntity.setUserEntity(userEntity);
        commonDao.addEntity(gameRecordEntity);

        //将新记录加进缓存
        redisCustom.addRecordListForRedis(new GameRecordJO(gameRecordEntity),userId);

        //佣金结算 记录佣金明细 每个人佣金总表 和 用户表中的余额
        //this.calcuCommission(userId,num * 10);
        commonDao.updateEntity(userEntity);

        redisCustom.publish(RedisChannelEnum.channel_superComm,new SuperCommRO(userId,num*10));
        return new GameResultJO(win,allCost, userEntity.getGold(),userEntity.getCommission(),orderNumber);
    }




    public void calcuCommission(int userId, double price){
        List<UserEntity> superUserEntityList = userCustom.get5_SuperUserEntity(userId);//index=0为上一级
        if(superUserEntityList != null)
        {

            for (int i = 0; i < superUserEntityList.size(); i++) {
                //为0时为上一级父类 以此类推
                UserEntity superUser = superUserEntityList.get(i);
                if(i > 4)
                    break;
                //获取当前级别佣金千分比
                int awardNum = NODE_VALUES[i];
                //是否是双倍佣金时间
                if(SysPropManager.isDoubleCommissionTime())
                {
                    awardNum += awardNum;
                }
                double resultCommission = CommonUtil.formatDouble_three(price *awardNum/1000);
                //修改父类的佣金对象
                this.calcuCommissionForCommissionEntity(superUser.getUserid(),resultCommission,superUser.singleCommissionEntity());
                //修改父类的user对象
                superUser.setCommission(CommonUtil.formatDouble_three(superUser.getCommission() + resultCommission));
                commonDao.updateEntity(superUser);
                //增加佣金记录
                UserCommissionEntity userCommissionEntity = new UserCommissionEntity();
                userCommissionEntity.setUserEntity(superUser);
                userCommissionEntity.setBaseNumber(price);
                userCommissionEntity.setBaseScale(awardNum);
                userCommissionEntity.setChildNodeIndex( ++i );
                userCommissionEntity.setCommissionResult(resultCommission);
                userCommissionEntity.setCommissionType(GAME_FISH_RECORD);
                userCommissionEntity.setNodeUserId(userId);
                commonDao.addEntity(userCommissionEntity);

                //插入缓存
                redisCustom.addRecordListForRedis(userCommissionEntity,superUser.getUserid());
            }
        }
    }



    /**
     * 计算用户佣金对象
     * @param userId
     * @param addCommiss
     * @param commissionEntity
     */
    private void calcuCommissionForCommissionEntity(int userId,double addCommiss,CommissionEntity commissionEntity){
        int calcDate = commissionEntity.getCalcuDate();
        int today = DateFormatUtil.now_yyMMdd_intVal();

        //如果不是今天算的 就将今天以前,上次计算之后的重新算一次并记录
        if(today != calcDate)
        {
            commissionEntity.setPreCommission(CommonUtil.formatDouble_three(commissionEntity.getPreCommission() + commissionEntity.getTodayCommission()));
            commissionEntity.setTodayCommission(addCommiss);
            commissionEntity.setCalcuDate(today);
        }
        else
        {
            commissionEntity.setTodayCommission(CommonUtil.formatDouble_three(commissionEntity.getTodayCommission() + addCommiss));
        }

    }





    private void setFishRuleEntityMap(Map<String,Object> fishRuleMap){
        if(!redisCacheManager.hmset(REDIS_KEY_fishRules, fishRuleMap))
        {
            LOGGER.error("捕鱼规则初始化失败(redis插入错误)");
            System.exit(0);
        }
    }
    private FishRuleEntity getFishRuleEntityBean(int price,int targetValue){
        String key = price + REDIS_KEY_fishRules_itemkey + targetValue;
        Object hget = redisCacheManager.hget(REDIS_KEY_fishRules, key);
        if(hget == null)
            return null;
        return (FishRuleEntity) hget;
    }

    private FishProbabilityBean getFishProbabilityBean(int price, int targetValue){
        return fishRules.get(price + REDIS_KEY_fishRules_itemkey + targetValue);
    }

    private boolean hasPriceTargetValue(int price, int targetValue){
        return fishRules.containsKey(price+REDIS_KEY_fishRules_itemkey+targetValue);
    }

}
