package com.mdmd.entity.bean;

import java.io.Serializable;

/**
 * 微信企业支付查询时 使用的参数对象
 */
public class SearchCompanyPayBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String appid;
    private String mch_id;
    private String nonce_str;
    private String partner_trade_no;
    private String sign;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
