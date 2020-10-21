package com.example.demo.dao.parent;

import com.example.demo.entity.parent.Content;
import com.example.demo.entity.parent.ContentInfo;

public abstract class RichTextContentDaoImpl<T extends Content, S extends ContentInfo> extends ContentDaoImpl<T, S> {
    @Override
    public int save(T t) {
        t.setDetail(adjustPicture(t.getDetail()));
        return super.save(t);
    }

    private String adjustPicture(String content) {
        StringBuilder buffer = new StringBuilder(content);
        int start = 0, end = 0;
        while ((start = buffer.indexOf("<img", end)) >= 0) {
            end = buffer.indexOf("/>", start);
            int heightStart = buffer.indexOf("height=\"", start);
            if (heightStart >= 0 && heightStart < end) {
                int heightEnd = buffer.indexOf("\" ", heightStart);
                buffer.delete(heightStart, heightEnd + 2);
            }
            int widthStart = buffer.indexOf("width=\"", start);
            if (widthStart >= 0 && widthStart < end) {
                int widthEnd = buffer.indexOf("\" ", widthStart);
                buffer.delete(widthStart, widthEnd + 2);
            }
            buffer.insert(start + 4, " width=\"400\" ");
        }
        return buffer.toString();
    }

}
