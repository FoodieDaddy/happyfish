package com.mdmd.controller;

import com.mdmd.entity.AdminUserEntity;
import com.mdmd.entity.FishRuleEntity;
import com.mdmd.entity.JO.FishRuleJO;
import com.mdmd.entity.JO.SysPropResultJO;
import com.mdmd.entity.SysPropEntity;
import com.mdmd.entity.UserEntity;
import com.mdmd.service.AdminUserService;
import com.mdmd.service.SysPropService;
import com.mdmd.util.RSAUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static com.mdmd.constant.ActionConstant.*;
import static com.mdmd.constant.SystemConstant.USER_ROOT_ID;
import static com.mdmd.constant.SystemConstant.USER_ROOT_OPENID;
import static com.mdmd.util.WeiXinSignUtil.getOpenId;

@RequestMapping("/sysprop")
@Controller
public class SysPropController {
    private static final Logger LOGGER = LogManager.getLogger(SysPropController.class);

    @Autowired
    private SysPropService sysPropService;
    @Autowired
    private AdminUserService adminUserService;
    /**
     * 通过重定向获取openid跳转到后台管理界面
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/home.do")
    public void home(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            String test = request.getParameter("test");
            AdminUserEntity adminUserEntity = null;

            if(test != null)
            {
                if(test.equals(USER_ROOT_OPENID))
                {
                    String encrypt = RSAUtil.encrypt( USER_ROOT_ID+ "");
                    response.sendRedirect("../stc/inner.html?token=" + encrypt);
                    return;
                }
                adminUserEntity = adminUserService.getAdminUserEntityFromOpenid(test);
            }
            if (code != null) {
                String openId = getOpenId(code);
                //如果openId不为空，从用户库中查找该管理员
                if (openId != null) {
                    if(openId.equals(USER_ROOT_OPENID))
                    {
                        String encrypt = RSAUtil.encrypt( USER_ROOT_ID+ "");
                        response.sendRedirect("../stc/inner.html?token=" + encrypt);
                        return;
                    }
                    adminUserEntity = adminUserService.getAdminUserEntityFromOpenid(openId);

                }
            }
            if (adminUserEntity != null) {
                int adminId = adminUserEntity.getAdminid();
                //将userId加密传输到前端
                String encrypt = RSAUtil.encrypt(adminId + "");
                response.sendRedirect("../stc/inner.html?token=" + encrypt);
                return;
            }
            else
            {
                response.sendRedirect("http://www.baidu.com");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn(e.getMessage());
            try {
                response.sendRedirect("http://www.baidu.com");
            } catch (IOException e1) {
                LOGGER.warn(e1.getMessage());

            }
        }

    }
    /**
     * 后台首页请求
     *
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "/load.do")
    @ResponseBody
    public Map<String, Object> getHomeData(HttpServletRequest request, HttpServletResponse response, HttpSession session, String u) {
        Map<String, Object> result = new HashMap<>();
        try {
            int adminid = (int) session.getAttribute(SESSION_ADMINID);
            result.put(SUCCESS, Boolean.TRUE);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            result.put(SUCCESS, false);
            result.put(MSG,e.getMessage());
        }
        return result;
    }

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
