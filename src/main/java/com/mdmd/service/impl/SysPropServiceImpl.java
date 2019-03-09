package com.mdmd.service.impl;

import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.controller.GameAction;
import com.mdmd.dao.CommonDao;
import com.mdmd.dao.SysPropDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.FishRuleEntity;
import com.mdmd.entity.JO.FishRuleJO;
import com.mdmd.entity.JO.SysPropResultJO;
import com.mdmd.entity.SysPropEntity;
import com.mdmd.service.SysPropService;
import com.mdmd.util.DateFormatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.mdmd.constant.ActionConstant.MSG;
import static com.mdmd.constant.ActionConstant.SUCCESS;
import static com.mdmd.constant.RedisConstant.*;


@Service
public class SysPropServiceImpl implements SysPropService {

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SysPropDao sysPropDao;
    @Autowired
    private RedisCacheManager redisCacheManager;

    private static final Logger LOGGER = LogManager.getLogger(SysPropServiceImpl.class);


    //记录所有的配置编号和值
    public static Map<Integer,SysPropEntity> sysPropMap = null;

    public void initSysProps(){
        try {
            List<SysPropEntity> list = commonDao.listAllEntity(SysPropEntity.class);
            if(list != null)
            {
                sysPropMap = new HashMap<>();
                for (int i = 0; i < list.size(); i++) {
                    SysPropEntity sysPropEntity = list.get(i);
                    sysPropMap.put(sysPropEntity.getSysNum(),sysPropEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("读取系统配置文件失败,终止服务!");
            LOGGER.error(e.getMessage());
            System.exit(0);
        }
    }

    public String updateSysprop(SysPropEntity sysPropEntity) {
        int sysNum = sysPropEntity.getSysNum();
        if(!sysPropMap.containsKey(sysNum))
            return "不存在的参数";
        String sysName = sysPropEntity.getSysName();
        String sysValue = sysPropEntity.getSysValue();
        if(sysName.trim().equals(""))
        {

            return "num:"+sysNum+"名字不能为空";
        }
        if(sysValue == null)
        {
            return  "num:"+sysNum+"不能为空值";
        }
        try {
            String val = sysValue.toString().trim();
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
                    Integer.valueOf(val);
                    break;
                default:
                    return "num:" + sysNum + "不存在";
            }
            //修改全局变量
            sysPropMap.put(sysNum,sysPropEntity);
            //更新数据库
            commonDao.updateEntity(sysPropEntity);
            return null;
        } catch (Exception e) {
           e.printStackTrace();
           return "num:"+sysNum+"修改时发生错误："+e.getMessage();
        }
    }

    public String updateSysprops(List<SysPropEntity> sysPropEntities) {
        for (int i = 0; i < sysPropEntities.size(); i++) {
            String s = updateSysprop(sysPropEntities.get(i));
            if(s != null)
                return s;
        }
        return null;
    }


    public SysPropEntity getSyspropWithSysNum(int sysNum) {
        return sysPropMap.get(sysNum);
    }

    public SysPropResultJO getSyspropWithSysNum_JO(int sysNum) {
        return new SysPropResultJO(sysPropMap.get(sysNum));
    }

    public List<SysPropEntity> getSyspropWithSysNums(List<Integer> sysNums) {
        List<SysPropEntity> sysPropEntities = new ArrayList<>();
        for (int i = 0; i < sysNums.size(); i++) {
            SysPropEntity sysPropEntity = sysPropMap.get(sysNums.get(i));
            if(sysPropEntity != null)
                sysPropEntities.add(sysPropEntity);
        }
        return sysPropEntities;
    }

    public List<SysPropResultJO> getSyspropWithSysNums_JO(List<Integer> sysNums) {
        List<SysPropResultJO> sysPropResultJOS = new ArrayList<>();
        for (int i = 0; i < sysNums.size(); i++) {
            SysPropEntity sysPropEntity = sysPropMap.get(sysNums.get(i));
            if(sysPropEntity != null)
                sysPropResultJOS.add(new SysPropResultJO(sysPropEntity));
        }
        return sysPropResultJOS;
    }

    public List<SysPropEntity> getAllSysprop() {
        return new ArrayList<>(sysPropMap.values());
    }

    public  boolean isDoubleCommissionTime() {
        try {

            SysPropEntity catchDouSys = sysPropMap.get(3);
            String sysValue = catchDouSys.getSysValue();
            int now = DateFormatUtil.now_HH();
            //是否开启双倍佣金
            if(sysValue.contains("1"))
            {
                SysPropEntity doubleTimeSys = sysPropMap.get(2);
                //保证每个时间后面都有-
                String timeString = doubleTimeSys.getSysValue().trim()+"-";
                String now_ = now + "-";
                if(timeString.contains(now_))
                    return true;

            }
        } catch (Exception e) {
            return false;
        }
        return false;
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
                //id为0的是创建
                if(fishRuleEntity.getId() == 0)
                {
                    commonDao.addEntity(fishRuleEntity);
                }
                else
                {
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

    public boolean deleteFishRules(List<Integer> ids) throws RuntimeException {
        try {
            for (int i = 0; i < ids.size(); i++) {
                Integer id = ids.get(i);
                commonDao.removeEntity(FishRuleEntity.class,id);
                //更新缓存中数据
                FishRuleEntity fishRuleEntity = (FishRuleEntity) commonDao.getEntity(FishRuleEntity.class, id);
                delFishRuleEntity_redis(fishRuleEntity.getPrice(),fishRuleEntity.getTargetValue());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    private void setFishRuleEntity_redis( FishRuleEntity fishRuleEntity) {
        String key = fishRuleEntity.getPrice() + REDIS_KEY_fishRules_itemkey + fishRuleEntity.getTargetValue();
        if(!redisCacheManager.hset(REDIS_KEY_fishRules, key, fishRuleEntity))
            if(!redisCacheManager.hset(REDIS_KEY_fishRules, key, fishRuleEntity))
                LOGGER.error("捕鱼规则数据插入失败(redis插入错误)");
    }



    private void delFishRuleEntity_redis(int price, int targetValue) {
        String key = price + REDIS_KEY_fishRules_itemkey + targetValue;
        if(!redisCacheManager.hdel(REDIS_KEY_fishRules,key))
            if(!redisCacheManager.hdel(REDIS_KEY_fishRules,key))
                LOGGER.error("捕鱼规则数据删除失败(redis插入错误)");
    }

}
