package com.example.demo.dto;

public class SelfHead {
    private int id;
    private String username;
    private String icon;
    private String intro;
    private String email;

    public SelfHead(SelfInfo user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.icon = user.getIcon();
        this.intro=user.getIntro();
        this.email=user.getEmail();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIntro() {
        return intro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
