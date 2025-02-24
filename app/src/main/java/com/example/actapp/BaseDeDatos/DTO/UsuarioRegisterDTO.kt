package com.example.actapp.BaseDeDatos.DTO

import com.example.actapp.BaseDeDatos.Model.Direccion

data class UsuarioRegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val rol: String? = "USER",
    val direccion: Direccion
)