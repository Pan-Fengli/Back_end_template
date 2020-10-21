package com.example.demo.security;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.exception.MyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("myAuthenctiationFailureHandler")
public class MyAuthenctiationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        JSONObject item=new JSONObject();
        String msg=exception.getMessage();
        if(msg.equals("找不到USER"))
        {
            item.put("code",4);//emmm不然就加一个判断。返回不同的4，5，7
        }
        else if(msg.equals("您已被封号"))
        {
            item.put("code",5);
        }
        else {
            item.put("code",7);//登录失败（密码不对）
        }
        item.put("message",msg);
        item.put("data",null);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());//如果不设置会有问题
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(item));
    }
}
