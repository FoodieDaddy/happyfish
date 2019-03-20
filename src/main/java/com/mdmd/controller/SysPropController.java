package com.mdmd.controller;

import com.mdmd.entity.*;
import com.mdmd.entity.JO.*;
import com.mdmd.enums.SysSearchEnum;
import com.mdmd.service.AdminUserService;
import com.mdmd.service.SysPropService;
import com.mdmd.service.UserService;
import com.mdmd.util.JsonObjectForPageAndFilter;
import com.mdmd.util.RSAUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private UserService userService;
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
                    response.sendRedirect("../stc/admin/index.html?token=" + encrypt);
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
                        response.sendRedirect("../stc/admin/index.html?token=" + encrypt);
                        return;
                    }
                    adminUserEntity = adminUserService.getAdminUserEntityFromOpenid(openId);

                }
            }
            if (adminUserEntity != null) {
                int adminId = adminUserEntity.getAdminid();
                //将userId加密传输到前端
                String encrypt = RSAUtil.encrypt(adminId + "");
                response.sendRedirect("../stc/admin/index.html?token=" + encrypt);
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
            result.put("adminid",adminid);
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
    public Map<String,Object> getSysProp(HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SysPropResultJO> syspropWithSysNums_jo = sysPropService.getSysprops_JO();
            result.put("list",syspropWithSysNums_jo);
            result.put(SUCCESS,true);
        } catch (Exception e) {
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
            LOGGER.warn(e.getMessage());
        }
        return result;
    }

    /**
     * 修改配置
     */
    @RequestMapping(value = "/setSysProp.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setSysProps(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            int adminid = (int) session.getAttribute(SESSION_ADMINID);
            if(!sysPropService.dosetSys(adminid))
            {
                result.put(SUCCESS,false);
                result.put(MSG,"没有修改权限");
            }
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<Integer, String> sysMap = new HashMap<>();
            Iterator<Map.Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String[]> next = iterator.next();
                String key = next.getKey();
                if(key.contains("sys_"))
                {
                    String sys_ = key.replace("sys_", "").trim();
                    sysMap.put(Integer.valueOf(sys_),next.getValue()[0].trim());
                }
            }
            String msg = null;
            if(sysMap.size() > 0)
            {
                msg = sysPropService.updateSysprops(sysMap);
            }
            if(msg == null)
            {
                result.put(SUCCESS,true);
            }
            else
            {
                result.put(SUCCESS,false);
                result.put(MSG,msg);
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
            if(sysPropService.saveOrUpdateFishRules(Arrays.asList(fishRuleEntities)))
            {
                result.put(SUCCESS,true);
            }
            else
            {
                result.put(SUCCESS,false);
                result.put(MSG,"操作失败，创建或为已存在，修改或为不存的金额与目标组");
            }

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
            System.out.println(dels);
            System.out.println(split);
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

    @RequestMapping(value = "/listDatas.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> listDatas_page(@RequestBody JsonObjectForPageAndFilter pageAndFilter){
        Map<String, Object> result = new HashMap<>();
        try {
            PageJO pageJO = pageAndFilter.getPageJO();
            Map<String, Object> filter = pageAndFilter.getFilter();
            Map<String, Object> sort = pageAndFilter.getSort();
            String type = pageAndFilter.getString("type");
            SysSearchEnum anEnum = SysSearchEnum.getEnum(type);
            if(anEnum == null)
            {
                throw new Exception("参数值错误");
            }
            List list = null;
            switch (anEnum){
                case search_user:
                    list = userService.listUserEntity_page(pageJO,filter,sort);
                    break;
                case search_topup:
                    List<UserTopupEntity> list1 = sysPropService.listDatas_page(UserTopupEntity.class, pageJO, filter);
                    list = new ArrayList();
                    for (int i = 0; i < list1.size(); i++) {
                        list.add(new UserTopupJO(list1.get(i)));
                    }
                    break;
                case search_takeout:
                    List<UserTakeoutEntity> list2 = sysPropService.listDatas_page(UserTakeoutEntity.class, pageJO, filter);
                    list = new ArrayList();
                    for (int i = 0; i < list2.size(); i++) {
                        list.add(new UserTakeoutJO(list2.get(i)));
                    }
                    break;
                case search_gamerecord:
                    List<GameRecordEntity> list3 = sysPropService.listDatas_page(GameRecordEntity.class, pageJO, filter);
                    list = new ArrayList();
                    for (int i = 0; i < list3.size(); i++) {
                        list.add(new GameRecordJO(list3.get(i)));
                    }
                    break;
                case search_ordertopup:
                    List<UserOrderTopupEntity> list4 = sysPropService.listDatas_page(UserOrderTopupEntity.class, pageJO, filter);
                    list = new ArrayList();
                    for (int i = 0; i < list4.size(); i++) {
                        list.add(new UserOrderTopupJO(list4.get(i)));
                    }
                    break;
                case search_adminuser:
                    list = sysPropService.listDatas_page(AdminUserEntity.class, pageJO, filter);
                    break;
            }
            result.put(SUCCESS,true);
            result.put("list",list);
            result.put("page",pageJO);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
        }
        return result;
    }
    @RequestMapping("/setUser.do")
    @ResponseBody
    public Map<String,Object> setUser(HttpServletRequest request,HttpSession session){
        Map<String, Object> result = new HashMap<>();
        String u= request.getParameter("userId");
        String gold = request.getParameter("gold");
        String commission = request.getParameter("commission");
        String ban = request.getParameter("ban");
        int adminid = (int) session.getAttribute(SESSION_ADMINID);
        try {
            int userId = Integer.valueOf(u);
            if(sysPropService.doSetUser(adminid))
            {
                if(gold != null &&!gold.equals(""))
                {
                    sysPropService.setUser(adminid,userId,gold,"gold");

                }
                if(commission != null && !commission.equals(""))
                {
                    sysPropService.setUser(adminid,userId,commission,"commission");


                }
                if(ban != null && !ban.equals(""))
                {
                    sysPropService.setUser(adminid,userId,ban,"ban");

                }
            }
            else
            {
                result.put(MSG,"没有权限");
                result.put(SUCCESS,false);

            }
            result.put(SUCCESS,true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            LOGGER.warn(e.getMessage());
        }
        return result;
    }


    /**
     * 获取近几天的汇总数据，包含总收入，充值，金币提现，佣金提现，夺宝次数
     */
    @RequestMapping("/calc_today.do")
    @ResponseBody
    public Map<String,Object> calcToday(String type){
        Map<String, Object> result = new HashMap<>();
        try {
            List<AccountDatasJO> accountDatasJOS = sysPropService.listAccountDatas_today_all();
            result.put("today",accountDatasJOS.get(0));
            result.put("all",accountDatasJOS.get(1));
            result.put(SUCCESS,true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            LOGGER.warn(e.getMessage());
        }
        return result;
    }

    /**
     * 获取近几天的汇总数据，包含总收入，充值，金币提现，佣金提现，夺宝次数
     */
    @RequestMapping("/calc_data.do")
    @ResponseBody
    public Map<String,Object> calcData(String type){
        Map<String, Object> result = new HashMap<>();
        try {
            if(type.equals("d7"))
            {
                List<AccountDatasJO> accountDatasJOS = sysPropService.listAccountDatas(7);
                Collections.sort(accountDatasJOS,new Comparator<AccountDatasJO>() {
                    @Override
                    public int compare(AccountDatasJO o1, AccountDatasJO o2) {
                        return Integer.valueOf(o1.getTime().replace("-",""))
                                - Integer.valueOf(o2.getTime().replace("-",""));

                    }
                });
                result.put("d7",accountDatasJOS);
            }
            result.put(SUCCESS,true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            LOGGER.warn(e.getMessage());
        }
        return result;
    }
}
