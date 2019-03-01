package com.mdmd.service.impl;

import com.mdmd.constant.WeiXinPublicContant;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.UserEntity;
import com.mdmd.entity.bean.Transfers;
import com.mdmd.service.DealService;
import com.mdmd.service.UserService;
import com.mdmd.util.WeiXinMessageUtil;
import com.mdmd.util.WeixinConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.mdmd.constant.WeiXinPublicContant.WEIXIN_SERVER_IP;
import static com.mdmd.util.WeixinConfigUtils.*;

@Component
public class DealServiceImpl implements DealService {
    private static final Logger LOGGER = LogManager.getLogger(DealServiceImpl.class);
    @Autowired
    private UserDao userDao;

    public Map<String,String> payToUser(int userId,String openId, int money, String desc){
        // 构造签名的map
        SortedMap<Object, Object> parameters = new TreeMap<>();
        Transfers transfers = new Transfers();
        // 参数组
        String nonce_str = getRandomString(16);
        //是否校验用户姓名 NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
        String checkName ="NO_CHECK";
        //等待确认转账金额,ip,openid的来源
        String partner_trade_no = createWXOrderNo("x"+userId+"x");
        // 参数：开始生成第一次签名
        parameters.put("mch_appid",  WeiXinPublicContant.WEIXIN_APPID);
        parameters.put("mchid", WeiXinPublicContant.WEIXIN_mch_id);
        parameters.put("partner_trade_no", partner_trade_no);
        parameters.put("nonce_str", nonce_str);
        parameters.put("openid", openId);
        parameters.put("check_name", checkName);
        parameters.put("amount", money);
        parameters.put("spbill_create_ip", WEIXIN_SERVER_IP);
        parameters.put("desc", desc);
        String sign = createSign("UTF-8", parameters);
        transfers.setAmount(money);
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
            Map<String, String> record = WeiXinMessageUtil.pareXml(new ByteArrayInputStream(post.getBytes("utf-8")));
            //判断通讯是否成功
            if(record.get("return_code").contains("SUCCESS"))
            {
                //判断交易是否成功
                if(record.get("result_code").contains("SUCCESS"))
                {
                    String payment_no = record.get("payment_no");//交易单号
                }
                else
                {

                }
            }
            else
            {

            }

            return record;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("支付给用户" + userId + ",(openId:"+ openId+")时失败");
            return null;
        }

    }
}
