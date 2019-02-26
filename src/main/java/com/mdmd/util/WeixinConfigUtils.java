package com.mdmd.util;

import com.mdmd.constant.WeiXinPublicContant;
import com.mdmd.entity.bean.Transfers;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mdmd.constant.WeiXinPublicContant.WEIXIN_COMPAPY_PAY_URL;
import static com.mdmd.util.WeiXinMessageUtil.xmStream;

public class WeixinConfigUtils {

    private static final Logger LOGGER = LogManager.getLogger(WeixinConfigUtils.class);
    private static String allChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    public static String getRandomString(int length) { //length表示生成字符串的长度
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int number = 0;
        for (int i = 0; i < length; i++) {
            number = random.nextInt(allChar.length());
            sb.append(allChar.charAt(number));
        }
        return sb.toString();
    }
    public static String createWXOrderNo(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + getRandomString(10);
    }

     /** 构造企业付款xml参数
     * @return
      */
    public static String transferXml(Transfers transfers) {
        xmStream.autodetectAnnotations(true);
        xmStream.alias("xml", Transfers.class);
        return xmStream.toXML(transfers);

    }
    public static String transferXml(SortedMap m){
        xmStream.autodetectAnnotations(true);
        xmStream.alias("xml", SortedMap.class);
        return xmStream.toXML(m);
    }

    /**
     * 给微信发送post请求
     *
     */
    public static String Post(String reqStr) throws Exception {
        CloseableHttpClient httpclient = certificateValidation(WeiXinPublicContant.WEIXIN_mch_id);
        //加载证书
        HttpPost httppost = new HttpPost(WEIXIN_COMPAPY_PAY_URL);
        StringEntity myEntity = new StringEntity(reqStr, "UTF-8");
        httppost.setEntity(myEntity);

        CloseableHttpResponse response = httpclient.execute(httppost);
        System.out.println(response.getStatusLine());

        HttpEntity resEntity = response.getEntity();
        InputStreamReader reader = new InputStreamReader(
                resEntity.getContent(), "UTF-8");
        char[] buff = new char[1024];
        int length = 0;
        StringBuffer strhuxml = new StringBuffer();
        while ((length = reader.read(buff)) != -1)
        {
            strhuxml.append(new String(buff, 0, length));
        }
        httpclient.getConnectionManager().shutdown();
        reader.close();
        return strhuxml.toString();
    }

    public static CloseableHttpClient certificateValidation(String mchid) throws Exception
    {
        // 指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 证书地址
        FileInputStream instream = new FileInputStream(new File(WeiXinPublicContant.CERT_PATH));
        try
        {
            keyStore.load(instream, mchid.toCharArray());
        }
        finally
        {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, mchid.toCharArray()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf).build();
        return httpclient;
    }
    /**
     * 解析申请退款之后微信返回的值并进行存库操作
     * @throws IOException
     * @throws JDOMException
     */
    public static Map<String, String> parseRefundXml(String refundXml) throws JDOMException, IOException {
//        ParseXMLUtils.jdomParseXml(refundXml);
        StringReader read = new StringReader(refundXml);
        // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        // 创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        // 通过输入源构造一个Document
        org.jdom2.Document doc;
        doc =  sb.build(source);
        org.jdom2.Element root = doc.getRootElement();// 指向根节点
        List<org.jdom2.Element> list = root.getChildren();
        Map<String, String> refundOrderMap = new HashMap<String, String>();
        if(list!=null&&list.size()>0){
            for (org.jdom2.Element element : list) {
                refundOrderMap.put(element.getName(), element.getText());
            }
            return refundOrderMap;
        }
        return null;
    }

    /**
     * 加载证书
     */
    public static SSLConnectionSocketFactory initCert() throws Exception {
        FileInputStream instream = null;
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        instream = new FileInputStream(new File(WeiXinPublicContant.CERT_PATH));
        keyStore.load(instream, WeiXinPublicContant.WEIXIN_mch_id.toCharArray());

        if (null != instream) {
            instream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,WeiXinPublicContant.WEIXIN_mch_id.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        return sslsf;
    }


    /**
     * 微信支付签名算法sign
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）

        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + WeiXinPublicContant.KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;

    }


}
