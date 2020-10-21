package com.example.demo.entity;

import com.example.demo.entity.parent.ContentInfo;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "discuss_info")
public class DiscussInfo extends ContentInfo {
    private String text;

    public DiscussInfo() {
        super();
    }

    public DiscussInfo(int id, String detail, String text) {
        super(id, detail);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
