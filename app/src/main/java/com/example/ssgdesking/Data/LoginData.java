package com.example.ssgdesking.Data;

import com.airbnb.lottie.L;

public class LoginData {
    private String id;
    private String seatid;

    public LoginData(String id) {
        this.id = id;
    }

    public LoginData(String id, String seatid) {
        this.id = id;
        this.seatid = seatid;
    }

    public LoginData() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
