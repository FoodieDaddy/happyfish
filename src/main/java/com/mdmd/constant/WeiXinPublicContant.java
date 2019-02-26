package com.mdmd.constant;

public class WeiXinPublicContant {
    //微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
    public static final String SIGNATURE = "signature";
    //时间戳
    public static final String TIMESTAMP = "timestamp";
    //随机数
    public static final String NONCE = "nonce";
    //随机字符串
    public static final String ECHOSTR = "echostr";


    public static final String WEIXIN_APPID = "wxf16e677138970e0a";
    public static final String WEIXIN_APPSECRET= "3fe08f5a5244a2e7a30b8c49bb566445";
    public static final String KEY = "a30b8c49bb566445a30b8c49bb566445";//商户号api密码
    public static final String WEIXIN_mch_id = "1495156732";//商户号
    public static final String WEIXIN_COMPAPY_PAY_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    public static final String WEIXIN_PIC_IMG = "/usr/local/wximg";
    public static final String CERT_PATH = "/usr/local/happyfish/apiclient_cert.p12";//支付证书位置

    public static final String WEIXIN_SERVER_IP = "47.101.201.185";
}