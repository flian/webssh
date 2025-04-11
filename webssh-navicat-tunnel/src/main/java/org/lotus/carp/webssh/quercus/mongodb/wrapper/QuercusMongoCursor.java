package org.lotus.carp.webssh.quercus.mongodb.wrapper;

import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.Value;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.Iterator;

/**
 * @author : foy
 * @date : 2025/4/11:14:45
 **/
public class QuercusMongoCursor implements Iterator<Value> {
    private MongoCursor<Document> cursor;
    private Env env;

    public QuercusMongoCursor(Env env, MongoCursor<Document> cursor) {
        this.env = env;
        this.cursor = cursor;
    }

    public boolean hasNext() {
        return this.cursor.hasNext();
    }

    public Value next() {
        Document doc = this.cursor.next();
        // 将 Document 转换为 PHP 数组
        // 实现转换逻辑...
        //FIXME 转换
        return env.wrapJava(doc);
    }

    public void close() {
        this.cursor.close();
    }
}

