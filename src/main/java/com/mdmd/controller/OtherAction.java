package com.mdmd.controller;

import com.mdmd.Manager.SysPropManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

import static com.mdmd.constant.SystemConstant.SCAN_QRCODE_URL_SUFFIX;

/**
 * 不需要验证session的其他方法
 */
@Controller
@RequestMapping("/ot")
public class OtherAction {


    /**
     * 扫描二维码跳转
     * @param response
     */
    @RequestMapping(value = "/red.do")
    public void redictQr(HttpServletResponse response, String token){
                try {
            response.sendRedirect(SysPropManager.getSystemWebUrl() + SCAN_QRCODE_URL_SUFFIX +token);
        } catch (IOException e) {
            try {
                response.sendRedirect("http://www.baidu.com");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    @RequestMapping(value = "/stopServer.do")
    public void stopServer(HttpServletResponse response, String token) {
        try {
            response.sendRedirect("/index.do?msg=我大大");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}