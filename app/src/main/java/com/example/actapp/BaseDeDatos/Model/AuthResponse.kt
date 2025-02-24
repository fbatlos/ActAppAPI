package com.example.actapp.BaseDeDatos.Model

import com.example.actapp.BaseDeDatos.DTO.UsuarioDTO
import com.example.actapp.BaseDeDatos.DTO.UsuarioLoginDTO
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val user: UsuarioDTO
)
