package com.example.caleb.beecontrol;

public class Employee {

    public String name;
    public String lastName;
    public String email;
    public String status;
    public boolean isAdmin;
    public boolean onTrip;

    Employee(){

    }

    Employee(String name, String lastName, String email, boolean isAdmin, boolean onTrip){
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.onTrip = onTrip;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAdmin() {return isAdmin;}

    public boolean OnTrip() {return onTrip;}
}
