package com.mdmd.security;


import com.mdmd.Manager.SysPropManager;
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

import static com.mdmd.constant.ActionConstant.*;
import static com.mdmd.security.IpHelper.getIpAddress;
import static com.mdmd.security.IpHelper.isBlackIp;

public class WXInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        if(requestURI.contains("/wx/"))
        {
            return true;
        }
        String msg = SysPropManager.stopWebServer();
        if(msg != null)
        {
            response.sendRedirect(SysPropManager.getSystemWebUrl()+"/stc/stop_server.html?msg="+msg);
            return false;
        }
        HttpSession session = request.getSession();
        if(requestURI.contains("/qx/")){
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
        else if(requestURI.contains("/gm/"))
        {

            Object userId = session.getAttribute(SESSION_USERID);
            if(userId == null)
                return false;
            return true;
        }
        //充值
        else if(requestURI.contains("/topup/"))
        {

            if(requestURI.contains("/record.do"))
                return true;
            Object userId = session.getAttribute(SESSION_USERID);
            if(userId == null)
                return false;
            return true;
        }
        else if(requestURI.contains("/ot/"))
        {
            if(requestURI.contains("/red.do"))
            {
                boolean blackIp = isBlackIp(request);
                if(blackIp)
                    response.sendRedirect(URL_Be_Ban);
            }
            return true;
        }

        //todo 后台登录验证 也要验证user
        else if(requestURI.contains("/sysprop/"))
        {
            Object adminid = session.getAttribute(SESSION_ADMINID);
            if(adminid == null)
            {
                if(requestURI.contains("/home.do"))
                {
                    return true;
                }
                if(requestURI.contains("/load.do"))
                {
                    String u = request.getParameter("u");
                    if(u!=null&&!u.equals(""))
                    {
                            String ad = RSAUtil.decryptByHttpRequestValue(u);
                            int adminidInt = Integer.valueOf(ad);
                            session.setAttribute(SESSION_ADMINID,adminidInt);
                            return true;
                    }
                }
            }
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
