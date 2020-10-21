package com.example.demo.tmp;

import java.util.ArrayList;
import java.util.List;

public class PageHelper<T> {
    public List<T> naivePage(int pageSize, int pageIndex, List<T> list) {
        list.sort((o1, o2) -> o2.hashCode() - o1.hashCode());
        int allNum = list.size();
        int start = pageIndex * pageSize;
        if (start >= allNum) {
            return new ArrayList<>();
        }
        int end = start + pageSize;
        return list.subList(start, Math.min(allNum, end));
    }
}
