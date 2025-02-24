package com.example.actapp.BaseDeDatos.Model

import java.util.Date

data class Tarea(
    val _id:String?,
    var titulo: String,
    var cuerpo : String,
    var username: String,
    val fecha_pub : Date,
    var completada:Boolean = false
)
