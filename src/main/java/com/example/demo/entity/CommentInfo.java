package com.example.demo.entity;

import com.example.demo.entity.parent.ContentInfo;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comment_info")
public class CommentInfo extends ContentInfo {
    public CommentInfo(){super();}
    public CommentInfo(int id, String detail) {
        super(id, detail);
    }
}
