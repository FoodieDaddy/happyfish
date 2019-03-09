package com.mdmd.listener;

import com.mdmd.custom.RedisCustom;
import com.mdmd.enums.RedisChannelEnum;
import com.mdmd.service.DataService;
import com.mdmd.service.GameRuleService;
import com.mdmd.service.SysPropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 监听spring读取application。
 * 用作初始化某些配置和数据
 */
@Component
public class InitDataListener  implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DataService dataService;
    @Autowired
    private SysPropService sysPropService;
    @Autowired
    private RedisCustom redisCustom;
    @Autowired
    private GameRuleService gameRuleService;
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String displayName = event.getApplicationContext().getDisplayName();
        if(displayName.equals("Root WebApplicationContext"))
        {
            //缓存初始化
            dataService.initCache();
            //捕鱼规则初始化
            gameRuleService.initFishRules();
            //系统配置初始化
            sysPropService.initSysProps();
            //测试redispubsub的运行
            redisCustom.publish(RedisChannelEnum.channel_test,"1");
        }
    }
}
