package org.lotus.carp.webssh.quercus.mongodb.wrapper;

import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.StringValue;
import com.caucho.quercus.env.Value;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Iterator;

/**
 * @author : foy
 * @date : 2025/4/10:14:48
 **/
// MongoId 类
public class QuercusMongoId {
    private ObjectId id;

    public QuercusMongoId(ObjectId id) {
        this.id = id;
    }

    public StringValue __toString(Env env) {
        return env.createString(this.id.toString());
    }

    public long getTimestamp() {
        return this.id.getTimestamp();
    }
}

// MongoCursor 类
