package com.example.caleb.beecontrol;

public class Trip {
    public String tripDate;
    public String tripTitle;
    public String tripDriverName;
    public String tripPartingHour;
    public String tripEntryHour;
    public String tripDescription;
    public int tripId;

    Trip(){

    }

    Trip(String tripDate, String tripTitle, String tripDriverName, String tripPartingHour, String tripEntryHour, String tripDescription, int tripId){
        this.tripDate = tripDate;
        this.tripTitle = tripTitle;
        this.tripDriverName = tripDriverName;
        this.tripPartingHour = tripPartingHour;
        this.tripEntryHour = tripEntryHour;
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
