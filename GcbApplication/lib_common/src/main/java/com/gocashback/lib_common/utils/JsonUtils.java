package com.gocashback.lib_common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonUtils {

    private static Gson prettyGson;
    private static Gson normalGson;

    static{
        prettyGson = new GsonBuilder().setPrettyPrinting().create();
        normalGson = new GsonBuilder().create();
    }


    /**
     * 对象转JSON
     * @param o
     * @param pretty 是否格式化
     * @return
     */
    public static String toJson(Object o, boolean pretty){
        return pretty ? prettyGson.toJson(o) : normalGson.toJson(o);
    }

    /**
     * 对象转JSON，未格式化
     * @param o
     * @return
     */
    public static String toJson(Object o){
        return toJson(o, false);
    }

    /**
     * 对象转JSON，格式化字符串
     * @param o
     * @return
     */
    public static String toJsonWithPretty(Object o){
        return toJson(o, true);
    }

    public static <T> ArrayList<T> convertToArrayList(String str, Class<T> clz){
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(ArrayList.class, new Class[]{clz});
        return normalGson.fromJson(str, listType);
    }

    public static <T> ArrayList<T> convertToArrayList(String str, Type clz){
        // 生成List<T> 中的 List<T>
        return normalGson.fromJson(str, clz);
    }

    public static <T> T convertToObject(String str, Class<T> clz){
        return normalGson.fromJson(str, clz);
    }

    public static <T> T convertToObject(String str, Type clz){
        return normalGson.fromJson(str, clz);
    }

    public static JsonObject parseJsonObject(String str){
        return normalGson.toJsonTree(str).getAsJsonObject();
    }

    public static JsonArray parseJsonArray(String str){
        return normalGson.toJsonTree(str).getAsJsonArray();
    }

    static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;
        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }
        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }
        @Override
        public Type getRawType() {
            return raw;
        }
        @Override
        public Type getOwnerType() {return null;}
    }

}
