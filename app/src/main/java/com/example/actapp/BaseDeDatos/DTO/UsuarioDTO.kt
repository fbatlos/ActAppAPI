package com.example.actapp.BaseDeDatos.DTO

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDTO(
    val username: String,
    val rol: String?
)
