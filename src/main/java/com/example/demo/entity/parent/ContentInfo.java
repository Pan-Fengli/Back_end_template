package com.example.demo.entity.parent;

import javax.persistence.Id;

public class ContentInfo {
    @Id
    private int id;
    private String content;
    public ContentInfo(){}

    public ContentInfo(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
