package com.gocashback.lib_common.network.model;

public class ApiModel<T> {
    public String msg;

    public int code;

    public T data;

    public T getData() {
        return data;
    }
}
