package com.mdmd.controller;


import com.mdmd.entity.GoldEntity;
import com.mdmd.entity.JO.RankingListJO;
import com.mdmd.entity.JO.UserChildsDataJO;
import com.mdmd.entity.UserEntity;
import com.mdmd.service.DataService;
import com.mdmd.service.UserService;
import com.mdmd.util.QrcodeUtil;
import com.mdmd.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mdmd.constant.ActionConstant.*;
import static com.mdmd.constant.SystemConstant.QRCODE_PREFIX;
import static com.mdmd.util.WeiXinSignUtil.getOpenId;

@Controller
@RequestMapping("/qx")
public class DataAction {

    @Autowired
    private UserService userService;

    @Autowired
    private DataService dataService;
    static final Logger LOGGER = LogManager.getLogger(DataAction.class);

    /**
     * 通过重定向获取openid
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
            String sup = request.getParameter("sup");

            LOGGER.info("home获取到" + code + ";" + state + ";" + sup + "。");

            UserEntity userEntity = null;
            if (test != null) {
                if (!test.equals("")) {
                    userEntity = userService.getUserWithOpenId(test);
                    if (userEntity == null)
                        userEntity = userService.addUserInfo(test);
                }
            }
            if (code != null) {
                String openId = getOpenId(code);
                //如果openId不为空，从用户库中查找该用户
                if (openId != null) {
                    userEntity = userService.getUserWithOpenId(openId);
                    LOGGER.info("openId:" + openId);
                    if (userEntity == null) {
                        LOGGER.info("sup" + sup);
                        if (sup != null) {
                            sup = sup.trim();
                            if (!"".equals(sup)) {
                                LOGGER.info("校验");
                                if (sup.matches(REGEX_NUMBER))
                                    userEntity = userService.addUserInfo(openId, Integer.valueOf(sup));

                            } else {
                                userEntity = userService.addUserInfo(openId);
                            }
                        } else {
                            userEntity = userService.addUserInfo(openId);
                        }
                    }


                }
            }

            if (userEntity != null) {
                int userid = userEntity.getUserid();
                if (userEntity.getLoginBan() == 0) {
                    //将userId加密传输到前端
                    String encrypt = RSAUtil.encrypt(userid + "");
                    response.sendRedirect("../stc/index.html?token=" + encrypt);
                } else {
                    response.sendRedirect("http://www.baidu,com");
                }
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
        //todo 跳转到什么网页
    }


    /**
     * 获取首页的数据
     *
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "/getHomeData.do")
    @ResponseBody
    public Map<String, Object> getHomeData(HttpServletRequest request, HttpServletResponse response, HttpSession session, String u) {
        Map<String, Object> result = new HashMap<>();
        try {
            int userId = (int) session.getAttribute(SESSION_USERID);
            System.out.println("获取user--" + userId);
            UserEntity userEntity = userService.getUserWithUserId_self_or_cascade(userId, true);
            LOGGER.info("------user是:" + userId);
            result.put("userId", userId);
            result.put("gold", userEntity.getGold());
            result.put("commission", userEntity.getCommission());//todo 未计算
            result.put(SUCCESS, Boolean.TRUE);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            result.put(SUCCESS, false);
            result.put(MSG,"获取个人信息失败");
        }
        return result;
    }

    /**
     * 获取二维码tickert
     *
     * @param request
     * @param session
     * @param response
     * @return
     */
    @RequestMapping(value = "/refreshQrcode.do")
    @ResponseBody
    public Map<String, Object> catchFish(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            int userId = (int) session.getAttribute(SESSION_USERID);
            QrcodeUtil.createQrcode(QRCODE_PREFIX + userId, userId + "");
            result.put(SUCCESS, Boolean.TRUE);
        } catch (Exception e) {
            result.put(SUCCESS, false);
            result.put(MSG, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

//    /**
//     * 获取微信二维码tickert
//     * @param request
//     * @param session
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "/getQRCode.do")
//    @ResponseBody
//    public Map<String,Object> getQrCODE(HttpServletRequest request, HttpSession session,HttpServletResponse response){
//        Map<String, Object> result = new HashMap<>();
//        try {
//            int userId = (int) session.getAttribute("userId");
//            String ticket = dataService.getQRCodeTicketWithUserId(userId);
//            result.put("data",QRCODE_GET_URL+ URLEncoder.encode(ticket));
//            result.put(SUCCESS,Boolean.TRUE);
//        } catch (Exception e) {
//            result.put(SUCCESS,false);
//            result.put(MSG,e.getMessage());
//            e.printStackTrace();
//        }
//        return result;
//    }


    /**
     * 获取公钥
     *
     * @param request
     * @param session
     * @param response
     * @return
     */
    @RequestMapping(value = "/getPub.do")
    @ResponseBody
    public Map<String, Object> getPublicKey(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();

        try {
            RSAPublicKey publicKey1 = RSAUtil.getPublicKey();
            String key = Base64.encodeBase64String(publicKey1.getEncoded());
//            byte[] keyBytes;
//            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            result.put("key", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put(SUCCESS, Boolean.TRUE);
        return result;
    }


    /**
     * 获取游戏记录等数据信息
     *
     * @param request
     * @param session
     * @param response
     * @return
     */
    @RequestMapping(value = "/listData.do")
    @ResponseBody
    public Map<String, Object> listGameData(String type, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();

        try {
            int t = Integer.valueOf(type);
            int userId = (int) session.getAttribute("userId");
            if (t > 0 && t < 5) {
                List list = dataService.listDatas(t, userId);
                result.put(SUCCESS, true);
                result.put("list", list);
            } else {
                result.put(SUCCESS, false);
                result.put(MSG, "非法数据");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result.put(SUCCESS, false);
            result.put(MSG, "意外错误");
        }
        return result;
    }

    /**
     * 获取所有下级人数及佣金总数
     * @param request
     * @param session
     * @param response
     * @return
     */
    @RequestMapping(value = "/listUnder.do")
    @ResponseBody
    public Map<String, Object> getChildsData(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            int userId = (int) session.getAttribute("userId");
            List<UserChildsDataJO> datas = userService.listAllLevelChildsNumberAndMoneySum(userId);
            result.put(SUCCESS, true);
            result.put("list",datas);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
        }
        return result;
    }

    /**
     * 获取昨日佣金榜
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/listRanking.do")
    @ResponseBody
    public Map<String, Object> listRanking(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<RankingListJO> rankingListJOS = dataService.calcYesterdayCommissionRankingList();
            result.put(SUCCESS, true);
            result.put("list",rankingListJOS);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
        }
        return result;
    }

    /**
     * 获取提现时的某些信息
     * @param request
     * @param session
     * @param response
     * @return
     */
    @RequestMapping(value = "/takeoutInfo.do")
    @ResponseBody
    public Map<String, Object> getTakeoutInfo(HttpServletRequest request,HttpSession session, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            int userId = (int) session.getAttribute("userId");
            UserEntity user = userService.getUserWithUserId_self_or_cascade(userId, false);
            double gold = user.getGold();
            double commission = user.getCommission();
            GoldEntity goldEntity = user.singleGoldEntity();
            double todayWater = goldEntity.todayWater();
            result.put(SUCCESS, true);
            result.put("gold",gold);
            result.put("commission",commission);
            result.put("todayWater",todayWater);
            result.put("userId",userId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(SUCCESS,false);
            result.put(MSG,e.getMessage());
        }
        return result;
    }
}
