package com.mdmd.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import static com.mdmd.constant.WeiXinPublicContant.WEIXIN_APPID;
import static com.mdmd.constant.WeiXinPublicContant.WEIXIN_APPSECRET;

public class WeiXinSignUtil {
    private static final String token = "happyfish";

    public static boolean checkSignature(String signature,String timestamp,String nonce) {
        String[] arr = {token, timestamp, nonce};
        Arrays.sort(arr);
        StringBuffer sb = new StringBuffer();
        for (String s : arr) {
            sb.append(s);
        }
        //SHA1加密
        String temp = getSha1(sb.toString());
        return temp.equals(signature);
    }

    public static String getSha1(String str){
        if(str == null || str.length() == 0)
            return null;
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes("UTF-8"));

            byte[] md = messageDigest.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                buf[k++] = hexDigits[b >>> 4 & 0xf];
                buf[k++] = hexDigits[b & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }




    /**
     * 获取openid
     * @param code
     * @return
     */
    public static String getOpenId(String code) {

        //封装获取openId的微信API
        StringBuffer url = new StringBuffer();
        url.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                .append(WEIXIN_APPID)
                .append("&secret=")
                .append(WEIXIN_APPSECRET)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        String urlContent = getURLContent(url.toString());
        Map<String,String> o = new Gson().fromJson(urlContent,new TypeToken<Map<String,String>>(){}.getType());
        String openid = o.get("openid");
        if(openid != null)
            return openid;
        return null;
    }


    /**
     * 请求url获取数据
     * @param urlStr
     * @return
     */
    private static String getURLContent(String urlStr) {
        //请求的url
        URL url = null;
        //请求的输入流
        BufferedReader in = null;
        //输入流的缓冲
        StringBuffer sb = new StringBuffer();
        try{
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8") );
            String str = null;
            //一行一行进行读入
            while((str = in.readLine()) != null) {
                sb.append( str );
            }
        } catch (Exception ex) {

        } finally{
            try{
                if(in!=null) {
                    in.close(); //关闭流
                }
            }catch(IOException ex) {

            }
        }
        String result =sb.toString();
        return result;
    }
}