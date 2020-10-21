package com.example.demo.dto;

public class RtnMsg<T> {
    int code;
    T msg;

    public RtnMsg(int code, T m) {
        this.code = code;
        this.msg = m;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }
}
