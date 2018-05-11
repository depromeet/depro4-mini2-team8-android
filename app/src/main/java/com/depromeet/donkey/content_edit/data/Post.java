package com.depromeet.donkey.content_edit.data;

public class Post {
    private int areaNo;
    private double lat;
    private double lng;
    private String title;
    private String content;
    private String nickname;
    private int accountNo;
    private String si;
    private String gu;
    private String dong;
    private int postNo;

    public Post(int areaNo, double lat, double lng, String title, String content, String nickname, int accountNo, String si, String gu, String dong, int postNo) {
        this.areaNo = areaNo;
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.accountNo = accountNo;
        this.si = si;
        this.gu = gu;
        this.dong = dong;
        this.postNo = postNo;
    }
}
