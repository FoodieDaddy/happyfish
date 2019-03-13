package com.mdmd.Manager;

import com.mdmd.entity.JO.SysPropResultJO;
import com.mdmd.entity.SysPropEntity;
import com.mdmd.util.DateFormatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysPropManager {
    private static Map<Integer, SysPropEntity> sysPropMap = new HashMap<>();


    public static Map<Integer, SysPropEntity> getSysPropMap() {
        return sysPropMap;
    }
    public static List<SysPropEntity> getSyspropWithSysNums(List<Integer> sysNums) {
        List<SysPropEntity> sysPropEntities = new ArrayList<>();
        for (int i = 0; i < sysNums.size(); i++) {
            SysPropEntity sysPropEntity = sysPropMap.get(sysNums.get(i));
            if(sysPropEntity != null)
                sysPropEntities.add(sysPropEntity);
        }
        return sysPropEntities;
    }

    public static List<SysPropResultJO> getSyspropWithSysNums_JO(List<Integer> sysNums) {
        List<SysPropResultJO> sysPropResultJOS = new ArrayList<>();
        for (int i = 0; i < sysNums.size(); i++) {
            SysPropEntity sysPropEntity = sysPropMap.get(sysNums.get(i));
            if(sysPropEntity != null)
                sysPropResultJOS.add(new SysPropResultJO(sysPropEntity));
        }
        return sysPropResultJOS;
    }

    /**
     * 获取系统网址
     * @return
     */
    public static String getSystemWebUrl(){
        return sysPropMap.get(1).getSysValue();
    }

    /**
     * 网站是否维护中,如果维护中，返回维护信息
     * @return
     */
    public static String stopWebServer(){
        if(sysPropMap.get(4).getSysValue().equals("0"))
            return null;
        return sysPropMap.get(5).getSysValue();
    }

    /**
     * 是否为双倍佣金时间
     * @return
     */
    public static   boolean isDoubleCommissionTime() {
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



}
