package com.mdmd.service.impl;

import com.mdmd.controller.GameAction;
import com.mdmd.dao.CommonDao;
import com.mdmd.dao.SysPropDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.SysPropEntity;
import com.mdmd.service.SysPropService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class SysPropServiceImpl implements SysPropService {

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SysPropDao sysPropDao;

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

    public void updateSysprop(int sysNum, String sysValue) {
        if(!sysPropMap.containsKey(sysNum))
            return;
        //修改全局变量
        SysPropEntity sysPropEntity = sysPropMap.get(sysNum);
        sysPropEntity.setSysValue(sysValue);
        sysPropMap.put(sysNum,sysPropEntity);
        //更新数据库
        commonDao.updateEntity(sysPropEntity);

    }

    public SysPropEntity getSyspropWithSysNum(int sysNum) {
        return sysPropMap.get(sysNum);
    }

    public List<SysPropEntity> getAllSysprop() {
        return new ArrayList<>(sysPropMap.values());
    }

    public  boolean isDoubleCommissionTime() {
        try {

            SysPropEntity catchDouSys = sysPropMap.get(3);
            String sysValue = catchDouSys.getSysValue();
            Calendar calendar = Calendar.getInstance();
            String now = calendar.get(Calendar.HOUR_OF_DAY) + "";
            //是否开启双倍佣金
            if(sysValue.contains("1"))
            {
                SysPropEntity doubleTimeSys = sysPropMap.get(2);
                String timeString = doubleTimeSys.getSysValue().trim();
                if(timeString.contains("-"))
                {
                    String[] split = timeString.split("-");
                    for (int i = 0; i < split.length; i++) {
                        if(now.equals(split[i]))
                            return true;
                    }
                }
                else if(now.equals(timeString))
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
