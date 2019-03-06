package com.mdmd.custom;

import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.dao.CommonDao;
import com.mdmd.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class RedisCustom {

    @Autowired
    private RedisCacheManager redisCacheManager;

    /**
     * 添加数据到reids中 仅支持上面的4类查询
     * @param addRedisObj
     * @param userId
     * @return
     */
    public boolean addRecordListForRedis(Object addRedisObj,int userId) {
        String keyName = addRedisObj.getClass().getSimpleName()+userId;
        //如果有表示被查询 才加入
        if(redisCacheManager.hasKey(keyName))
        {
            long size = redisCacheManager.lGetListSize(keyName);
            if(size < 12)
            {
                redisCacheManager.lSet(keyName,addRedisObj);
            }
            else
            {
                redisCacheManager.lRightPopAndLeftPush_IfSize(keyName,addRedisObj,12);
            }
            return true;
        }
        return false;
    }

}
