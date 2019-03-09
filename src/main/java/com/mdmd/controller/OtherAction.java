package com.mdmd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.mdmd.service.impl.SysPropServiceImpl.sysPropMap;

@Controller
public class OtherAction {


    /**
     * 扫描二维码跳转
     * @param response
     */
    @RequestMapping(value = "/red.do")
    public void redictQr(HttpServletResponse response,String token){
        try {
            response.sendRedirect(sysPropMap.get(1).getSysValue() + "/wx/routerToMyPage.do?token="+token);
        } catch (IOException e) {
            try {
                response.sendRedirect("http://www.baidu.com");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}