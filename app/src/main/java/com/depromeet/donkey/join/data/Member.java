package com.depromeet.donkey.join.data;

public class Member {
    private String email;
    private String id;
    private String password;

    public Member(String email, String name, String password) {
        this.email = email;
        this.id = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
