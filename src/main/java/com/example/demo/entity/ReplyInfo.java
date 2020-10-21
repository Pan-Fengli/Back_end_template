package com.example.demo.entity;

import com.example.demo.entity.parent.ContentInfo;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reply_info")
public class ReplyInfo extends ContentInfo {
    public ReplyInfo() {
        super();
    }

    public ReplyInfo(int id, String detail) {
        super(id, detail);
    }
}
