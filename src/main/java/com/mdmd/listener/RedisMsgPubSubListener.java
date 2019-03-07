package com.mdmd.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.entity.GameRecordEntity;
import com.mdmd.entity.RO.SuperCommRO;
import com.mdmd.enums.RedisChannelEnum;
import com.mdmd.service.GameRuleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mdmd.constant.SystemConstant.DATEFORMAT__yyyyMMddHHmmss;
import static com.mdmd.enums.RedisChannelEnum.channel_superComm;
import static com.mdmd.enums.RedisChannelEnum.channel_test;

/**
 * 接受redis消息
 */
@Component
public class RedisMsgPubSubListener implements MessageListener {

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    private GameRuleService gameRuleService;
    public static final Gson redisGson = new Gson();

    private static final Logger LOGGER = LogManager.getLogger(RedisMsgPubSubListener.class);

    /**
     * 监听频道信息 频道接受设置在spring-content.xml的redisMessageListenerContainer中
     * @param message
     * @param bytes
     */
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer<?> serializer = redisCacheManager.getValueSerializer();
        // message.getBody()是Redis的值，需要用redis的valueSerializer反序列化
        String channel = new String(message.getChannel());
        LOGGER.info(channel+"频道接收<<<消息时间"+ new SimpleDateFormat(DATEFORMAT__yyyyMMddHHmmss).format(new Date()));
        String connent = serializer.deserialize(message.getBody()).toString();
        //接受测试类消息，仅文本
        if(channel.equals(channel_test.getChannel()))
        {
            LOGGER.info(connent);
            System.out.println("接收到消息:"+connent);
        }
        else if(channel.equals(channel_superComm.getChannel()))
        {
            SuperCommRO superCommRO = redisGson.fromJson(connent, new TypeToken<SuperCommRO>() {
            }.getType());
            gameRuleService.calcuCommission(superCommRO.getUserId(),superCommRO.getPrice());
        }

        //负荷预警
        int activeCount = Thread.activeCount();
        if(activeCount > 150)
        {
            LOGGER.error("订阅频道即将崩溃，当前线程数：" + activeCount);
            //todo 通知管理者
        }
        else if(activeCount > 100)
        {
            LOGGER.error("订阅频道线程负荷过大，当前线程数：" + activeCount);
            //todo 通知管理者
        }
        else if(activeCount > 50)
        {
            LOGGER.warn("订阅频道线程繁忙，当前线程数：" + activeCount);
        }
        else if(activeCount > 20)
        {
            LOGGER.warn("订阅频道当前线程活动数超过核心线程数，当前线程数：" + activeCount);
        }
    }
}
