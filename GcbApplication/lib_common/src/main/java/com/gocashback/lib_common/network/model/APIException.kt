package com.gocashback.lib_common.network.model

/**
 * @Author 55HAITAO
 * @Date 2019-05-09 13:44
 */
class APIException(var code: Int, var msg: String) : Exception()