package com.example.demo.dto;

public class CheckItem {
    private boolean result;
    public CheckItem(boolean result)
    {
        this.result=result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
