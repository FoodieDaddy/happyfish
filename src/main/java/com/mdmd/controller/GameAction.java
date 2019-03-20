package com.mdmd.controller;


import com.mdmd.entity.JO.GameResultJO;
import com.mdmd.service.GameRuleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.mdmd.constant.ActionConstant.*;


@Controller
@RequestMapping("/gm")
public class GameAction {

    @Autowired
    private GameRuleService gameRuleService;


    private static final Logger LOGGER = LogManager.getLogger(GameAction.class);


    /**
     * 获取捕鱼结果
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/catch_fish.do")
    @ResponseBody
    public Map<String,Object> catchFish(HttpServletRequest request, HttpSession session,HttpServletResponse response,String guess_num, String catch_num){
        Map<String, Object> result = new HashMap<>();

        try {
            if(guess_num == null || catch_num == null)
            {
                result.put(SUCCESS,false);
                return result;
            }
            int userId = (int)session.getAttribute(SESSION_USERID);
            int g = 0,c=0;
            boolean flag = true;
            try {
                 g = Integer.valueOf(guess_num);
                 c = Integer.valueOf(catch_num);
            } catch (NumberFormatException e) {
                LOGGER.warn("来自"+userId+"的非法数据guess_num="+guess_num+",catch_num="+catch_num+"!!!");
                result.put(SUCCESS,false);
                result.put(MSG,"数据异常");
                flag = false;
            }
            if(g<=0 || c<0)
            {
                LOGGER.warn("来自"+userId+"的非法数据guess_num="+guess_num+",catch_num="+catch_num+"!!!");
                result.put(SUCCESS,false);
                result.put(MSG,"数据异常");
                flag = false;
            }
            GameResultJO fishResult = null;
            if(flag)
            {
                 fishResult = gameRuleService.getFishResult(g, c, userId);
            }
            if(fishResult != null)
            {
                if(fishResult.isSuccess())
                {
                    result.put(SUCCESS,Boolean.TRUE);
                    result.put("data",fishResult);
                }
                else
                {
                    result.put(SUCCESS,false);
                    result.put(MSG,fishResult.getMsg());
                }
            }

        } catch (Exception e) {
            result.put(SUCCESS,false);
            result.put(MSG,"游戏异常");
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    /*
     * 夺宝
     */
    @RequestMapping(value = "/seize_treasure.do")
    @ResponseBody
    public Map<String,Object> seizeTreasure(HttpServletRequest request, HttpSession session,HttpServletResponse response,String treasure_type, String treasure_num) {
        Map<String, Object> result = new HashMap<>();
        int userId = 0,type = 0,num =0;
        try {
            userId = (int)session.getAttribute(SESSION_USERID);
            String treasureType = treasure_type.trim();
            String treasureNum = treasure_num.trim();
            type = Integer.valueOf(treasureType);
            num = Integer.valueOf(treasureNum);
            if(!(type > 9 && type < 19 && num > 0 && num < 201))
            {
                result.put(SUCCESS,false);
                result.put(MSG,"非法数据");
                LOGGER.error("《夺宝》来自用户:"+userId+"的非法数据type="+type+",num="+num+"。");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn(e.getMessage());
            result.put(SUCCESS,false);
            result.put(MSG,"意外的错误");
            return result;
        }

        String msg = null;
        try {
            msg = gameRuleService.getTreasureResult_1_deductMoney(num, userId);
        } catch (Exception e) {
            msg =e.getMessage();
            LOGGER.error("用户"+ userId +"夺宝时扣款时错误");
        }
        if(msg != null)
        {
            result.put(SUCCESS,false);
            result.put(MSG,msg);
            return result;
        }

        GameResultJO treasureResult = null;
        try {
            treasureResult = gameRuleService.getTreasureResult(type, num, userId);
            msg = treasureResult.getMsg();
        } catch (Exception e) {
            msg = e.getMessage();
        }

        if(msg == null)
        {
            result.put("data",treasureResult);
            result.put(SUCCESS,true);
        }
        else
        {
            result.put(SUCCESS,false);
            result.put(MSG,treasureResult.getMsg());
            try {
                gameRuleService.treasureCost_back(num,userId);
            } catch (Exception e) {
                result.put(SUCCESS,false);
                result.put(MSG,e.getMessage());
                LOGGER.error("用户"+ userId +"夺宝失败还款时错误");
            }
        }

        return result;
    }

}
















