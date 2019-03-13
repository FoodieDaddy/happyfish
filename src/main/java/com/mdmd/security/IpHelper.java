package com.mdmd.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class IpHelper {

    private static final Logger LOG = LoggerFactory.getLogger(IpHelper.class);


    public static String BLACK_IPS = "14.17.37.43 61.166.191.116 113.108.0.15 182.140.175.142 183.60.88.4 ";


    public static boolean isBlackIp(HttpServletRequest request){
        String ipAddress = getIpAddress(request);
        boolean contains = BLACK_IPS.contains(ipAddress);
        if(contains)
            LOG.info("来自黑名单的ip："+ipAddress);
        else
            LOG.info("用户ip:"+ipAddress);
        return contains;
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
//        if (LOG.isInfoEnabled()) {
//            LOG.info("Ip - X-Forwarded-For - String ip=" + ip);
//        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
//                if (LOG.isInfoEnabled()) {
//                    LOG.info("Ip - Proxy-Client-IP - String ip=" + ip);
//                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
//                if (LOG.isInfoEnabled()) {
//                    LOG.info("Ip - WL-Proxy-Client-IP - String ip=" + ip);
//                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
//                if (LOG.isInfoEnabled()) {
//                    LOG.info("Ip - HTTP_CLIENT_IP - String ip=" + ip);
//                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//                if (LOG.isInfoEnabled()) {
//                    LOG.info("Ip - HTTP_X_FORWARDED_FOR - String ip=" + ip);
//                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
//                if (LOG.isInfoEnabled()) {
//                    LOG.info("Ip - getRemoteAddr - String ip=" + ip);
//                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

}


