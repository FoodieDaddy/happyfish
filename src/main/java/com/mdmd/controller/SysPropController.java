package com.mdmd.controller;

import com.mdmd.entity.FishRuleEntity;
import com.mdmd.entity.JO.FishRuleJO;
import com.mdmd.entity.JO.SysPropResultJO;
import com.mdmd.entity.SysPropEntity;
import com.mdmd.service.SysPropService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
        try {
            List<SysPropResultJO> syspropWithSysNums_jo = sysPropService.getSyspropWithSysNums_JO(Arrays.asList(sys_num));
            result.put("list",syspropWithSysNums_jo);
            result.put(SUCCESS,true);
        } catch (Exception e) {
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
            LOGGER.warn(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/setSysProp.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setSysProps(HttpServletRequest request, HttpSession session, HttpServletResponse response,
                                         @RequestBody SysPropEntity[] sysArr) {
        Map<String, Object> result = new HashMap<>();
        try {
            String s = sysPropService.updateSysprops(Arrays.asList(sysArr));
            if(s == null)
            {
                result.put(SUCCESS,true);
            }
            else
            {
                result.put(SUCCESS,false);
                result.put(MSG,s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/getFishRules.do")
    @ResponseBody
    public Map<String,Object> getFishRules(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<FishRuleJO> fishRuleJOS = sysPropService.listAllFishRule();
            result.put(SUCCESS,true);
            result.put("list",fishRuleJOS);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/addFishRules.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addFishRules(@RequestBody FishRuleEntity[] fishRuleEntities, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            sysPropService.saveOrUpdateFishRules(Arrays.asList(fishRuleEntities));
            result.put(SUCCESS,true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/delFishRules.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delFishRules(String dels,HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            String[] split = dels.trim().split(",");
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                String id = split[i];
                if(!"".equals(id))
                    ids.add(Integer.valueOf(id));
            }
            sysPropService.deleteFishRules(ids);
            result.put(SUCCESS,true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
        }
        return result;
    }

}
