package com.mdmd.controller;

import com.mdmd.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class TestCtl {



    @RequestMapping(value = "/s.do")
    public String lalal(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        try {
            String userId = "26";
            String encrypt = RSAUtil.encrypt(userId);
            Cookie cookie = new Cookie("u",encrypt);
            response.addCookie(cookie);
            response.sendRedirect("index.html");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
    }

    @RequestMapping(value = "/k.do")
    public String lalalk(HttpServletRequest request, HttpServletResponse response, HttpSession session,String s){
        try {
            String s2 = request.getParameter("s");
            String password = s2.trim();  //取值
            password= password.replaceAll(" ","+");//
            String s1 = RSAUtil.decryptBy(password);
            System.out.println("jiemi--:"+ s1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
    }

}