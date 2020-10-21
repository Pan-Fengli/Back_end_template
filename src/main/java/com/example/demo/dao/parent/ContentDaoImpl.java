package com.example.demo.dao.parent;

import com.example.demo.entity.parent.Content;
import com.example.demo.entity.parent.ContentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public abstract class ContentDaoImpl<T extends Content, S extends ContentInfo> implements ContentDao<T, S> {
    @Autowired
    protected JpaRepository<T, Integer> jpaRepository;
    @Autowired
    protected MongoRepository<S, Integer> mongoRepository;

    protected abstract S newContentInfo(T t);

    protected abstract void addInfoBy(T t, S s);

    protected T addInfo(T t) {
        int id = t.getId();
        Optional<S> sOptional = mongoRepository.findById(id);
        if (sOptional.isEmpty()) {
            t.setDetail("");
        } else {
            addInfoBy(t, sOptional.get());
        }
        return t;
    }

    protected List<T> listAddInfo(List<T> tList) {
        for (T t : tList) {
            addInfo(t);
        }
        return tList;
    }

    @Override
    public T findById(int id) {
        Optional<T> t = jpaRepository.findById(id);
        if (t.isEmpty()) {
            return null;
        }
        return addInfo(t.get());
    }

    @Override
    public T findByIdMySQL(int id) {
        Optional<T> tOptional = jpaRepository.findById(id);
        if (tOptional.isEmpty()) {
            return null;
        } else {
            return tOptional.get();
        }
    }

    @Override
    public int save(T t) {
        int id = jpaRepository.save(t).getId();
        t.setId(id);
        S s = newContentInfo(t);
        mongoRepository.save(s);
        return id;
    }

    @Override
    public void deleteById(int id) {
        jpaRepository.deleteById(id);
        mongoRepository.deleteById(id);
    }

    @Override
    public void saveMySQL(T t) {
        jpaRepository.save(t);
    }
}
