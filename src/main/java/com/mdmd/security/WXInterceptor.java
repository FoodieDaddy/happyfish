package com.mdmd.security;


import com.mdmd.entity.UserEntity;
import com.mdmd.service.DataService;
import com.mdmd.service.UserService;
import com.mdmd.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.mdmd.constant.ActionConstant.SESSION_USERID;
import static com.mdmd.security.IpHelper.getIpAddress;

public class WXInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String requestURI = request.getRequestURI();
        if(requestURI.contains("/gm/")||requestURI.contains("/qx/")){
            Object userId = session.getAttribute(SESSION_USERID);
            if(userId == null)
            {
                if(requestURI.contains("/home.do"))
                {
                    return true;
                }
                if(requestURI.contains("/getHomeData.do"))
                {
                    String u = request.getParameter("u");
                    if(u!=null&&!u.equals(""))
                    {
                        try {
                            String user = RSAUtil.decryptByHttpRequestValue(u);
                            int userIdInt = Integer.valueOf(user);
                            UserEntity userEntity = userService.getUserWithUserId_self_or_cascade(userIdInt, true);
                            byte loginBan = userEntity.getLoginBan();
                            if(loginBan == 0)
                            {
                                session.setAttribute(SESSION_USERID,userIdInt);
                                return true;
                            }
                        } catch (Exception e) {
                            return false;
                        }

                    }
                    else
                    {
                        return false;
                    }
                }
                return false;
            }
            return true;
        }
        //不需要做session判断的方法
        else if(requestURI.contains("/MP_verify_bvG4JnJzyk7M3RNH.txt"))
        {
            return true;
        }
        else if(requestURI.contains("/red.do"))
        {
            String ipAddress = getIpAddress(request);
            System.out.println("ip:"+ipAddress);
            return true;
        }
        //不需要做session的命名空间
        else if(requestURI.contains("/wx/"))
        {
            return true;
        }
        //todo 后台登录验证 也要验证user
        else if(requestURI.contains("/sysprop/"))
        {
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
