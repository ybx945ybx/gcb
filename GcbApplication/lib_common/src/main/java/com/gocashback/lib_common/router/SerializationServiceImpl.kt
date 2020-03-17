package com.gocashback.lib_common

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.gocashback.lib_common.utils.toJson
import com.gocashback.lib_common.utils.toObject
import java.lang.reflect.Type

@Route(path = "/default/serialization")
class SerializationServiceImpl : SerializationService {
    override fun <T : Any?> json2Object(input: String, clazz: Class<T>): T {
        return toObject(input, clazz)
    }

    override fun init(context: Context?) {
    }

    override fun object2Json(instance: Any): String {
        return toJson(instance)
    }

    override fun <T : Any?> parseObject(input: String, clazz: Type): T {
        return toObject(input, clazz)

    }
}