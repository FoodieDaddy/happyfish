package com.mdmd.service.impl;

import com.mdmd.constant.WeiXinPublicContant;
import com.mdmd.dao.CommonDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.UserEntity;
import com.mdmd.entity.bean.CompanyPayBean;
import com.mdmd.service.DealService;
import com.mdmd.util.WeiXinMessageUtil;
import com.mdmd.util.WeixinConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.*;

import static com.mdmd.constant.ActionConstant.MSG;
import static com.mdmd.constant.GameConstant.TAKEOUT_TAK;
import static com.mdmd.constant.WeiXinPublicContant.*;
import static com.mdmd.util.WeixinConfigUtils.*;

@Component
public class DealServiceImpl implements DealService {
    private static final Logger LOGGER = LogManager.getLogger(DealServiceImpl.class);

    private static final String  WEIXIN_STATUS_SUCCESS = "SUCCESS";//成功
    private static final String  WEIXIN_STATUS_FAIL = "FAIL";//失败
    private static final String  WEIXIN_STATUS_PROCESSING = "PROCESSING";//处理中

    private static final String  MSG_SYS_ERROR = "支付失败，意外错误，请联系客服。";//一般为未扣款的错误
    private static final String  MSG_UNKNOW_ERROR = "支付失败，未知错误,请联系客服。";//可能出现重复扣款的错误

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommonDao commonDao;
    public Map<String, String> companyPayToUser(int userId, String openId, int money, String desc) {
        return this.companyPayToUser(userId,openId,money,desc,null,true);
    }

    public Map<String, String> companyPayToUser(int userId, String openId, int money, String desc, String tradeOrderId, boolean doAgain) {

        if(money < 100 )
        {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(MSG,"提现金额必须大于1元");
            return hashMap;
        }
        // 构造签名的map
        SortedMap<Object, Object> parameters = new TreeMap<>();
        CompanyPayBean companyPayBean = new CompanyPayBean();
        // 参数组
        String nonce_str = getRandomString(16);
        //是否校验用户姓名 NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
        String checkName ="NO_CHECK";

        String partner_trade_no = tradeOrderId == null ? createWXOrderNo("x"+userId+"x") : tradeOrderId;
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
        companyPayBean.setAmount(money);
        companyPayBean.setCheck_name(checkName);
        companyPayBean.setDesc(desc);
        companyPayBean.setMch_appid(WeiXinPublicContant.WEIXIN_APPID);
        companyPayBean.setMchid(WeiXinPublicContant.WEIXIN_mch_id);
        companyPayBean.setNonce_str(nonce_str);
        companyPayBean.setOpenid(openId);
        companyPayBean.setPartner_trade_no(partner_trade_no);
        companyPayBean.setSign(sign);
        companyPayBean.setSpbill_create_ip(WEIXIN_SERVER_IP);
        String xmlInfo = WeixinConfigUtils.transferXml(companyPayBean);
        try {
            String post = weiXinPost(xmlInfo,WEIXIN_COMPAPY_PAY_URL);
            Map<String, String> record = WeiXinMessageUtil.pareXml(new ByteArrayInputStream(post.getBytes("utf-8")));
            //判断通讯是否成功
            if(record.get("return_code").contains(WEIXIN_STATUS_SUCCESS))
            {
                //判断交易是否成功
                if(record.get("result_code").contains(WEIXIN_STATUS_SUCCESS))
                {
                    String payment_no = record.get("payment_no");//交易单号
                    record.put("payment_no",payment_no);
                }
                else
                {
                    String err_code = record.get("err_code");//错误码
                    if(err_code.equals("SEND_FAILED"))//付款错误
                    {
                        Map<String, String> map = this.searchCompanyPayStatusAndOrder(partner_trade_no);
                        String status = map.get("status");
                        if(status.equals(WEIXIN_STATUS_SUCCESS))
                        {
                            String orderNum = map.get("orderNum");
                            record.put("payment_no",orderNum);
                        }
                        else
                        {
                            record.put(MSG,MSG_UNKNOW_ERROR);
                        }
                    }
                    else if(err_code.equals("NOTENOUGH"))//商户号余额不足
                    {
                        //todo 提醒管理者
                        record.put(MSG,MSG_SYS_ERROR);
                    }
                    else if(err_code.equals("AMOUNT_LIMIT"))//金额超限
                    {
                        record.put(MSG, "交易金额过高或过低");
                    }
                    else if(err_code.equals("SYSTEMERROR"))//系统繁忙，请稍后再试 需要核对订单号是否完成 以免重复付款
                    {
                        //查询该订单号
                        Map<String, String> map = this.searchCompanyPayStatusAndOrder(partner_trade_no);
                        String status = map.get("status");
                        if(status.equals(WEIXIN_STATUS_SUCCESS))
                        {
                            String orderNum = map.get("orderNum");
                            record.put("payment_no",orderNum);
                        }
                        else if(status.equals(WEIXIN_STATUS_PROCESSING))
                        {
                            //todo 异常收录 如果一直都在处理中 应由未处理队列来查找并轮询查询
                            record.put(MSG,MSG_UNKNOW_ERROR);
                        }
                        else
                        {
                            //使用原单号重新付款
                            if(doAgain)
                            {
                                return this.companyPayToUser(userId,openId,money,desc,partner_trade_no,false);
                            }
                            record.put(MSG,MSG_UNKNOW_ERROR);
                        }
                    }
                    else if(err_code.equals("FREQ_LIMIT"))//超出频率限制 需使用原订单号付款
                    {
                        //一直支付
                        return this.companyPayToUser(userId,openId,money,desc,partner_trade_no,true);
                    }

                    else if(err_code.equals("MONEY_LIMIT"))//已达到付款给此用户上限
                    {
                        record.put(MSG,"今日收款已达微信规定上限");
                    }
                    else if(err_code.equals("V2_ACCOUNT_SIMPLE_BAN"))//无法给非实名用户付款
                    {
                        record.put(MSG,"请完成微信实名认证");
                    }
                    else if(err_code.equals("SENDNUM_LIMIT"))//该用户今日付款次数超过限制
                    {
                        record.put(MSG,"您今日砖石夺宝游戏次数与提现次数不足，请明日再来");
                    }
                    else  if(err_code.equals("NO_AUTH"))//没有该接口权限
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                    else if(err_code.equals("PARAM_ERROR"))//参数错误
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                    else if(err_code.equals("OPENID_ERROR"))//Openid格式错误或者不属于商家公众账号
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                    else if(err_code.equals("NAME_MISMATCH"))//姓名校验错误
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                    else if(err_code.equals("SIGN_ERROR"))//签名错误
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                    else if(err_code.equals("XML_ERROR"))//Post内容出错
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                    else if(err_code.equals("FATAL_ERROR"))//两次请求参数不一致
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                    else if(err_code.equals("CA_ERROR"))//商户API证书校验出错
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }

                    else if(err_code.equals("PARAM_IS_NOT_UTF8"))//请求参数中包含非utf8编码字符
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                    else if(err_code.equals("RECV_ACCOUNT_NOT_ALLOWED"))//收款账户不在收款账户列表
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                    else if(err_code.equals("PAY_CHANNEL_NOT_ALLOWED"))//本商户号未配置API发起能力
                    {
                        //TODO 通知管理者
                        record.put(MSG,MSG_SYS_ERROR);
                        LOGGER.error("支付错误："+ record.get("err_code_des") +"；错误代码："+ err_code +"\n 发生于用户"+userId+",xmlInfo:"+xmlInfo);
                    }
                }
            }
            else
            {
                //TODO 通知管理者
                record.put(MSG,"支付失败。");
                LOGGER.error("支付时与微信通信未成功,来自用户"+userId);
            }

            return record;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("支付给用户" + userId + ",(openId:"+ openId+")时失败！"+e.getMessage());
            return null;
        }

    }

    public Map<String,String> searchCompanyPay(String tradeOrderId){
        // 构造签名的map
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String nonce_str = getRandomString(16);
        parameters.put("appid",  WeiXinPublicContant.WEIXIN_APPID);
        parameters.put("mch_id", WeiXinPublicContant.WEIXIN_mch_id);
        parameters.put("partner_trade_no", tradeOrderId);
        parameters.put("nonce_str", nonce_str);
        String sign = createSign("UTF-8", parameters);
        System.out.println("sign:"+sign);
        parameters.put("appid",  WeiXinPublicContant.WEIXIN_APPID);
        parameters.put("mch_id", WeiXinPublicContant.WEIXIN_mch_id);
        parameters.put("partner_trade_no", tradeOrderId);
        parameters.put("nonce_str", nonce_str);
        parameters.put("sign",sign);
        String xmlInfo = transferXml(parameters);
        System.out.println(xmlInfo);
        String post = null;
        try {
            post = weiXinPost(xmlInfo,WEIXIN_SEARCH_PAY_URL);
            Map<String, String> record = WeiXinMessageUtil.pareXml(new ByteArrayInputStream(post.getBytes("utf-8")));
            return record;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String,String> searchCompanyPayStatusAndOrder(String tradeOrderId) {
        Map<String, String> record = this.searchCompanyPay(tradeOrderId);
        Map<String, String> map = new HashMap<>();
        if(record == null)
        {
            map.put("status",WEIXIN_STATUS_FAIL);
        }
        String return_code = record.get("return_code");//返回状态码
        if(return_code.equals(WEIXIN_STATUS_SUCCESS))
        {
            map.put("status",record.get("status"));
            map.put("orderNum", record.get("detail_id"));
        }
        else
        {
            map.put("status",WEIXIN_STATUS_FAIL);
        }
        return map;
    }

    public String takeoutForUser(int userId, int num, int type) {
        UserEntity user = (UserEntity) commonDao.getEntity(UserEntity.class, userId);
        //不为0则无法提现
        if(user.getTakeoutBan() != 0)
        {
            return "无法提现，请联系客服";
        }
        //查看提现次数是否有剩余
        int takeOutTime = user.takeOutTime();
        //提现手续费
        double tax = 0;
        if(takeOutTime > 0)
        {
            tax =  num*TAKEOUT_TAK/100.0;
        }
        //金币提现
        if(type == 0)
        {
            double gold = user.getGold();
            double minGold = num + tax;
            if(gold < minGold)
            {
                return "金币不足";
            }
        }
        else
        {

        }

        return null;
    }
}
