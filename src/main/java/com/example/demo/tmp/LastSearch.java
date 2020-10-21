package com.example.demo.tmp;

import java.io.Serializable;

public class LastSearch implements Serializable {
    public static final int batchNum = 50;
    private int batchIndex;
    private int index;
    private int pageIndex;

    public LastSearch(int batchI, int i, int pageI) {
        this.batchIndex = batchI;
        this.index = i;
        this.pageIndex = pageI;
    }

    public static int getBatchNum() {
        return batchNum;
    }

    public int getBatchIndex() {
        return batchIndex;
    }

    public void setBatchIndex(int batchIndex) {
        this.batchIndex = batchIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
