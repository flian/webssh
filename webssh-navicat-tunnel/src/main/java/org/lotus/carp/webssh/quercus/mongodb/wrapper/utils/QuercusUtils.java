package org.lotus.carp.webssh.quercus.mongodb.wrapper.utils;

import com.caucho.quercus.env.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.checkerframework.checker.units.qual.A;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author : foy
 * @date : 2025/4/15:10:45
 **/
@Slf4j
public class QuercusUtils {
    private static boolean LOG_START_END_PREFIX = true;

    public static void printValues(Object... values){
        StringBuilder sb = new StringBuilder();
        if(null != values && values.length>0){
            Arrays.stream(values).forEach(sb::append);
        }
        if(LOG_START_END_PREFIX){
            log.info("######## start printValues: ##########");
        }
        log.info(sb.toString());
        if(LOG_START_END_PREFIX){
            log.info("######## end printValues. ##########");
        }
    }

    public static Value convertDocumentToValue(Env env, Document document){
        ArrayValue result = new ArrayValueImpl();
        for (Map.Entry<String, Object> entry : document.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Document) {
                result.put(env.createString(key), convertDocumentToValue(env, (Document) value));
            } else {
                result.put(env.createString(key), convertToValue(env, value));
            }
        }
        return result;
    }

    public static Value convertToValue(Env env, Object value) {
        if (value == null) {
            return NullValue.NULL;
        } else if (value instanceof Boolean) {
            return BooleanValue.create((Boolean) value);
        } else if (value instanceof Number) {
            return env.wrapJava(value);
        } else if (value instanceof String) {
            return env.createString((String) value);
        } else if (value instanceof List) {
            // 处理列表...
            ArrayValue result = new ArrayValueImpl();
            for(Object o:(List<?>)value){
                result.add(env.wrapJava(o));
            }
            return result;
        }

        return env.createString(value.toString());
    }
}
