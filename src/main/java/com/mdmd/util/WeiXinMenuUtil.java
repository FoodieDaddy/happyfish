package com.mdmd.util;

import com.google.gson.Gson;
import com.mdmd.constant.WeiXinPublicContant;
import com.mdmd.util.weixinbutton.AccessToken;
import com.mdmd.util.weixinbutton.Menu;
import com.mdmd.util.weixinbutton.MyX509TrustManager;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.scene.input.DataFormat;
import net.sf.json.JSON;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.http.ProtocolException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.mdmd.constant.WeiXinPublicContant.WEIXIN_APPID;
import static com.mdmd.constant.WeiXinPublicContant.WEIXIN_APPSECRET;

public class WeiXinMenuUtil {
    //get
    public static final String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //post
    public static final String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";


     //获取微信分享二维码的链接  后面接上的ticket需要进行URLEncoder.encode
    public static final String QRCODE_GET_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

    //只能保存两小时
    private static AccessToken ACCESS_TOKEN = null;
    //存储上次计算时间
    private static String GET_ASSESS_TOKEN_TIME = null;
    /**
     * @param menu
     * @param accessToken
     * @return
     */
    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;

        String url = menu_create_url + accessToken;
        String jsonMenu = JSONObject.fromObject(menu).toString();
        System.out.println("----发送的数据" + jsonMenu);
        //调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            int errcode = jsonObject.getInt("errcode");
            if (0 != errcode) {
                result = errcode;
                System.out.println("创建菜单失败errcode:" + errcode);
            }
        }
        return result;
    }

    /**
     * 获取access_token
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @return
     */
    public static AccessToken getAccessToken(String appid, String appsecret) {
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        if(ACCESS_TOKEN == null)
        {

            System.out.println("---jsonobject----" + jsonObject.toString() + "-----");
            // 如果请求成功
            if (null != jsonObject) {
                try {
                    ACCESS_TOKEN = new AccessToken();
                    ACCESS_TOKEN.setToken(jsonObject.getString("access_token"));
                    ACCESS_TOKEN.setExpiresIn(jsonObject.getInt("expires_in"));
                    GET_ASSESS_TOKEN_TIME = DateFormatUtil.now_yyyyMMddHHmmss();
                } catch (Exception e) {
                    ACCESS_TOKEN = null;
                    // 获取token失败
                    System.out.println("获取token失败");
                }
            }
        }
        else
        {
            String now = DateFormatUtil.now_yyyyMMddHHmmss();
            Date nowDate = null;
            long time = 0;
            try {
                nowDate = DateFormatUtil.parse_yyyyMMddHHmmss(now);
                Date beforeDate = DateFormatUtil.parse_yyyyMMddHHmmss(GET_ASSESS_TOKEN_TIME);
                time = (nowDate.getTime() - beforeDate.getTime()) / 60000;
            } catch (ParseException e) {
                ACCESS_TOKEN = new AccessToken();
                ACCESS_TOKEN.setToken(jsonObject.getString("access_token"));
                ACCESS_TOKEN.setExpiresIn(jsonObject.getInt("expires_in"));
                GET_ASSESS_TOKEN_TIME = DateFormatUtil.now_yyyyMMddHHmmss();
            }
            //消亡时间为2小时 这里每100分钟请求一次
            if(time > 100)
            {
                ACCESS_TOKEN = new AccessToken();
                ACCESS_TOKEN.setToken(jsonObject.getString("access_token"));
                ACCESS_TOKEN.setExpiresIn(jsonObject.getInt("expires_in"));
                GET_ASSESS_TOKEN_TIME = DateFormatUtil.now_yyyyMMddHHmmss();
            }
        }

        return ACCESS_TOKEN;
    }

    /**
     * 获取微信公众号二维码的ticket
     * @param codeType 二维码类型 "1": 临时二维码  "2": 永久二维码
     * @param sceneId 场景值ID
     * @return
     */
    public static String getWXPublicQRCodeTicket(String codeType, Integer sceneId) {
        String wxAccessToken = getAccessToken(WEIXIN_APPID,WEIXIN_APPSECRET).getToken();
        Map<String, Object> map = new HashMap<>();
        if ("1".equals(codeType)) { // 临时二维码
            map.put("expire_seconds", 2592000);
            map.put("action_name", "QR_SCENE");
            Map<String, Object> sceneMap = new HashMap<>();
            Map<String, Object> sceneIdMap = new HashMap<>();
            sceneIdMap.put("scene_id", sceneId);
            sceneMap.put("scene", sceneIdMap);
            map.put("action_info", sceneMap);
        } else if ("2".equals(codeType)) { // 永久二维码
            map.put("action_name", "QR_LIMIT_SCENE");
            Map<String, Object> sceneMap = new HashMap<>();
            Map<String, Object> sceneIdMap = new HashMap<>();
            sceneIdMap.put("scene_id", sceneId);
            sceneMap.put("scene", sceneIdMap);
            map.put("action_info", sceneMap);
        }
        String data = new Gson().toJson(map);
        System.out.println();
        // 得到ticket票据,用于换取二维码图片
        JSONObject jsonObject = httpRequest("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + wxAccessToken, "POST", data);
        String ticket = (String) jsonObject.get("ticket");
        System.out.println("得到jsonobject>>>>>ticket--"+ticket);
        return ticket;
        // WXConstants.QRCODE_SAVE_URL: 填写存放图片的路径
//        httpsRequestPicture("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(ticket),
//                "GET", null, WeiXinPublicContant.WEIXIN_PIC_IMG, fileName, "png");
    }

    /**
     * 发送https请求，返回二维码图片
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param data 提交的数据
     * @param savePath 图片保存路径
     * @param fileName 图片名称
     * @param fileType 图片类型
     */
    public static void httpsRequestPicture(String requestUrl, String requestMethod, String data, String savePath, String fileName, String fileType) {
        InputStream inputStream = null;
        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            //设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.connect();
            //当data不为null时向输出流写数据
            if (null != data) {
                //getOutputStream方法隐藏了connect()方法
                OutputStream outputStream = conn.getOutputStream();
                //注意编码格式
                outputStream.write(data.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            inputStream = conn.getInputStream();
            System.out.println("开始生成微信二维码...");
            inputStreamToMedia(inputStream, savePath, fileName, fileType);
            System.out.println("微信二维码生成成功!!!");
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("发送https请求失败，失败");
        }finally {
            //释放资源
            try {
                if(null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }
    /**
     * 将输入流转换为图片
     * @param input 输入流
     * @param savePath 图片需要保存的路径
     * @param fileType 图片类型
     */
    public static void inputStreamToMedia(InputStream input, String savePath, String fileName, String fileType) throws Exception {
        String filePath = savePath + "/" + fileName + "." + fileType;
        File file = new File(filePath);
        FileOutputStream outputStream = new FileOutputStream(file);
        int length;
        byte[] data = new byte[1024];
        while ((length = input.read(data)) != -1) {
            outputStream.write(data, 0, length);
        }
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 描述:  发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null,tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
            httpsURLConnection.setSSLSocketFactory(ssf);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestProperty("Content-Type","application/json;charset=utf-8");
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setRequestMethod(requestMethod);
            if("GET".equalsIgnoreCase(requestMethod))
                httpsURLConnection.connect();
            //当有数据提交时
            if(null != outputStr)
            {
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            InputStream inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine())!= null){
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream = null;
            httpsURLConnection.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());

        } catch (ConnectException e) {
            e.printStackTrace();
            System.out.println("微信连接超时");
        } catch (Exception e){
            System.out.println("<<<https request error>>>");
        }
        return jsonObject;
    }


}
