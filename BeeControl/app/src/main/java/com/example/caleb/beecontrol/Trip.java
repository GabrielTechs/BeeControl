package com.example.caleb.beecontrol;

public class Trip {
    private String tripDate;
    private String tripTitle;
    private String tripDriverName;
    private String tripCreatedHour;
    private String tripDriverEmail;
    private String tripPartingHour;
    private String tripEntryHour;
    private String tripDescription;
    private int tripId;

    Trip(){

    }

    Trip(String tripDate, String tripTitle, String tripDriverName, String tripDriverEmail, String tripCreatedHour,  String tripDescription, int tripId){
        this.tripDate = tripDate;
        this.tripTitle = tripTitle;
        this.tripCreatedHour = tripCreatedHour;
        this.tripDriverName = tripDriverName;
        this.tripDriverEmail = tripDriverEmail;
        this.tripDescription = tripDescription;
        this.tripId = tripId;
    }

    public String getTripDate() {
        return tripDate;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public String getTripDriverName() {
        return tripDriverName;
    }

    public String getTripCreatedHour(){ return tripCreatedHour;}

    public String getTripDriverEmail() {return tripDriverEmail;}

    public int getTripId() {
        return tripId;
    }

    public String getTripPartingHour() {
        return tripPartingHour;
    }

    public String getTripEntryHour() {
        return tripEntryHour;
    }

    public String getTripDescription() {
        return tripDescription;
    }
}
