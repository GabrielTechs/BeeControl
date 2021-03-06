package com.example.caleb.beecontrol;

public class Employee {

    public String name;
    public String lastName;
    public String email;
    public String role;
    public boolean noCell;

    Employee(){

    }

    Employee(String name, String lastName, String email, String role, boolean noCell){
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.noCell = noCell;
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

    public String getRole() {
        return role;
    }

    public boolean isNoCell() {
        return noCell;
    }
}
