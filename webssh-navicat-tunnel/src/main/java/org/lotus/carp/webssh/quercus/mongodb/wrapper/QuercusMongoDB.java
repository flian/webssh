package org.lotus.carp.webssh.quercus.mongodb.wrapper;
import com.caucho.quercus.env.*;
import com.mongodb.client.*;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.*;
import org.lotus.carp.webssh.quercus.mongodb.wrapper.utils.QuercusWrapperUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : foy
 * @date : 2025/4/10:14:47
 **/
public class QuercusMongoDB {
    private MongoDatabase db;

    public QuercusMongoDB(MongoDatabase db) {
        this.db = db;
    }

    public Value __getField(Env env, StringValue name) {
        return QuercusWrapperUtils.wrapJava(env,this.db.getCollection(name.toString()),QuercusMongoCollection.class);
        /*return env.wrapJava(
                this.db.getCollection(name.toString()),
                env.getJavaClassDefinition(QuercusMongoCollection.class)//QuercusMongoCollection.class
        );*/
    }

    public Value selectCollection(Env env, StringValue name) {
        return QuercusWrapperUtils.wrapJava(env,this.db.getCollection(name.toString()),QuercusMongoCollection.class);
        /*return env.wrapJava(
                this.db.getCollection(name.toString()),
                env.getJavaClassDefinition(QuercusMongoCollection.class)//QuercusMongoCollection.class
        );*/
    }


    public Value createCollection(Env env, StringValue name, ArrayValue options) {
        CreateCollectionOptions createOptions = new CreateCollectionOptions();

        if (options != null) {
            // 处理选项...
        }

        this.db.createCollection(name.toString(), createOptions);
        return selectCollection(env, name);
    }

    public Value drop(Env env) {
        this.db.drop();
        return BooleanValue.TRUE;
    }

    public Value listCollections(Env env) {
        ListCollectionsIterable<Document> collections = this.db.listCollections();
        ArrayValue result = new ArrayValueImpl();

        for (Document coll : collections) {
            Map<String,String> colMap = new HashMap<>();
            colMap.put(String.format("%s.%s",db.getName(),coll.getString("name")),coll.getString("name"));
            result.add(new JavaMapAdapter(env,colMap));
            //result.append(env.createString(String.format("%s.%s",db.getName(),coll.getString("name"))));
        }

        return result;
    }

    // 其他 MongoDB 方法...
}
