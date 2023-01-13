package com.example.ssgdesking.Data;

public class ReservationLoginData {
    private String empno;

    private String password;

    public ReservationLoginData(String empno, String password) {
        this.empno = empno;
        this.password = password;
    }

    public String getEmpno() {
        return empno;
    }

    public void setEmpno(String empno) {
        this.empno = empno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
