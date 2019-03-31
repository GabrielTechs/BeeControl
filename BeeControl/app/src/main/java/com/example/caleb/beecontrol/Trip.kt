package com.example.caleb.beecontrol

import java.util.Date

abstract class Trip {

    lateinit var tripDate: String
    lateinit var tripTitle: String
    lateinit var truckDriverName: String
    lateinit var tripPartingHour: String

    internal constructor() {

    }

    internal constructor(tripDate: String, tripTitle: String, employeeName: String, tripPartingHour: String, returnHour: Date) {
        this.tripDate = tripDate
        this.tripTitle = tripTitle
        this.truckDriverName = employeeName
        this.tripPartingHour = tripPartingHour
    }
}
