package com.example.demo.service;

import com.example.demo.dto.RtnMsg;
import com.example.demo.dto.TagItem;
import com.example.demo.entity.Interest;
import com.example.demo.exception.MyException;

import java.util.List;

public interface InterestService {
    List<TagItem> getAll() throws MyException;

    void changeInterest(int userId, int interestId, int type) throws MyException;

    public List<TagItem> searchInterest(String key) throws MyException;

    public List<TagItem> getChildren(int id) throws MyException;

    public TagItem getOne(int id) throws MyException;

    public TagItem addOne(int id, String name) throws MyException;
}
