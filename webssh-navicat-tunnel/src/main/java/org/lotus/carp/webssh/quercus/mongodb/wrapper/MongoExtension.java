package org.lotus.carp.webssh.quercus.mongodb.wrapper;

import com.caucho.quercus.QuercusContext;
import com.caucho.quercus.module.*;


/**
 * @author : foy
 * @date : 2025/4/10:14:49
 **/
public class MongoExtension extends AbstractQuercusModule {
    public static final String[] MONGODB_CONSTANTS = {
            "MONGO_STREAMS", "MONGO_SUPPORTS_STREAMS"
    };

    @Override
    public String[] getLoadedExtensions() {
        return new String[] { "mongo" };
    }

    public  MongoExtension(QuercusContext quercusContext) {

        quercusContext.addJavaClass("MongoClient",QuercusMongoClient.class);
        quercusContext.addJavaClass("MongoDB",QuercusMongoDB.class);
        quercusContext.addJavaClass("MongoCollection",QuercusMongoCollection.class);
        quercusContext.addJavaClass("MongoId",QuercusMongoId.class);
        quercusContext.addJavaClass("MongoCursor",QuercusMongoCursor.class);
        /*Env env = Env.getCurrent();
        // 注册常量
        for (String constant : MONGODB_CONSTANTS) {
            env.addConstant(constant, LongValue.create(1),true);
        }
        // 注册类
        env.addClass("MongoClient", env.getJavaClassDefinition(QuercusMongoClient.class));
        env.addClass("MongoDB", env.getJavaClassDefinition(QuercusMongoDB.class));
        env.addClass("MongoCollection", env.getJavaClassDefinition(QuercusMongoCollection.class));
        env.addClass("MongoId", env.getJavaClassDefinition(QuercusMongoId.class));
        env.addClass("MongoCursor", env.getJavaClassDefinition(QuercusMongoCursor.class));*/
    }
}
