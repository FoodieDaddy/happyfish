package com.mdmd.service.impl;

import com.mdmd.constant.WeiXinPublicContant;
import com.mdmd.entity.bean.Transfers;
import com.mdmd.service.DealService;
import com.mdmd.util.WeixinConfigUtils;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.mdmd.constant.WeiXinPublicContant.WEIXIN_SERVER_IP;
import static com.mdmd.util.WeixinConfigUtils.*;

@Component
public class DealServiceImpl implements DealService {

    public void payToUser(String openId, String money,String desc){
        // 构造签名的map
        SortedMap<Object, Object> parameters = new TreeMap<>();
        Transfers transfers = new Transfers();
        // 参数组
        String nonce_str = getRandomString(16);
        //是否校验用户姓名 NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
        String checkName ="NO_CHECK";
        //等待确认转账金额,ip,openid的来源
        Integer amount = Integer.valueOf(money);
        String partner_trade_no = createWXOrderNo();
        // 参数：开始生成第一次签名
        parameters.put("mch_appid",  WeiXinPublicContant.WEIXIN_APPID);
        parameters.put("mchid", WeiXinPublicContant.WEIXIN_mch_id);
        parameters.put("partner_trade_no", partner_trade_no);
        parameters.put("nonce_str", nonce_str);
        parameters.put("openid", openId);
        parameters.put("check_name", checkName);
        parameters.put("amount", amount);
        parameters.put("spbill_create_ip", WEIXIN_SERVER_IP);
        parameters.put("desc", desc);
        String sign = createSign("UTF-8", parameters);
        transfers.setAmount(amount);
        transfers.setCheck_name(checkName);
        transfers.setDesc(desc);
        transfers.setMch_appid(WeiXinPublicContant.WEIXIN_APPID);
        transfers.setMchid(WeiXinPublicContant.WEIXIN_mch_id);
        transfers.setNonce_str(nonce_str);
        transfers.setOpenid(openId);
        transfers.setPartner_trade_no(partner_trade_no);
        transfers.setSign(sign);
        transfers.setSpbill_create_ip(WEIXIN_SERVER_IP);
        String xmlInfo = WeixinConfigUtils.transferXml(transfers).replace("__","_");
        try {
            String post = Post(xmlInfo);
            System.out.println("成功--"+post);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
