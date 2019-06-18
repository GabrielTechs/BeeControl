package com.example.caleb.beecontrol;

public class Assistance {

    public String employeeName;
    public String employeeEmail;
    public String status;
    public String assistDate;
    public String assistHour;

    Assistance(){

    }

    Assistance(String employeeName, String employeeEmail,String status, String assistDate, String assistHour){
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.status = status;
        this.assistDate = assistDate;
        this.assistHour = assistHour;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getAssistHour() {return assistHour;}

    public String getEmployeeEmail() { return employeeEmail;}

    public String getStatus() {
        return status;
    }

    public String getAssistDate() {
        return assistDate;
    }
}
