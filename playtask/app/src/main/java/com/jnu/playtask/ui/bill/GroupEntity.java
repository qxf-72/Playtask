package com.jnu.playtask.ui.bill;

import com.jnu.playtask.data.CountItem;

import java.util.ArrayList;

/**
 * 组数据的实体类
 */
public class GroupEntity {

    private String header;
    private String footer;
    private ArrayList<CountItem> children;

    public GroupEntity(String header, String footer, ArrayList<CountItem> children) {
        this.header = header;
        this.footer = footer;
        this.children = children;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public ArrayList<CountItem> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<CountItem> children) {
        this.children = children;
    }
}
