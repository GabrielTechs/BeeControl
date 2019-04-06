package com.example.caleb.beecontrol;

public class AssistanceNew {

    public String employeeName;
    public String status;
    public String assistDate;

    AssistanceNew(){

    }

    AssistanceNew(String employeeName, String status, String assistDate){
        this.employeeName = employeeName;
        this.status = status;
        this.assistDate = assistDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getStatus() {
        return status;
    }

    public String getAssistDate() {
        return assistDate;
    }
}
