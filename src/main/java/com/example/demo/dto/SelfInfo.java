package com.example.demo.dto;

import com.example.demo.entity.User;

public class SelfInfo {
    private int id;
    private String username;
    private String icon;
    private int isAdmin;
    private String password;
    private String email;
    private String gender;
    private String hometown;
    private String phoneNumber;
    private String intro;

//    public boolean isSame(SelfInfo info)
//    {
//        if(info==null)
//        {
//            return false;
//        }
//        return info.id==this.id
//                && info.intro==this.intro//空指针不能够比较
////                && info.username==this.username
////                && info.icon==this.icon
////                && info.isAdmin==this.isAdmin
////                && info.password==this.password
////                && info.email==this.email
////                && info.gender==this.gender
////                && info.hometown==this.hometown
////                && info.phoneNumber==this.phoneNumber
//        ;
//    }

    public SelfInfo(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.icon = user.getIcon();
        this.isAdmin=user.getRoot();
        this.password=user.getPassword();
        this.email=user.getEmail();
        this.gender=user.getGender();
        this.hometown=user.getHometown();
        this.phoneNumber=user.getPhoneNumber();
        this.intro=user.getIntro();
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

    public void setIsAdmin(int isAdmin){this.isAdmin=isAdmin;}

    public int getIsAdmin(){return this.isAdmin;}

    public String getPassword(){return this.password;}

    public void setPassword(String password){this.password=password;}

    public String getEmail(){return this.email;}

    public void setEmail(String email){this.email=email;}

    public void setGender(String gender){this.gender=gender;}

    public String getGender() {
        return gender;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
