package com.example.caleb.beecontrol

import java.util.Date

class Trip {

    lateinit var tripDate: String
    lateinit var tripTitle: String
    lateinit var tripDriverName: String
    var tripId: Int = 0

    internal constructor() {

    }

    internal constructor(tripDate: String, tripTitle: String, tripDriverName: String, tripId: Int) {
        this.tripDate = tripDate
        this.tripTitle = tripTitle
        this.tripDriverName = tripDriverName
        this.tripId = tripId
    }
}
