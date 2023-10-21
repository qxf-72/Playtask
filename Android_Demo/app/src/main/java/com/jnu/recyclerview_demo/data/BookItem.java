package com.jnu.recyclerview_demo.data;

import java.io.Serializable;

public class BookItem implements Serializable {
    private String title;
    private int imageId;

    public BookItem(String name, int imageId) {
        this.title = name;
        this.imageId = imageId;
    }
    //getter
    public String getTitle() { return title; }
    public int getCoverResourceId() { return imageId;}
    //setter
    public void setTitle(String title) { this.title = title; }
    public void setCoverResourceId(int imageId) { this.imageId = imageId; }
}
