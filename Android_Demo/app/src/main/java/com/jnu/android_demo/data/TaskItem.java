package com.jnu.android_demo.data;

import java.sql.Timestamp;

public class TaskItem {
    private long id;
    private Timestamp time;
    private String name;
    private int score;
    private int type;   // 0: 每日任务   1: 每周任务   2: 普通任务
    private int totalAmount; // 任务数量
    private int finishedAmount; // 已完成任务数量

    public TaskItem() {
    }

    public TaskItem(Timestamp time, String name, int score, int type, int totalAmount, int finishedAmount) {
        this.time = time;
        this.name = name;
        this.score = score;
        this.type = type;
        this.totalAmount = totalAmount;
        this.finishedAmount = finishedAmount;

    }

    // getter
    public long getId() {
        return id;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getType() {
        return type;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getFinishedAmount() {
        return finishedAmount;
    }


    // setter
    public void setId(long id) {
        this.id = id;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setFinishedAmount(int finishedAmount) {
        this.finishedAmount = finishedAmount;
    }


}
