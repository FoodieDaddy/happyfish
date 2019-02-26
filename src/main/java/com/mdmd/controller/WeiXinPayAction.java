package com.mdmd.controller;

import com.mdmd.constant.WeiXinPublicContant;
import com.mdmd.entity.bean.Transfers;
import com.mdmd.util.WeiXinSignUtil;
import com.mdmd.util.WeixinConfigUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mdmd.constant.WeiXinPublicContant.WEIXIN_SERVER_IP;
import static com.mdmd.util.WeixinConfigUtils.*;


@Controller
@RequestMapping("/wx")
public class WeiXinPayAction {
    private static final Logger LOGGER = LogManager.getLogger(WeiXinPayAction.class);

    /**
     * 微信提现（企业付款）
     */
    @RequestMapping(value = "/pay.do")
    public String weixinWithdraw(HttpServletRequest request){
        System.out.println("-----------进来了---------------\n---------------------------------------\n");
//        String openId = request.getParameter("openid");
        String openId = "oA7Y0v7MorciYdD8qU6_j1ZbuPQM";
        String ip=WEIXIN_SERVER_IP;
//        String money = request.getParameter("money");
        String money = "100";
        // 构造签名的map
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        Transfers transfers = new Transfers();
        if (money != null && ip!=null && openId != null) {
            // 参数组
            String appid = WeiXinPublicContant.WEIXIN_APPID;
            String mch_id = WeiXinPublicContant.WEIXIN_mch_id;
            String nonce_str = getRandomString(16);
            //是否校验用户姓名 NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
            String checkName ="NO_CHECK";
            //等待确认转账金额,ip,openid的来源
            Integer amount = Integer.valueOf(money);
            String spbill_create_ip = ip;
            String partner_trade_no = "wx";
            Random random = new Random();
            for (int i = 0; i < 22; i++) {
                partner_trade_no += random.nextInt(9);
            }
            //描述
            String desc = "hp测试付款";
            // 参数：开始生成第一次签名
            parameters.put("mch_appid", appid);
            parameters.put("mchid", mch_id);
            parameters.put("partner_trade_no", partner_trade_no);
            parameters.put("nonce_str", nonce_str);
            parameters.put("openid", openId);
            parameters.put("check_name", checkName);
            parameters.put("amount", amount);
            parameters.put("spbill_create_ip", spbill_create_ip);
            parameters.put("desc", desc);
            System.out.println("signpar:"+parameters);
            String sign = createSign("UTF-8", parameters);
            transfers.setAmount(amount);
            transfers.setCheck_name(checkName);
            transfers.setDesc(desc);
            transfers.setMch_appid(appid);
            transfers.setMchid(mch_id);
            transfers.setNonce_str(nonce_str);
            transfers.setOpenid(openId);
            transfers.setPartner_trade_no(partner_trade_no);
            transfers.setSign(sign);
            transfers.setSpbill_create_ip(spbill_create_ip);
            String xmlInfo = WeixinConfigUtils.transferXml(transfers).replace("__","_");
            System.out.println("xmlinfo: " + xmlInfo);
            try {

                String post = Post(xmlInfo);
                System.out.println("成功--"+post);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("失败");
        }
        return null;
    }


}
