package com.jnu.android_demo.data;

public class TaskItem {
    private String taskName;
    private int score;
    private int type;   // 0: 每日任务   1: 每周任务   2: 普通任务
    private int amount; // 任务数量
    private int finishedAmount; // 已完成任务数量


    public TaskItem(String taskName, int score, int type, int amount, int finishedAmount) {
        this.taskName = taskName;
        this.score = score;
        this.type = type;
        this.amount = amount;
        this.finishedAmount = finishedAmount;

    }

    // getter
    public String getTaskName() {
        return taskName;
    }

    public int getScore() {
        return score;
    }

    public int getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public int getFinishedAmount() {
        return finishedAmount;
    }



    // setter
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setFinishedAmount(int finishedAmount) {
        this.finishedAmount = finishedAmount;
    }


}
