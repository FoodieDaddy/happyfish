package com.mdmd.custom;

import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.dao.CommonDao;
import com.mdmd.entity.RedisPubsubFailEntity;
import com.mdmd.enums.RedisChannelEnum;
import com.mdmd.util.DateFormatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mdmd.listener.RedisMsgPubSubListener.redisGson;


/**
 * custom类是防止service层调用service层的一种疏通手段 此类中不应该有增删改操作
 */
@Component
public class RedisCustom {
    private static final Logger LOGGER = LogManager.getLogger(RedisCustom.class);

    @Autowired
    private CommonDao commonDao;

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

    /**
     * 发布消息到频道  此方法必须在service中调用，因为此类没有事务，而该方法中拥有增加操作
     * @param channelEnum
     * @param val
     */
    public void publish(RedisChannelEnum channelEnum, Object val) {
        String channel = channelEnum.getChannel();
        String v = redisGson.toJson(val);
        try {
            redisCacheManager.publish(channel,v);
            LOGGER.info(channel+"频道发送>>>消息时间"+ DateFormatUtil.now_yyyyMMddHHmmss());
        } catch (Exception e) {
            try {
                //重试发送
                redisCacheManager.publish(channel,v);
                LOGGER.info(channel+"频道发送>>>消息时间"+ DateFormatUtil.now_yyyyMMddHHmmss());
            } catch (Exception e1) {
                LOGGER.error("消息发布失败,发往频道"+channel);
                commonDao.addEntity(new RedisPubsubFailEntity(channel,v,e.getMessage()));
            }
        }
    }
}
