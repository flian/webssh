package org.lotus.carp.webssh.config.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h3>webssh</h3>
 * <p>web ssh utils</p>
 *
 * @author : foy
 * @date : 2024-02-29 10:02
 **/
public class WebSshUtils {

    /**
     * web ssh date format
     * can config by: webssh.dateFormat in spring boot startup
     */
    public static String WEB_SSH_DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String DEFAULT_NULL_DATE_STRING = "";

    /**
     * whether tools have config for startup
     */
    public static boolean isInitialized= false;

    public static String formatDateTimeStr(Date date){
        if(null == date){
            return DEFAULT_NULL_DATE_STRING;
        }
        SimpleDateFormat sd = new SimpleDateFormat(WEB_SSH_DEFAULT_DATE_FORMAT);
        return sd.format(date);
    }

    /**
     * get client ip address
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }

        return ipAddress;
    }
}
