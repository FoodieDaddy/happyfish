package com.mdmd.listener;

import com.mdmd.service.DataService;
import com.mdmd.service.SysPropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * 监听spring读取application。
 * 用作初始化某些配置
 */
@Service
public class InitDataListener  implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DataService dataService;
    @Autowired
    private SysPropService sysPropService;
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String displayName = event.getApplicationContext().getDisplayName();
        if(displayName.equals("Root WebApplicationContext"))
        {
            //缓存初始化
            dataService.initCache();
            //系统配置初始化
            sysPropService.initSysProps();
        }
    }
}
