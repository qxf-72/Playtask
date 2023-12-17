package com.jnu.playtask.data;

import java.sql.Timestamp;

public class CountItem {
    private int id;
    private Timestamp time;
    private String name;
    private int score;

    public CountItem() {
    }

    public CountItem(Timestamp time, String name, int score) {
        this.time = time;
        this.name = name;
        this.score = score;
    }

    /**
     * getter
     */
    public int getId() {
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


    /**
     * setter
     */
    public void setId(int id) {
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
}
