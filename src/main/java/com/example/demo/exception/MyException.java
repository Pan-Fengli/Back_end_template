package com.example.demo.exception;

public class MyException extends Exception {
    //异常错误码
    private int code;
    //异常提示信息
    private String msg;

    public MyException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }

    public String getMsg() { return msg; }

    public void setMsg(String msg) { this.msg = msg; }
}
