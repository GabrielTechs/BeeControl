package com.example.caleb.beecontrol;

public class TripNew {
    public String tripDate;
    public String tripTitle;
    public String tripDriverName;
    public int tripId;

    TripNew(){

    }

    TripNew(String tripDate, String tripTitle, String tripDriverName, int tripId){
        this.tripDate = tripDate;
        this.tripTitle = tripTitle;
        this.tripDriverName = tripDriverName;
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
}
