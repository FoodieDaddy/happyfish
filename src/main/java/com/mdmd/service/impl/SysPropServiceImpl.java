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

import static com.mdmd.Manager.SysPropManager.getSysPropMap;
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

    public String updateSysprop(SysPropEntity sysPropEntity) {
        Map<Integer, SysPropEntity> sysPropMap = getSysPropMap();
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
                    if(!("0".equals(val)||"1".equals(val)))
                        return "num:" + sysNum + " 值只能为1或0";
                    break;
                case 4:
                    if(!("0".equals(val)||"1".equals(val)))
                        return "num:" + sysNum + " 值只能为1或0";
                    break;
                case 5:
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
          LOGGER.warn("num:"+sysNum+"修改时发生错误："+e.getMessage());
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

    public List<SysPropResultJO> getSyspropWithSysNums_JO(List<Integer> ids) {
        List<SysPropResultJO> sysPropResultJOS = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            sysPropResultJOS.add(new SysPropResultJO(sysPropDao.getSysPropWithSysNum(ids.get(i))));
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
