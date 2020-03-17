package com.gocashback.lib_common.utils

import com.google.gson.GsonBuilder
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.ArrayList


private val prettyGson = GsonBuilder().setPrettyPrinting().create()
private val normalGson = GsonBuilder().create()

/**
 * 对象转JSON
 * @param o
 * @param pretty 是否格式化
 * @return
 */
fun toJson(o: Any, pretty: Boolean = false) = if (pretty) prettyGson.toJson(o) else normalGson.toJson(o)

fun <T> toObject(json: String?, clazz: Class<T>): T = normalGson.fromJson(json, clazz)

fun <T> toObject(json: String?, type: Type): T = normalGson.fromJson(json, type)

fun <T> toArrayList(json: String?, clazz: Class<T>): T = ParameterizedTypeImpl(ArrayList::class.java, arrayOf(clazz)).let {
    return normalGson.fromJson(json, it)
}

fun <T> toArrayList(json: String, type: Type): T = normalGson.fromJson(json, type)

fun toJsonObject(json: String) = normalGson.toJsonTree(json).asJsonObject
fun toJsonArray(json: String) = normalGson.toJsonTree(json).asJsonArray


class ParameterizedTypeImpl(private val raw: Class<*>, private val args: Array<Type>?) : ParameterizedType {

    override fun getRawType(): Type {
        return raw
    }

    override fun getOwnerType(): Type? {
        return null
    }

    override fun getActualTypeArguments(): Array<Type> {
        return args ?: emptyArray()
    }
}