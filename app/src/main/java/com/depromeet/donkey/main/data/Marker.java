package com.depromeet.donkey.main.data;

import java.io.Serializable;

public class Marker implements Serializable{
    private int postNo;
    private int areaNo;
    private double lat;
    private double lng;
    private String title;
    private String content;
    private String nickname;
    private int accountNo;
    private String createAt;
    private String expiredAt;

    public Marker(int postNo, int areaNo, double lat, double lng, String title, String content, String nickname, int accountNo, String createAt, String expiredAt) {
        this.postNo = postNo;
        this.areaNo = areaNo;
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.accountNo = accountNo;
        this.createAt = createAt;
        this.expiredAt = expiredAt;
    }

    public Marker(double lat, double lng, String title, String content, String nickname, String createAt, String expiredAt) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.createAt = createAt;
        this.expiredAt = expiredAt;
    }

    public int getPostNo() {
        return postNo;
    }

    public void setPostNo(int postNo) {
        this.postNo = postNo;
    }

    public int getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(int areaNo) {
        this.areaNo = areaNo;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }
}
