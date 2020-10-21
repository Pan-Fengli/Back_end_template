package com.example.demo.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="audit")
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int senderId;
    private int userId;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date time;
    private int state;
    private String content;
    private boolean done;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Date getTime() { return time; }

    public void setTime(Date time) { this.time = time; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
