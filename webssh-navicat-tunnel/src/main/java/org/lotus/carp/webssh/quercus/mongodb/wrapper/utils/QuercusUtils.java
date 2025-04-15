package org.lotus.carp.webssh.quercus.mongodb.wrapper.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author : foy
 * @date : 2025/4/15:10:45
 **/
@Slf4j
public class QuercusUtils {
    public static void printValues(Object... values){
        StringBuilder sb = new StringBuilder();
        if(null != values && values.length>0){
            Arrays.stream(values).forEach(sb::append);
        }
        log.info("######## start printValues: ##########");
        log.info(sb.toString());
        log.info("######## end printValues. ##########");
    }
}
