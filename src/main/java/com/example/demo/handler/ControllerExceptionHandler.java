package com.example.demo.handler;

import com.alibaba.fastjson.JSONException;
import com.example.demo.dto.ResultBean;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyUsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;


@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResultBean handleMyException(MyException e){
        return ResultBean.error(e.getCode(),e.getMsg());
    }

    @ExceptionHandler
    public ResultBean handleParseException(ParseException e){
        return ResultBean.error(2,"日期格式错误");
    }

    @ExceptionHandler
    public ResultBean handleNumberFormatException(NumberFormatException e){
        return ResultBean.error(3,"参数错误");
    }

    @ExceptionHandler
    public ResultBean handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        return ResultBean.error(1,"缺少参数");
    }//
    @ExceptionHandler
    public ResultBean handleJSONException(JSONException e){
        return ResultBean.error(8,"body格式错误");
    }
    @ExceptionHandler
    public ResultBean handleNullPointerException(NullPointerException e){
        return ResultBean.error(9,"null错误");
    }
    @ExceptionHandler
    public ResultBean handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return ResultBean.error(11,"请求方法错误");
    }
    @ExceptionHandler
    public ResultBean handleMyUsernameNotFoundException(MyUsernameNotFoundException e){
        System.out.println("catch");
        return ResultBean.error(5,e.getMessage());
    }
//    @ExceptionHandler
//    public ResultBean handleIOException(IOException e){
//        System.out.println("IOException");
//        return ResultBean.error(5,e.getMessage());
//    }
}
