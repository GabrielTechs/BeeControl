package com.example.caleb.beecontrol;

public class Assistance {

    private String employeeName;
    private String employeeEmail;
    private String status;
    private String assistDate;
    private String assistTime;

    Assistance(){

    }

    Assistance(String employeeName, String employeeEmail,String status, String assistDate, String assistTime){
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.status = status;
        this.assistDate = assistDate;
        this.assistTime = assistTime;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeEmail() { return employeeEmail;}

    public String getStatus() {
        return status;
    }

    public String getAssistDate() {
        return assistDate;
    }

    public String getAssistTime() {
        return assistTime;
    }
}
