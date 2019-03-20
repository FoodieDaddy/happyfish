package com.mdmd.controller;

import com.mdmd.Manager.SysPropManager;
import com.mdmd.service.DataService;
import com.mdmd.service.UserService;
import com.mdmd.util.WeiXinMenuUtil;
import com.mdmd.util.WeiXinMessageUtil;
import com.mdmd.util.WeiXinSignUtil;
import com.mdmd.util.weixinbutton.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mdmd.constant.WeiXinPublicContant.*;
import static com.mdmd.util.WeiXinMessageUtil.*;

@Controller
@RequestMapping("/wx")
public class WeiXinPublicAction {


    @Autowired
    private UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(WeiXinPublicAction.class);

    /**
     * 微信
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/home.do")
    public void wxHome(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String method = request.getMethod();

        if(method.equalsIgnoreCase("get"))
        {
            String signature = request.getParameter(SIGNATURE);
            String timestamp = request.getParameter(TIMESTAMP);
            String nonce = request.getParameter(NONCE);
            String echostr = request.getParameter(ECHOSTR);
            PrintWriter out = response.getWriter();
            //校验后判断是否来自于微信服务器
            if(WeiXinSignUtil.checkSignature(signature, timestamp, nonce))
            {
                out.print(echostr);
            }
            out.close();
        }
        else
        {
            String respMessage = null;//返回的xml数据
            String respContent = "公众号功能开发中。。。";//返回的内容
            String fromUserName = "";
            String toUserName = "";
            String msgType = "";
            try {
                Map<String, String> requestMap = WeiXinMessageUtil.pareXml(request.getInputStream());
                System.out.println("------解析消息-"+ requestMap);
                fromUserName = requestMap.get("FromUserName");
                toUserName = requestMap.get("ToUserName");
                msgType = requestMap.get("MsgType");
                if(RESP_MESSAGE_TYPE_TEXT.equalsIgnoreCase(msgType))
                {
                    String content = requestMap.get("Content");
                    System.out.println(content);
                    if(content.contains("add"))
                    {
                        String[] split = content.split("-");
                        int userId = Integer.valueOf(split[1]);
                        int gold = Integer.valueOf(split[2]);
                        String s = userService.handlerTopup(userId, gold,fromUserName);
                        LOGGER.info(s+"："+fromUserName+"为用户"+userId+"充值"+gold+"。");
                    }else if(content.contains("myopenid")){
                        respContent = fromUserName;
                    }
                }
                //事件消息
                else if(RESP_MESSAGE_TYPE_EVENT.equalsIgnoreCase(msgType))
                {
//                    String event = requestMap.get("Event");
//                    //如果是订阅或者扫码
//                    if(event.equalsIgnoreCase(RESP_EVENT_SCAN) || event.equalsIgnoreCase(RESP_EVENT_SUBSCRIBE))
//                    {
//                        //如果有扫码者的evenkey
//                        if(requestMap.containsKey("EventKey"))
//                        {
//                            // 如果系统中有这个用户 就不创建 并且不修改父类
//                            UserEntity userEntity = userService.getUserWithOpenId_createIs_noSuperUserId(fromUserName, false);
//                            if(userEntity == null)
//                            {
//                                String eventKey = requestMap.get("EventKey");
//                                String scene = eventKey.trim().replace("qrscene_","");
//                                int sceneId = Integer.valueOf(scene);
//                                UserEntity superUser = dataService.getUserWithQrcodeSceneId(sceneId);
//                                //添加用户信息
//                                userService.addUserInfo(fromUserName, superUser.getUserid());
//                                //跳转 todo
//
//                            }
//
//                        }
//                    }
                }
            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
                respContent = "无法解析的内容";
            }

            respMessage = "<xml>"+
                    "<ToUserName><![CDATA["+ fromUserName + "]]></ToUserName>" +
                    "<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>" +
                    "<CreateTime>" + System.currentTimeMillis() + "</CreateTime>" +
                    "<MsgType><![CDATA[text]]></MsgType>"+
                    "<Content><![CDATA[" + respContent + "]]></Content>"+
                    "</xml>";
            System.out.println("----即将返回-----"+respMessage);
            PrintWriter out = response.getWriter();
            out.print(respMessage);
            out.close();
        }

    }

    /**
     * 创建菜单
     * @return
     */
    @RequestMapping("/createMenu.do")
    public String createMenu(){
        AccessToken token = WeiXinMenuUtil.getAccessToken(WEIXIN_APPID,WEIXIN_APPSECRET);
        if(token == null)
        {
            LOGGER.warn("创建菜单获取token失败");
            return null;
        }
        else
        {
            int menu = WeiXinMenuUtil.createMenu(this.init(), token.getToken());
            if(menu == 0)
                return "创建成功";
            else
                return "创建失败";
        }
    }

    /**
     * 菜单构建
     * @return
     */
    private Menu init(){
        Menu menu = new Menu();
        List<Button> buttons = new ArrayList<>();//一级菜单
        ViewButton viewButton = new ViewButton();
        viewButton.setName("管理中心");
        viewButton.setUrl(SysPropManager.getSystemWebUrl()+"/wx/routerToServer.do");
        buttons.add(viewButton);

        ClickButton clickButton = new ClickButton();
        clickButton.setName("后台入口");
        clickButton.setKey("testButton");
        buttons.add(clickButton);

        CommonButton commonButton = new CommonButton();
        commonButton.setName("菜单");
        List<Button> sub_button = new ArrayList<>();

        ClickButton clickButton2 = new ClickButton();
        clickButton2.setName("测试按钮2");
        clickButton2.setKey("testButton2");
        sub_button.add(clickButton2);

        ViewButton viewButton2 = new ViewButton();
        viewButton2.setName("登录测试~");
        viewButton2.setUrl(SysPropManager.getSystemWebUrl()+"/wx/routerToMyPage.do");
        sub_button.add(viewButton2);

        commonButton.setSub_button(sub_button);
        buttons.add(commonButton);
        menu.setButton(buttons);
        return menu;
    }

    /**
     * 通过重定向获取openid (跳转到游戏界面)
     * @param request
     * @param response
     */
    @RequestMapping(value="/routerToMyPage.do")
    public void redirectToMyPage(HttpServletRequest request,HttpServletResponse response){
        StringBuffer sb = new StringBuffer();
        String redirectUrl = SysPropManager.getSystemWebUrl() + "/qx/home.do?sup=";
        try {
            String sup = request.getParameter("token");
            if(sup != null)
            {
                if(!"".equalsIgnoreCase(sup))
                {
                    redirectUrl += sup;
                }
            }
            //公众号中配置的回调域名（网页授权回调域名）
            sb.append(WEIXIN_RETURN_URL);
            sb.append(WEIXIN_APPID);
            String url = "";
            //对重定向url进行编码，官方文档要求
            url = URLEncoder.encode(redirectUrl, "utf-8");
            sb.append("&redirect_uri=").append(url);
            //网页授权的静默授权snsapi_base
            sb.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
            System.out.println("~~~~~redi~~~~~~" + sb.toString() +"~~~~~");

            response.sendRedirect(sb.toString());
        }  catch (Exception e) {
            LOGGER.warn("重定向url编码失败：>>");
            e.printStackTrace();
        }
    }

    /**
     * 通过重定向获取openid (跳转到后台)
     * @param request
     * @param response
     */
    @RequestMapping(value="/routerToServer.do")
    public void redirectToMyPage_server(HttpServletRequest request,HttpServletResponse response){
        StringBuffer sb = new StringBuffer();
        String redirectUrl = SysPropManager.getSystemWebUrl() + "/sysprop/home.do";
        try {
            //公众号中配置的回调域名（网页授权回调域名）
            sb.append(WEIXIN_RETURN_URL);
            sb.append(WEIXIN_APPID);
            String url = "";
            //对重定向url进行编码，官方文档要求
            url = URLEncoder.encode(redirectUrl, "utf-8");
            sb.append("&redirect_uri=").append(url);
            //网页授权的静默授权snsapi_base
            sb.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
            System.out.println("~~~~~redi~~~~~~" + sb.toString() +"~~~~~");

            response.sendRedirect(sb.toString());
        }  catch (Exception e) {
            LOGGER.warn("重定向url编码失败：>>");
            e.printStackTrace();
        }
    }

}





















