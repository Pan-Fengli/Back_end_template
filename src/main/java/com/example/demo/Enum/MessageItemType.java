package com.example.demo.Enum;

public enum MessageItemType {
    MSG_COMMENT,
    MSG_REPLY,
    MSG_USER_DISCUSS_LIKE,
    MSG_USER_COMMENT_LIKE;

    public static MessageItemType parseString(String string){
        if(string!=null){
            return Enum.valueOf(MessageItemType.class,string.trim());
        }else return null;
    }
}

