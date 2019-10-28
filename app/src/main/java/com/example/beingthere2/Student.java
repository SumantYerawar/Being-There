package com.example.beingthere2;

class Student {
    String syear;
    String sbatch;
    String sdiv;
    String sname;
    String sid;
    String classname;
    String spass;
    String rollno;

    public Student(String sname,String classname,String sbatch, String rollno,String sid,String spass) {
        this.sname = sname;
        this.sid = sid;
        this.classname = classname;
        this.spass = spass;
        this.sbatch=sbatch;
        this.rollno=rollno;
    }

    public String getSname() { return sname; }

    public String getSid() {
        return sid;
    }


    public String getSbatch() {
        return sbatch;
    }

    public String getClassname() {
        return classname;
    }

    public String getSpass() {
        return spass;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getRollno() {
        return rollno;
    }
}
