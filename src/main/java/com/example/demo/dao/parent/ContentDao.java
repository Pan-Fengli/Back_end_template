package com.example.demo.dao.parent;

import com.example.demo.entity.parent.Content;
import com.example.demo.entity.parent.ContentInfo;

import java.util.List;

public interface ContentDao<T extends Content, S extends ContentInfo> {
    T findById(int id);

    T findByIdMySQL(int id);

    int save(T t);

    void deleteById(int id);

    void saveMySQL(T t);
}
