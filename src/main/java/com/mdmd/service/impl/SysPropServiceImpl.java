package com.mdmd.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.controller.GameAction;
import com.mdmd.dao.CommonDao;
import com.mdmd.dao.SqlDao;
import com.mdmd.dao.SysPropDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.*;
import com.mdmd.entity.JO.AccountDatasJO;
import com.mdmd.entity.JO.FishRuleJO;
import com.mdmd.entity.JO.PageJO;
import com.mdmd.entity.JO.SysPropResultJO;
import com.mdmd.entity.bean.FishProbabilityBean;
import com.mdmd.entity.bean.PageBean;
import com.mdmd.service.SysPropService;
import com.mdmd.util.CommonUtil;
import com.mdmd.util.DateFormatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mdmd.Manager.SysPropManager.getSysPropMap;
import static com.mdmd.constant.ActionConstant.MSG;
import static com.mdmd.constant.ActionConstant.SUCCESS;
import static com.mdmd.constant.RedisConstant.*;
import static com.mdmd.service.impl.GameRuleSerciceImpl.fishRules;


@Service
public class SysPropServiceImpl implements SysPropService {

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SysPropDao sysPropDao;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private SqlDao sqlDao;

    private static final Logger LOGGER = LogManager.getLogger(SysPropServiceImpl.class);


    //记录所有的配置编号和值

    public void initSysProps(){
        try {
            List<SysPropEntity> list = commonDao.listAllEntity(SysPropEntity.class);
            Map<Integer, SysPropEntity> sysPropMap = getSysPropMap();
            if(list != null )
            {
                for (int i = 0; i < list.size(); i++) {
                    SysPropEntity sysPropEntity = list.get(i);
                    sysPropMap.put(sysPropEntity.getSysNum(),sysPropEntity);
                }
            }
            Map<Integer, SysPropEntity> s = getSysPropMap();
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("读取系统配置文件失败,终止服务!");
            LOGGER.error(e.getMessage());
            System.exit(0);
        }
    }

    public String updateSysprop(int sysNum, String sysValue) {
        Map<Integer, SysPropEntity> sysPropMap = getSysPropMap();
        if(!sysPropMap.containsKey(sysNum))
            return "不存在的参数";
        if(sysValue == null)
        {
            return  "num:"+sysNum+"不能为空值";
        }
        try {
            String val = sysValue.trim();
            switch (sysNum){
                case 1:
                    if(!val.contains("http"))
                    {
                        return "num:"+sysNum+"请加上前缀http或者https";
                    }
                    if(!val.contains("."))
                    {
                        return "num:" + sysNum + "网站格式不正确";
                    }
                    break;
                case 2:
                    if(val.contains("-"))
                    {
                        String[] split = val.split("-");
                        for (int i = 0; i < split.length; i++) {
                            String s = split[i].trim();
                            if(!"".equals(s))
                                Integer.valueOf(split[i]);
                        }
                    }
                    break ;
                case 3:
                    if(!"1".equals(val))
                        val = "0";
                    break;
                case 4:
                    if(!"1".equals(val))
                      val = "0";
                    break;
                case 5:
                    break;
                default:
                    break;
            }
            SysPropEntity sysPropEntity = sysPropMap.get(sysNum);
            sysPropEntity.setSysValue(sysValue);

            //更新数据库
            commonDao.updateEntity(sysPropEntity);
            return null;
        } catch (Exception e) {
          LOGGER.warn("num:"+sysNum+"修改时发生错误："+e.getMessage());
           return "num:"+sysNum+"修改时发生错误："+e.getMessage();
        }
    }

    public String updateSysprops(Map<Integer,String> sysMap) {
        Iterator<Map.Entry<Integer, String>> iterator = sysMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, String> next = iterator.next();
            String s = updateSysprop(next.getKey(), sysMap.get(next.getKey()));
            if(s != null)
                return s;
        }
        return null;
    }

    public List<SysPropResultJO> getSysprops_JO() {
        List<SysPropResultJO> sysPropResultJOS = new ArrayList<>();

        ArrayList<SysPropEntity> sysPropEntities = new ArrayList<>(getSysPropMap().values());

        for (int i = 0; i < sysPropEntities.size(); i++) {
            SysPropEntity sysPropEntity = sysPropEntities.get(i);
            sysPropResultJOS.add(new SysPropResultJO(sysPropEntity));
        }
        return sysPropResultJOS;
    }


    public List<FishRuleJO> listAllFishRule() {
        List<FishRuleEntity> list = commonDao.listAllEntity(FishRuleEntity.class);
        List<FishRuleJO> fishRuleJOS = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            fishRuleJOS.add(new FishRuleJO(list.get(i)));
        }
        return fishRuleJOS;
    }

    public boolean saveOrUpdateFishRules(List<FishRuleEntity> fishRuleEntities) throws RuntimeException{
        try {
            for (int i = 0; i < fishRuleEntities.size(); i++) {
                FishRuleEntity fishRuleEntity = fishRuleEntities.get(i);
                boolean exsit = exsitsFishRuleEntity_redis(fishRuleEntity);

                //id为0的是创建
                if(fishRuleEntity.getId() == 0)
                {
                    //创建时不能存在
                    if(exsit)
                    {
                        return false;
                    }
                    commonDao.addEntity(fishRuleEntity);
                }
                else
                {
                    //修改时必须存在
                    if(!exsit)
                    {
                        return false;
                    }
                    commonDao.updateEntity(fishRuleEntity);
                }
                //更新缓存中数据
                setFishRuleEntity_redis(fishRuleEntity);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    public boolean deleteFishRules(List<Integer> ids) throws Exception {
            for (int i = 0; i < ids.size(); i++) {
                Integer id = ids.get(i);
                FishRuleEntity fishRuleEntity =  commonDao.getEntity(FishRuleEntity.class, id);
                commonDao.removeEntity(FishRuleEntity.class,id);
                //更新缓存中数据
                delFishRuleEntity_redis(fishRuleEntity.getPrice(),fishRuleEntity.getTargetValue());
            }
            return true;
    }

    public <T> List<T> listDatas_page(Class<T> tClass, PageJO pageJO,  Map<String,Object> filter) throws Exception {
        int count = commonDao.countAllEntity_filter(tClass,filter);
        pageJO.setAllCount(count);
        PageBean pageData = CommonUtil.getPageData(pageJO);
        List<T> ts = commonDao.listAllEntity_limit_desctime(tClass, pageData, filter);
        return ts;
    }

    public List<AccountDatasJO> listAccountDatas(int dayNum) {

        String[] days = new String[dayNum];
        for (int i = 0; i < dayNum; i++) {
            days[i] = DateFormatUtil.getDayByCalendar(i - dayNum);
        }
        int start = Integer.valueOf(days[0].replace("-",""));
        int end = Integer.valueOf(days[days.length -1].replace("-",""));
        List<AccountDatasJO> accountDatasJOS = new ArrayList<>();
        List<AccountDatasEntity> accountDatasEntity = sysPropDao.getAccountDatasEntity(start, end);
        if(accountDatasEntity.size() == dayNum)
        {
            Gson gson = new Gson();
            Type type = new TypeToken<AccountDatasJO>() {
            }.getType();
            for (int i = 0; i < accountDatasEntity.size(); i++) {
                AccountDatasEntity datasEntity = accountDatasEntity.get(i);
                AccountDatasJO o = gson.fromJson(datasEntity.getJsonValue(), type);
                accountDatasJOS.add(o);
            }
        }
        else
        {
            Gson gson = new Gson();
            Type type = new TypeToken<AccountDatasJO>() {
            }.getType();
            HashMap<String, AccountDatasEntity> map = new HashMap<>();
            for (int i = 0; i < accountDatasEntity.size(); i++) {
                AccountDatasEntity account = accountDatasEntity.get(i);
                map.put(account.getTimeInt()+"",account);
            }
            for (int i = 0; i < days.length; i++) {
                String d = days[i];
                String day = d.replace("-","");
                if(map.containsKey(day))
                {
                    AccountDatasJO o = gson.fromJson(map.get(day).getJsonValue(), type);
                    accountDatasJOS.add(o);
                }
                else
                {
                    AccountDatasJO accountDatasJO = sqlDao.getAccountDatas(d);
                    AccountDatasEntity newAccount = new AccountDatasEntity();
                    newAccount.setTimeInt(Integer.valueOf(day));
                    newAccount.setJsonValue(gson.toJson(accountDatasJO));
                    commonDao.addEntity(newAccount);
                    accountDatasJOS.add(accountDatasJO);
                }
            }

        }
        return accountDatasJOS;
    }

    public  List<AccountDatasJO> listAccountDatas_today_all(){
        LinkedList<AccountDatasJO> accountDatasJOS = new LinkedList<>();
        String today = DateFormatUtil.getDayByCalendar(0);
        accountDatasJOS.add(sqlDao.getAccountDatas(today));
        accountDatasJOS.addLast(sqlDao.getAccountDatas_all());
        return accountDatasJOS;
    }
    public boolean doSetUser(int adminid) {
        AdminUserEntity entity = commonDao.getEntity(AdminUserEntity.class, adminid);
        return entity.getSetMoney() != 0;
    }

    public boolean dosetSys(int adminid) {
        AdminUserEntity entity = commonDao.getEntity(AdminUserEntity.class, adminid);
        return entity.getSetSys() != 0;
    }

    public void setUser(int adminid,int userId,String value, String type) throws Exception {
        UserEntity entity = commonDao.getEntity(UserEntity.class, userId);
        if(type.equals("gold"))
        {
            entity.setGold(Double.valueOf(value));
            commonDao.updateEntity(entity);
            LOGGER.warn("管理员"+adminid+"更改了用户"+userId+"的"+type +entity.getGold() +"为"+value);
        }
        else if(type.equals("commission"))
        {
            entity.setCommission(Double.valueOf(value));
            commonDao.updateEntity(entity);
            LOGGER.warn("管理员"+adminid+"更改了用户"+userId+"的"+type +entity.getCommission() +"为"+value);
        }
        else if(type.equals("ban"))
        {
            if(value.equals("true"))
            {
                byte b = 1;
                entity.setTopupBan(b);
                entity.setTakeoutBan(b);
                entity.setLoginBan(b);
                entity.setCommissionGiveBan(b);
                entity.setCommissionGetBan(b);
                LOGGER.warn("管理员"+adminid+"封禁了用户"+userId);
            }
            else
            {
                byte b = 0;
                entity.setTopupBan(b);
                entity.setTakeoutBan(b);
                entity.setLoginBan(b);
                entity.setCommissionGiveBan(b);
                entity.setCommissionGetBan(b);
                LOGGER.warn("管理员"+adminid+"解封了用户"+userId);
            }
            commonDao.updateEntity(entity);
        }
    }

    private boolean exsitsFishRuleEntity_redis( FishRuleEntity fishRuleEntity){
        String key = fishRuleEntity.getPrice() + REDIS_KEY_fishRules_itemkey + fishRuleEntity.getTargetValue();
        return  fishRules.containsKey( key);
    }

    private void setFishRuleEntity_redis( FishRuleEntity fishRuleEntity) throws Exception{
        String key = fishRuleEntity.getPrice() + REDIS_KEY_fishRules_itemkey + fishRuleEntity.getTargetValue();
        if(!redisCacheManager.hset(REDIS_KEY_fishRules, key, fishRuleEntity))
            if(!redisCacheManager.hset(REDIS_KEY_fishRules, key, fishRuleEntity))
            {
                LOGGER.error("捕鱼规则数据插入失败(redis插入错误)");
                throw new Exception("捕鱼规则数据插入失败(redis插入错误)");
            }
        fishRules.put(key,new FishProbabilityBean(fishRuleEntity));

    }



    private void delFishRuleEntity_redis(int price, int targetValue) throws Exception{
        String key = price + REDIS_KEY_fishRules_itemkey + targetValue;
        if(!redisCacheManager.hdel(REDIS_KEY_fishRules,key))
            if(!redisCacheManager.hdel(REDIS_KEY_fishRules,key))
            {
                LOGGER.error("捕鱼规则数据删除失败(redis插入错误)");
                throw new Exception("捕鱼规则数据删除失败(redis插入错误)");
            }
        fishRules.remove(key);
    }

}
