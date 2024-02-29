package org.lotus.carp.webssh.config.utils;

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
}
