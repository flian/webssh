package org.lotus.carp.webssh.quercus.mongodb.wrapper;
import com.caucho.quercus.env.*;
import com.mongodb.client.*;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.*;

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
        return env.wrapJava(
                this.db.getCollection(name.toString()),
                env.getJavaClassDefinition(QuercusMongoCollection.class)//QuercusMongoCollection.class
        );
    }

    public Value selectCollection(Env env, StringValue name) {
        return env.wrapJava(
                this.db.getCollection(name.toString()),
                env.getJavaClassDefinition(QuercusMongoCollection.class)//QuercusMongoCollection.class
        );
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
            result.append(env.createString(coll.getString("name")));
        }

        return result;
    }

    // 其他 MongoDB 方法...
}
