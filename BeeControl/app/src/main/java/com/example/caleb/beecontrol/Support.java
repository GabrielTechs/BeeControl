package com.example.caleb.beecontrol;

public class Support {
    public String employeeName;
    public int id;
    public String employeeEmail;
    public String supportDate;
    public String subject;

    Support(){

    }

    Support (String employeeName, String employeeEmail, int id, String supportDate, String subject){
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.id = id;
        this.supportDate = supportDate;
        this.subject = subject;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getId() {
        return id;
    }

    public String getEmployeeEmail() { return employeeEmail;}

    public String getSupportDate() {
        return supportDate;
    }

    public String getSubject (){return subject;}
}
