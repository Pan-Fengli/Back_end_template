package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "discuss_interest")
public class DiscussInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int discussId;
    private int interestId;

    public DiscussInterest(int interestId, int discussId) {
        this.interestId = interestId;
        this.discussId = discussId;
    }

    public DiscussInterest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiscussId() {
        return discussId;
    }

    public void setDiscussId(int discussId) {
        this.discussId = discussId;
    }

    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }
}
