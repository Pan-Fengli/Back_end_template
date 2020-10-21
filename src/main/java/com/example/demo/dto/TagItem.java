package com.example.demo.dto;


import com.example.demo.entity.Interest;

import java.io.Serializable;

public class TagItem implements Serializable {
    private int id;
    private String name;
    private String fatherName;

    public TagItem(Interest interest) {
        this.id = interest.getId();
        this.name = interest.getName();
    }

    public TagItem(Interest interest, String fatherName) {
        this.id = interest.getId();
        this.name = interest.getName();
        this.fatherName = fatherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
