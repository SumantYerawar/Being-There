package com.example.beingthere2;

class Attendance_sheet {
    String cn,dbms,sepm,isee,sdl,sdll,cnl,dbmsl;

    public Attendance_sheet(String cn, String dbms, String sepm, String isee, String sdl, String sdll, String cnl, String dbmsl) {
        this.cn=cn;
        this.dbms=dbms;
        this.sepm=sepm;
        this.isee=isee;
        this.sdl=sdl;
        this.sdll=sdll;
        this.cnl=cnl;
        this.dbmsl=dbmsl;
    }

    public String getCn() {
        return cn;
    }

    public String getCnl() {
        return cnl;
    }

    public String getDbms() {
        return dbms;
    }

    public String getDbmsl() {
        return dbmsl;
    }

    public String getIsee() {
        return isee;
    }

    public String getSdl() {
        return sdl;
    }

    public String getSdll() {
        return sdll;
    }

    public String getSepm() {
        return sepm;
    }
}
