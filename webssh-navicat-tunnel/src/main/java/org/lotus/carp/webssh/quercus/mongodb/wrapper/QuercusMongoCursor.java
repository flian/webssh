package org.lotus.carp.webssh.quercus.mongodb.wrapper;

import com.caucho.quercus.env.ArrayValue;
import com.caucho.quercus.env.ArrayValueImpl;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.Value;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.lotus.carp.webssh.quercus.mongodb.wrapper.utils.QuercusWrapperUtils;

import java.util.Iterator;

import static org.lotus.carp.webssh.quercus.mongodb.wrapper.utils.QuercusUtils.convertDocumentToValue;

/**
 * @author : foy
 * @date : 2025/4/11:14:45
 **/
public class QuercusMongoCursor implements Iterator<Value> {
    private MongoCursor<Document> cursor;
    private Env env;



    private long count;

    public QuercusMongoCursor(Env env, MongoCursor<Document> cursor) {
        this.env = env;
        this.cursor = cursor;
    }

    public boolean hasNext() {
        return this.cursor.hasNext();
    }

    public Value getNext(){
        return next();
    }

    public Value next() {
        Document doc = this.cursor.next();
        // 将 Document 转换为 PHP 数组
        // 实现转换逻辑...
        return convertDocumentToValue(env,doc);
        //env.wrapJava(doc);
    }

    public Value sort(Env env, ArrayValue fields){
        //FIXME need impl
        return QuercusWrapperUtils.wrapJava(env,cursor,QuercusMongoCursor.class,count);
    }

    public Value skip(Env env,Integer skip){
        //FIXME need impl
        return QuercusWrapperUtils.wrapJava(env,cursor,QuercusMongoCursor.class,count);
    }

    public Value count(Env env){
        //FIXME fix it
        return env.wrapJava(count);
    }

    public void close() {
        this.cursor.close();
    }

    public ArrayValue listRows(Env env){
        ArrayValue result = new ArrayValueImpl();
        int i=0;
        while (cursor.hasNext()){
            result.put(env.wrapJava(i),next());
            i++;
        }
        return result;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

