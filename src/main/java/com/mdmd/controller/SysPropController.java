package com.mdmd.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mdmd.entity.JO.SysPropResultJO;
import com.mdmd.entity.SysPropEntity;
import com.mdmd.service.SysPropService;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import static com.mdmd.constant.ActionConstant.MSG;
import static com.mdmd.constant.ActionConstant.SUCCESS;

@Component
@RequestMapping("/sysprop")
public class SysPropController {
    @Autowired
    private SysPropService sysPropService;
    private static final Logger LOGGER = LogManager.getLogger(SysPropController.class);


    /**
     * 获取配置
     */
    @RequestMapping(value = "/getSysProp.do")
    @ResponseBody
    public Map<String,Object> getSysProp(HttpServletRequest request, HttpSession session, HttpServletResponse response,
                                         @RequestBody Integer[] sys_num) {
        Map<String, Object> result = new HashMap<>();
        List<SysPropResultJO> sysPropResultJOS = new ArrayList<>();
        try {
            for (int i = 0; i < sys_num.length; i++) {
                int sysNum = sys_num[i];
                SysPropEntity sysPropEntity = sysPropService.getSyspropWithSysNum(sysNum);
                if(sysPropEntity == null)
                {
                    result.put(SUCCESS,false);
                    result.put(MSG,"不存在的配置");
                    return result;
                }

                Object newVal ;
                String sysValue = sysPropEntity.getSysValue();
                List<SysPropResultJO> sysPropList = new LinkedList<>();
                switch (sysNum){
                    case 2 :
                        newVal = Arrays.asList(sysValue.split("-"));
                        break;
                    default:
                        newVal = sysValue;
                        break;
                }
                SysPropResultJO sysPropResultJO = new SysPropResultJO(sysPropEntity.getSysNum(), sysPropEntity.getSysName(), newVal);
                sysPropResultJOS.add(sysPropResultJO);
            }
            result.put("data",sysPropResultJOS);
            result.put(SUCCESS,true);
        } catch (Exception e) {
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
            LOGGER.warn(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/getFishRules.do")
    @ResponseBody
    public Map<String,Object> getFishRules(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();

        return result;
    }
}
