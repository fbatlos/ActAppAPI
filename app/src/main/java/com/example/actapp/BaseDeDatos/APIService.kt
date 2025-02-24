package com.example.actapp.BaseDeDatos

import com.example.actapp.BaseDeDatos.DTO.TareaInsertDTO
import com.example.actapp.BaseDeDatos.Model.AuthResponse
import com.example.actapp.BaseDeDatos.Model.Token
import com.example.actapp.BaseDeDatos.DTO.UsuarioLoginDTO
import com.example.actapp.BaseDeDatos.DTO.UsuarioRegisterDTO
import com.example.actapp.BaseDeDatos.Model.Tarea
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface APIService {

    @POST("usuarios/login")
    suspend fun postLogin(@Body usuarioLoginDTO: UsuarioLoginDTO):Response<Token>


    @POST("usuarios/register")
    suspend fun postRegister(@Body usuario: UsuarioRegisterDTO):Response<AuthResponse>

    @GET("tareas/tareas")
    suspend fun getTareas(@Header("Authorization") authHeader: String): Response<List<Tarea>>

    @POST("tareas/tarea")
    suspend fun postTareas(@Header("Authorization") authHeader: String, @Body tareaInsertDTO: TareaInsertDTO): Response<Tarea>

    @PUT("tareas/tarea/{id}")
    suspend fun putTareas(@Header("Authorization") authHeader: String,@Path("id") id:String ,@Body tarea: Tarea): Response<Tarea>
}