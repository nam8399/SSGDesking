package com.example.ssgdesking.Data;

public class ReviewData {
    private String id;
    private String empid;
    private String seatid;
    private String context;

    public ReviewData() {}

    public ReviewData(String id, String seatid, String context) {
        this.id = id;
        this.seatid = seatid;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getSeatid() {
        return seatid;
    }

    public void setSeatid(String seatid) {
        this.seatid = seatid;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }


}
