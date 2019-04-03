package com.example.caleb.beecontrol

import java.util.Date

abstract class Assitance{

    lateinit var usuario: String
    lateinit var condicion: String
    lateinit var fecha: String

    internal constructor(){

    }

    internal constructor(usuario: String, condicion: String, fecha: String, tripPartingHour: String, returnHour: Date) {
        this.usuario = usuario
        this.condicion = condicion
        this.fecha = fecha

    }

}