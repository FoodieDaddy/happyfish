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
     * 微信数字认证
     * @param response
     */
    @RequestMapping(value = "MP_verify_bvG4JnJzyk7M3RNH.txt")
    public void rediat_uri_depend(HttpServletResponse response){
        PrintWriter writer = null;
        try {
             writer = response.getWriter();
             writer.print("bvG4JnJzyk7M3RNH");//将微信提供的txt文件中的代码返回
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(writer != null)
                writer.close();
        }
    }

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