package com.mdmd.controller;

import com.mdmd.service.TakeoutService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static com.mdmd.constant.ActionConstant.*;


@Controller
@RequestMapping("/wx")
public class WeiXinPayAction {
    private static final Logger LOGGER = LogManager.getLogger(WeiXinPayAction.class);
    @Autowired
    private TakeoutService dealService;

    /**
     * 提现
     * @param request
     * @param type 提现类型 0为金币 1为佣金
     * @param num 最少为1（元） 最大3000
     * @return
     */
    @RequestMapping(value = "/takeout.do",method = RequestMethod.POST)
    @ResponseBody
    public Map takeout(HttpServletRequest request, HttpSession session,String type, String num) {
        Map<String, Object> result = new HashMap<>();
        int type_,num_;
        try {
            type_ = Integer.valueOf(type.trim());
            num_ = Integer.valueOf(num);
        } catch (NumberFormatException e) {
            result.put(SUCCESS,false);
            result.put(MSG,"提现失败");
            return result;
        }
        if(type_ < 0 || type_ > 1 || num_< 1 || num_ > 3000)
        {
            result.put(SUCCESS,false);
            result.put(MSG,"参数错误");
            return result;
        }
        int userId = (int)session.getAttribute(SESSION_USERID);
        try {
            String msg = dealService.takeoutForUser(userId, num_, type_);
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
            LOGGER.error("用户："+userId +"提现时错误,"+e.getMessage());
            e.printStackTrace();
            result.put(SUCCESS,false);
            result.put(MSG,"系统错误");
        }

        return result;
    }

    /**
     * 微信提现（企业付款）
     */
    @RequestMapping(value = "/pay.do")
    public String weixinWithdraw(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "/search.do")
    @ResponseBody
    public Map paySearch(HttpServletRequest request) {
        return null;
    }

}
