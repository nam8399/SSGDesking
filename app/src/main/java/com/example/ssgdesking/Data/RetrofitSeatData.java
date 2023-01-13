package com.example.ssgdesking.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetrofitSeatData {
    @SerializedName("deptid")
    @Expose
    private String deptid;

    @SerializedName("deptName")
    @Expose
    private String deptName;

    @SerializedName("empno")
    @Expose
    private String empno;

    @SerializedName("name")
    @Expose
    private String name;

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEmpno() {
        return empno;
    }

    public void setEmpno(String empno) {
        this.empno = empno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
