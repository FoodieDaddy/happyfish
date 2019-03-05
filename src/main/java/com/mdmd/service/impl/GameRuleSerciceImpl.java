package com.mdmd.service.impl;

import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.dao.CommonDao;
import com.mdmd.dao.GameRuleDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.*;
import com.mdmd.entity.JO.GameRecordJO;
import com.mdmd.entity.JO.GameResultJO;
import com.mdmd.entity.bean.FishProbabilityBean;
import com.mdmd.entity.bean.GameResultBean;
import com.mdmd.service.DataService;
import com.mdmd.service.DealService;
import com.mdmd.service.GameRuleService;
import com.mdmd.service.SysPropService;
import com.mdmd.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.mdmd.constant.ActionConstant.MSG;
import static com.mdmd.constant.GameConstant.*;
import static com.mdmd.constant.SystemConstant.DATEFORMAT__yyMMdd;

@Component
public class GameRuleSerciceImpl implements GameRuleService {
    @Autowired
    private GameRuleDao gameRuleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DataService dataService;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private SysPropService sysPropService;
    @Autowired
    private DealService dealService;
    @Autowired
    private CommonDao commonDao;

    private Map<Integer, Map<Integer, FishProbabilityBean>>  fishRules = null;

    private static final Logger LOGGER = LogManager.getLogger(GameRuleSerciceImpl.class);


    public List<FishRuleEntity> getAllFishRules() {
        return null;
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
        if(fishRules == null)
            this.init();
        //如果这个倍率不在库中
        if(!fishRules.containsKey(price))
        {
            LOGGER.error("用户"+userId+"不存在的倍率"+price+"!!!");
            String msg = "倍率"+price+"不存在";
            return new GameResultJO(msg);
        }

        //查看这个用户有没有金币玩游戏
        UserEntity userEntity = (UserEntity) commonDao.getEntity(UserEntity.class,userId);
        double balance = userEntity.getGold();
        System.out.println("用户余额："+balance+",倍率:"+price+",红包数值："+targetValue);
        if(balance < price)
        {
            String msg = "您的金币不足哦~";
            return new GameResultJO(msg);
        }
        Map<Integer, FishProbabilityBean> integerFishProbabilityBeanMap = fishRules.get(price);
        //如果这个红包数值不在库中
        if(!integerFishProbabilityBeanMap.containsKey(targetValue))
        {
            LOGGER.error("用户"+userId+"不存在的红包金额"+targetValue+"!!!");
            String msg = "红包金额"+targetValue+"不存在";
            return new GameResultJO(msg);
        }
        FishProbabilityBean fishProbabilityBean = integerFishProbabilityBeanMap.get(targetValue);

        GameResultBean gameResultBean = null;
        //将对象锁住
        synchronized (fishProbabilityBean){
            int index = fishProbabilityBean.getIndex();
            // 0就是输，
            gameResultBean = fishProbabilityBean.getResult();

            //>=为了防止意外情况
            if(index >= 9)
            {
                //更新值
                fishProbabilityBean.updateValue(this.getFishRulesWithPriceAndTargetValue(price, targetValue));

            }
        }
        double gameCost = gameResultBean.getGameCost();
        boolean gameResult = gameResultBean.getGameResult();
        //根据输赢计算金币总额
        GoldEntity goldEntity = userEntity.singleGoldEntity();

        double currentGold = CommonUtil.formatDouble_two(balance - price + gameCost);
        //金币余额
        userEntity.setGold(currentGold);

        //更新金币类记录
        this.calcuGoldForGoldEntity(userId,price,gameCost,goldEntity);

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
        dataService.addRecordListForRedis(new GameRecordJO(gameRecordEntity),userId);

        //佣金结算 记录佣金明细 每个人佣金总表 和 用户表中的余额
        this.calcuCommission(userId,price);

        commonDao.updateEntity(userEntity);

        return new GameResultJO(gameResultBean.getGameResult(),gameResultBean.getGameCost(),currentGold,0);

    }

    public GameResultJO getTreasureResult(int type, int num, int userId) {
        //总计花费
        int allCost = num * 11 + TREASURE_INFORMCOST;
        //查看这个用户有没有金币玩游戏
        UserEntity userEntity = (UserEntity) commonDao.getEntity(UserEntity.class,userId);
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
        Map<String, String> record = dealService.companyPayToUser(userId,openid, 100, "夺宝凭证");
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
                    gameType = TREASURE_TYPE_BIGSMALL_1;
                    gameContent = TREASURE_CONTENT_BIG;
                }
                break;
            case 11 ://小
                if(resultNumber < 5)
                {
                    get = num * 20;
                    gameType = TREASURE_TYPE_BIGSMALL_1;
                    gameContent = TREASURE_CONTENT_SMALL;
                }
                break;
            case 12 ://单
                if(resultNumber % 2 == 1)
                {
                    get = num * 20;
                    gameType = TREASURE_TYPE_BIGSMALL_1;
                    gameContent = TREASURE_CONTENT_SINGLE;
                }
                break;
            case 13 ://双
                if(resultNumber % 2 == 0)
                {
                    get = num * 20;
                    gameType = TREASURE_TYPE_BIGSMALL_1;
                    gameContent = TREASURE_CONTENT_DOUBLE;
                }
                break;
            case 14 : //大单
                if(resultNumber >= 5 && resultNumber % 2 == 1)
                {
                    get = num * 45;
                    gameType = TREASURE_TYPE_BIGSMALL_2;
                    gameContent = TREASURE_CONTENT_BIG_SINGLE;
                }
                break;
            case 15 : //小单
                if(resultNumber < 5 && resultNumber % 2 == 1)
                {
                    get = num * 45;
                    gameType = TREASURE_TYPE_BIGSMALL_2;
                    gameContent = TREASURE_CONTENT_SMALL_SINGLE;
                }
                break;
            case 16 : //大双
                if(resultNumber >= 5 && resultNumber % 2 == 0)
                {
                    get = num * 45;
                    gameType = TREASURE_TYPE_BIGSMALL_2;
                    gameContent = TREASURE_CONTENT_BIG_DOUBLE;
                }
                break;
            case 17 : //小双
                if(resultNumber < 5 && resultNumber % 2 == 0)
                {
                    get = num * 45;
                    gameType = TREASURE_TYPE_BIGSMALL_2;
                    gameContent = TREASURE_CONTENT_SMALL_DOUBLE;
                }
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
        this.calcuGoldForGoldEntity(userId,allCost,get, userEntity.singleGoldEntity());
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
        dataService.addRecordListForRedis(new GameRecordJO(gameRecordEntity),userId);

        //佣金结算 记录佣金明细 每个人佣金总表 和 用户表中的余额
        this.calcuCommission(userId,num * 10);

        commonDao.updateEntity(userEntity);

        return new GameResultJO(win,allCost, userEntity.getGold(),userEntity.getCommission(),orderNumber);
    }


    /**
     * 初始化捕鱼规则
     */
    private void init(){
        List<FishRuleEntity> fishRuleEntities = commonDao.listAllEntity(FishRuleEntity.class);
        if(fishRuleEntities.size() == 0){
            fishRules = null;
            return;
        }
        fishRules = new HashMap<>();
        for (int i = 0; i < fishRuleEntities.size(); i++) {
            FishRuleEntity fishRuleEntity = fishRuleEntities.get(i);
            int price = fishRuleEntity.getPrice();
            int targetValue = fishRuleEntity.getTargetValue();
            int probability = fishRuleEntity.getProbability();
            int volume = fishRuleEntity.getVolume();
            double minReturn = fishRuleEntity.getMinReturn();
            double maxReturn = fishRuleEntity.getMaxReturn();
            //如果集合中有这个价格，存入
            if(fishRules.containsKey(price))
            {
                Map<Integer, FishProbabilityBean> probabilityBeanMap = fishRules.get(price);
                //创建一个新的捕鱼概率计算类
                FishProbabilityBean fishProbabilityBean = new FishProbabilityBean(probability, volume,price, targetValue,minReturn,maxReturn);
                probabilityBeanMap.put(targetValue,fishProbabilityBean);
            }
            else
            {
                HashMap<Integer, FishProbabilityBean> beanHashMap = new HashMap<>();
                FishProbabilityBean fishProbabilityBean = new FishProbabilityBean(probability, volume,price, targetValue,minReturn,maxReturn);
                beanHashMap.put(targetValue,fishProbabilityBean);
                fishRules.put(price,beanHashMap);
            }
        }
    }

    /**
     * 计算父类佣金
     * @param userId
     * @param price
     */
    private void calcuCommission(int userId, double price){
        List<UserEntity> superUserEntityList = dataService.get5_SuperUserEntity(userId);//index=0为上一级
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
                if(sysPropService.isDoubleCommissionTime())
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
        int today = Integer.valueOf(new SimpleDateFormat(DATEFORMAT__yyMMdd).format(new Date()));
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


    /**
     * 计算用户金币对象
     * @param userId
     * @param price
     * @param addGold
     * @param goldEntity
     */
    private void calcuGoldForGoldEntity(int userId,double price,double addGold, GoldEntity goldEntity){
        int calcDate = goldEntity.getCalcDate();
        int today = Integer.valueOf(new SimpleDateFormat(DATEFORMAT__yyMMdd).format(new Date()));
        double todayWater = goldEntity.getTodayWater();
        double todayGold = goldEntity.getTodayGold();
        //如果不是今天算的 就将今天以前,上次计算之后的重新算一次并记录
//        if(today != calcDate)
//        {
//            String beforeDay = "20" + calcDate/10000 + "-" + calcDate%10000/100 + "-" + calcDate%10000%100 + " 00:00:00";
//            String endDay = "20" + today/10000 + "-" + today%10000/100 + "-" + today%10000%100 + " 00:00:00";
//            //查找这段时间内的游戏记录 获取这段时间内的总花费（总利润 - 总本金）
//            double costs = userDao.getGameRecord_costs_betweenTime(userId, beforeDay, endDay);
//
//            //查找这段时间内的提现记录
//            double takeouts = userDao.getTakeout_sum_betweenTime(userId, beforeDay, endDay);
//
//            //查找这段时间内的充值记录
//            double topups = userDao.getTopup_sum_betweenTime(userId, beforeDay, endDay);
//
//            //今天以前赢得 = 上次计算总赢 + 近日提现数量 + 总花费（赢的） - 充值数量
//            double bala = + takeouts + costs - topups;
//            //如果重新核对的近段时间记录跟存储的不同，用重新计算的
//
//            goldEntity.setPreGold(bala + goldEntity.getPreGold());
//            goldEntity.setTodayGold(addGold - price);
//            goldEntity.setTodayWater(price);
//            goldEntity.setCalcDate(today);
//
//        }
        if(today != calcDate)
        {
            goldEntity.setPreGold(CommonUtil.formatDouble_two(goldEntity.getPreGold() + goldEntity.getTodayGold()));
            goldEntity.setTodayGold(CommonUtil.formatDouble_three(addGold - price));
            goldEntity.setTodayWater(price);
            goldEntity.setCalcDate(today);

        }
        else
        {
            //记录今日赢得金币，正为今天赢得（需要在充值时减掉充值金额）
            goldEntity.setTodayGold(todayGold + addGold - price );
            //记录今日流水
            goldEntity.setTodayWater(todayWater + price);
        }

    }
}
