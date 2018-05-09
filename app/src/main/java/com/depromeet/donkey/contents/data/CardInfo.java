package com.depromeet.donkey.contents.data;

public class CardInfo {
    private int dday;
    private String title;

    public CardInfo(int dday, String title) {
        this.dday = dday;
        this.title = title;
    }

    public int getDday() {
        return dday;
    }

    public void setDday(int dday) {
        this.dday = dday;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
