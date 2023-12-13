package com.jnu.android_demo.data;

public class RewardItem {
    private String rewardName;
    private int finishedAmount;
    private int score;
    private int type;   // 1：单次奖励   2：多次奖励

    public RewardItem(String rewardName, int finishedAmount, int score, int type) {
        this.rewardName = rewardName;
        this.finishedAmount = finishedAmount;
        this.score = score;
        this.type = type;
    }

    /**
     * getter
     */
    public String getRewardName() {
        return rewardName;
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
    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public void setFinishedAmount(int amount) {
        this.finishedAmount = amount;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setType(int type) {
        this.type = type;
    }
}
