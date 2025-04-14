package org.lotus.carp.webssh.quercus.mongodb.wrapper.utils;

import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.NullValue;
import com.caucho.quercus.env.Value;
import com.caucho.quercus.program.JavaClassDef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.lotus.carp.webssh.quercus.mongodb.wrapper.QuercusMongoCollection;
import org.lotus.carp.webssh.quercus.mongodb.wrapper.QuercusMongoCursor;
import org.lotus.carp.webssh.quercus.mongodb.wrapper.QuercusMongoDB;
import org.lotus.carp.webssh.quercus.mongodb.wrapper.QuercusMongoId;

import java.util.Objects;


/**
 * @author : foy
 * @date : 2025/4/11:14:21
 **/
public class QuercusWrapperUtils {

    public static Value wrapJava(Env env,Object me, Class<?> target,Object... values){
        if (me == null) {
            return NullValue.NULL;
        }

        if (me instanceof Value) {
            return (Value) me;
        }
        JavaClassDef def = env.getJavaClassDefinition(target);
        // XXX: why is this logic here?  The def should be correct on the call
        // logic is for JavaMarshal, where can avoid the lookup call
        //copy from Env wrapJava
        //for mongo class, we need do some special...may not correct,but it should work for now.
        Class<?> meClass = me.getClass();
        if (def.getType() != meClass) {
            if(QuercusMongoDB.class.equals(target)){
                return env.wrapJava(new QuercusMongoDB((MongoDatabase)me));
            }else if(QuercusMongoCollection.class.equals(target)){
                return env.wrapJava(new QuercusMongoCollection((MongoCollection<Document>)me));
            }else if(QuercusMongoId.class.equals(target)){
                return env.wrapJava(new QuercusMongoId((ObjectId)me));
            }else if(QuercusMongoCursor.class.equals(target)){
                QuercusMongoCursor cursor = new QuercusMongoCursor(env, (MongoCursor<Document>) me);
                if(Objects.nonNull(values) && values.length > 0){
                    cursor.setCount((long)values[0]);
                }
                return env.wrapJava(cursor);
            }
            def = env.getJavaClassDefinition(me.getClass());
        }

        return def.wrap(env, me);
    }
}
