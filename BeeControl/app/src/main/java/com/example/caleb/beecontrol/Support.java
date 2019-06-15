package com.example.caleb.beecontrol;

public class Support {
    public String employeeName;
    public String id;
    public String supportDate;
    public String subject;

    Support(){

    }

    Support (String employeeName, String id, String supportDate, String subject){
        this.employeeName = employeeName;
        this.id = id;
        this.supportDate = supportDate;
        this.subject = subject;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getId() {
        return id;
    }

    public String getSupportDate() {
        return supportDate;
    }

    public String getSubject (){return subject;}
}
