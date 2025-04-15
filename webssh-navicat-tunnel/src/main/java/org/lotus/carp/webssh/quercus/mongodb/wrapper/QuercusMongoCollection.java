package org.lotus.carp.webssh.quercus.mongodb.wrapper;


import com.caucho.quercus.env.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;

import org.bson.*;
import org.bson.conversions.Bson;
import org.lotus.carp.webssh.quercus.mongodb.wrapper.utils.QuercusWrapperUtils;

import java.util.*;

/**
 * @author : foy
 * @date : 2025/4/10:14:48
 **/


public class QuercusMongoCollection {
    private MongoCollection<Document> collection;

    public QuercusMongoCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public Value insert(Env env, ArrayValue document, ArrayValue options) {
        InsertOneOptions insertOptions = new InsertOneOptions();

        if (options != null) {
            //FIXME options
            // 处理选项...
        }

        Document doc = convertToDocument(env, document);
        this.collection.insertOne(doc, insertOptions);
        return QuercusWrapperUtils.wrapJava(env,doc.getObjectId("_id"),QuercusMongoId.class);
        /*return env.wrapJava(doc.getObjectId("_id"), env.getJavaClassDefinition(QuercusMongoId.class)//QuercusMongoId.class
        );*/
    }

    public Value batchInsert(Env env, ArrayValue documents, ArrayValue options) {
        List<Document> docs = new ArrayList<>();

        for (Value entry : documents.values()) {
            if (entry.isArray()) {
                docs.add(convertToDocument(env, (ArrayValue) entry));
            }
        }

        InsertManyOptions insertOptions = new InsertManyOptions();
        this.collection.insertMany(docs, insertOptions);

        ArrayValue result = new ArrayValueImpl();
        for (Document doc : docs) {
            result.append(QuercusWrapperUtils.wrapJava(env,doc.getObjectId("_id"),QuercusMongoId.class));
            /*result.append(env.wrapJava(doc.getObjectId("_id"), env.getJavaClassDefinition(QuercusMongoId.class)//QuercusMongoId.class
            ));*/
        }

        return result;
    }

    public Value getIndexInfo(Env env){
        ListIndexesIterable<Document> indexes = this.collection.listIndexes();
        return QuercusWrapperUtils.wrapJava(env,indexes.iterator(),QuercusMongoCursor.class);
        //return env.wrapJava(this.collection.listIndexes());
    }

    public Value listRows(Env env){
        long count = this.collection.countDocuments();
        FindIterable<Document> cursor = this.collection.find();
        return QuercusWrapperUtils.wrapJava(env,cursor.iterator(),QuercusMongoCursor.class,count);
    }

    public Value find(Env env){
        long count = this.collection.countDocuments();
        FindIterable<Document> cursor = this.collection.find();
        return QuercusWrapperUtils.wrapJava(env,cursor.iterator(),QuercusMongoCursor.class,count);
    }

    public Value find(Env env, ArrayValue query, ArrayValue fields) {
        Bson filter = convertToBson(env, query);
        Bson projection = fields != null ? convertToBson(env, fields) : null;
        long count = this.collection.countDocuments(filter);
        FindIterable<Document> cursor = this.collection.find(filter);

        if (projection != null) {
            cursor.projection(projection);
        }
        return QuercusWrapperUtils.wrapJava(env,cursor.iterator(),QuercusMongoCursor.class,count);
        /*return env.wrapJava(cursor.iterator(), env.getJavaClassDefinition(QuercusMongoCursor.class)//QuercusMongoCursor.class
        );*/
    }



    public Value findOne(Env env, ArrayValue query, ArrayValue fields) {
        Bson filter = convertToBson(env, query);
        Bson projection = fields != null ? convertToBson(env, fields) : null;

        Document doc = projection != null
                ? this.collection.find(filter).projection(projection).first()
                : this.collection.find(filter).first();

        return doc != null ? convertToArray(env, doc) : BooleanValue.FALSE;
    }

    public Value count(Env env){
        return convertToValue(env,this.collection.countDocuments());
    }

    public Value update(Env env, ArrayValue criteria, ArrayValue newObj, ArrayValue options) {
        Bson filter = convertToBson(env, criteria);
        Bson update = convertToBson(env, newObj);

        UpdateOptions updateOptions = new UpdateOptions();
        if (options != null) {
            if (options.get(env.createString("multiple")).toBoolean()) {
                updateOptions.upsert(false);
            }
            if (options.get(env.createString("upsert")).toBoolean()) {
                updateOptions.upsert(true);
            }
        }

        UpdateResult result = this.collection.updateMany(filter, update, updateOptions);

        ArrayValue ret = new ArrayValueImpl();
        ret.put(env.createString("n"), env.createString(result.getMatchedCount()));
        ret.put(env.createString("updatedExisting"), env.createString(result.getMatchedCount() > 0 ?"true":"false"));
        ret.put(env.createString("ok"), env.createString(""+1.0));

        return ret;
    }

    // 其他 MongoCollection 方法...

    private Document convertToDocument(Env env, ArrayValue array) {
        Document doc = new Document();
        if(null != array && !array.isEmpty()){
            for (Map.Entry<Value, Value> entry : array.entrySet()) {
                String key = entry.getKey().toString();
                Value value = entry.getValue();

                if (value.isArray()) {
                    doc.put(key, convertToDocument(env, (ArrayValue) value));
                } else {
                    doc.put(key, convertValue(value,env));
                }
            }
        }
        return doc;
    }

    private Bson convertToBson(Env env, ArrayValue array) {
        return convertToDocument(env, array);
    }

    private Object convertValue(Value value,Env env) {
        if (value.isNull()) {
            return null;
        } else if (value.isBoolean()) {
            return value.toBoolean();
        } else if (value.isDouble()) {
            return value.toDouble();
        } else if (value.isLong()) {
            return value.toLong();
        } else if (value.isString()) {
            return value.toString();
        } else if (value.isArray()) {
            return convertToDocument(env, (ArrayValue) value);
        }

        return value.toString();
    }

    private Value convertToArray(Env env, Document doc) {
        ArrayValue array = new ArrayValueImpl();

        for (Map.Entry<String, Object> entry : doc.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Document) {
                array.put(env.createString(key), convertToArray(env, (Document) value));
            } else {
                array.put(env.createString(key), convertToValue(env, value));
            }
        }

        return array;
    }

    private Value convertToValue(Env env, Object value) {
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