package org.lotus.carp.webssh.quercus.mongodb.wrapper;
import com.caucho.quercus.annotation.ResourceType;
import com.caucho.quercus.env.*;
import com.mongodb.*;

import org.bson.*;
import java.util.*;
/**
 * @author : foy
 * @date : 2025/4/10:14:44
 **/
@ResourceType("mongodb client")
public class QuercusMongoClient extends EnvCloseable {
    private MongoClient mongoClient;
    private MongoClientURI uri;

    public QuercusMongoClient(Env env, StringValue server) {
        this(env, server, new ArrayValueImpl());
    }

    public QuercusMongoClient(Env env, StringValue server, ArrayValue options) {
        super(null);

        // 解析连接字符串
        String connectionString = server.toString();

        // 解析选项
        MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();

        if (options != null) {
            // 连接超时
            if (null != options.containsKey(env.createString("connectTimeoutMS"))) {
                optionsBuilder.connectTimeout(
                        options.get(env.createString("connectTimeoutMS")).toInt());
            }

            // 套接字超时
            if (null !=  options.containsKey(env.createString("socketTimeoutMS"))) {
                optionsBuilder.socketTimeout(
                        options.get(env.createString("socketTimeoutMS")).toInt());
            }

            // 其他选项...
        }

        this.uri = new MongoClientURI(connectionString, optionsBuilder);
        this.mongoClient = new MongoClient(this.uri);
    }

    @Override
    public void cleanup() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }

    public Value __getField(Env env, StringValue name) {
        String dbName = name.toString();

        if (dbName.equals("admin") || dbName.equals("local") ||
                dbName.equals("config") || dbName.matches("^[a-zA-Z0-9_-]+$")) {
            return env.wrapJava(this.mongoClient.getDatabase(dbName), env.getJavaClassDefinition(QuercusMongoDB.class)//QuercusMongoDB.class
            );
        }

        return UnsetValue.UNSET;
    }

    public Value selectDB(Env env, StringValue dbName) {
        return env.wrapJava(this.mongoClient.getDatabase(dbName.toString()), env.getJavaClassDefinition(QuercusMongoDB.class)//QuercusMongoDB.class
        );
    }

    public Value listDBs(Env env) {
        List<Document> dbs = this.mongoClient.listDatabases().into(new ArrayList<>());
        ArrayValue result = new ArrayValueImpl();

        for (Document db : dbs) {
            ArrayValue dbInfo = new ArrayValueImpl();
            dbInfo.put(env.createString("name"), env.createString(db.getString("name")));
            dbInfo.put(env.createString("sizeOnDisk"), env.createString(""+db.getLong("sizeOnDisk")));
            dbInfo.put(env.createString("empty"), env.createString(db.getBoolean("empty")?"true":"false"));

            result.put(env.createString(db.getString("name")), dbInfo);
        }

        return result;
    }

    public String getHost() {
        return this.uri.getHosts().get(0);
    }

    public int getPort() {
        return this.uri.getOptions().getServerSelectionTimeout();
    }

    //FIXME others???
    // 其他 MongoClient 方法...
}

