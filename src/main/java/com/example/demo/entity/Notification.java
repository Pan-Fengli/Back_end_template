package com.example.demo.entity;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int senderId;
    private int getterId;
    private String content;
    private boolean isRead;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date time;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getGetterId() {
        return getterId;
    }

    public void setGetterId(int getterId) {
        this.getterId = getterId;
    }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public Date getTime() { return time; }

    public void setTime(Date time) { this.time = time; }
}
