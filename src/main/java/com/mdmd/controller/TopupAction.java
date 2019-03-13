package com.mdmd.controller;

import com.mdmd.entity.UserTopupEntity;
import com.mdmd.service.TopupService;
import com.mdmd.util.CommonUtil;
import com.mdmd.util.MD5Util;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static com.mdmd.constant.ActionConstant.MSG;
import static com.mdmd.constant.ActionConstant.SUCCESS;
import static com.mdmd.constant.SystemConstant.TOPUP_token;

@Component
@RequestMapping("/topup")
public class TopupAction {
    private static final Logger LOGGER = LogManager.getLogger(TopupAction.class);
    @Autowired

    private TopupService topupService;

    @RequestMapping(value = "/sync.do")
    @ResponseBody
    public Map<String,Object> orderNum(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        int userId = (int) session.getAttribute("userId");
        try {
            String t = request.getParameter("type");
            String m = request.getParameter("money");
            int type = Integer.valueOf(t);
            int money = Integer.valueOf(m);
            if(money < 0)
            {
                result.put(MSG,"金额错误");
                result.put(SUCCESS,false);
                return result;
            }
            if(type != 1 && type != 2)
            {
                result.put(MSG,"支付参数错误");
                result.put(SUCCESS,false);
                return result;
            }
            SortedMap<String, Object> parametersForTopup = topupService.getParametersForTopup(userId, money, type);
            result.put("data",parametersForTopup);
            result.put(SUCCESS,true);
        } catch (Exception e) {
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping(value = "/record.do",method = RequestMethod.POST)
    @ResponseBody
    public String apipay(HttpServletRequest request) {
        String bill_no = request.getParameter("bill_no");
        String price = request.getParameter("price");
        String orderid = request.getParameter("orderid");
        String actual_price = request.getParameter("actual_price");//用户实际充值 单位分
        String orderuid = request.getParameter("orderuid");//自定义用户id
        String account_name = request.getParameter("account_name");//对应收款账号
        String key = request.getParameter("key");//接收参数sign
        LOGGER.info("billno= "+bill_no+";orderid= "+orderid+";price= "+price+";acprice= "+actual_price+";orderuid= "+orderuid +";name="+account_name);
        double a_p = 0;
        try {
            if(orderuid == null)
            {
                return "fail";
            }

            int userId = Integer.valueOf(orderuid);
            String newSign = MD5Util.MD5Encode(actual_price + bill_no + orderid + orderuid + price + TOPUP_token, "utf-8");
            if(newSign.equals(key))
            {
                a_p =  CommonUtil.formatDouble_two(Integer.valueOf(actual_price)/100);
                boolean re = topupService.updateUserTopupEntityData(orderid,bill_no,account_name, a_p,userId,true);
                LOGGER.info(userId+"收款"+re);
            }
            LOGGER.info("key= "+key +"; newSign= " +newSign );

        } catch (Exception e) {
            //todo 记录
            e.printStackTrace();
        }
        return SUCCESS;
    }


}
