package com.jnu.android_demo.data;

import java.sql.Timestamp;

public class RewardItem {
    private long id;
    private Timestamp time;
    private String name;
    private int score;
    private int type;           // 1：单次奖励   2：多次奖励
    private int finishedAmount;


    public RewardItem() {
    }

    public RewardItem(Timestamp time, String name, int score, int type, int finishedAmount) {
        this.time = time;
        this.name = name;
        this.finishedAmount = finishedAmount;
        this.score = score;
        this.type = type;
    }

    /**
     * getter
     */
    public long getId() {
        return id;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public int getFinishedAmount() {
        return finishedAmount;
    }

    public int getScore() {
        return score;
    }

    public int getType() {
        return type;
    }


    /**
     * setter
     */
    public void setId(long id) {
        this.id = id;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setName(String rewardName) {
        this.name = rewardName;
    }

    public void setFinishedAmount(int amount) {
        this.finishedAmount = amount;
    }


}
