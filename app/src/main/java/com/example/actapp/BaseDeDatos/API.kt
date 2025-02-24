package com.example.actapp.BaseDeDatos

import android.util.Log
import com.example.actapp.BaseDeDatos.ErrorAPI.ApiError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT

object API {
    private const val BASE_URL = "https://app-adat-v19x.onrender.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val retrofitService : APIService by lazy {
        retrofit.create(APIService::class.java)
    }


    fun parseError(response: Response<*>): ApiError {
        val converter: Converter<ResponseBody, ApiError> = retrofit.responseBodyConverter(ApiError::class.java, arrayOfNulls(0))
        return try {
            //el mensaje vuelve nulo ?????
            converter.convert(response.errorBody()!!) ?: ApiError("Unknown error")
        } catch (e: IOException) {
            ApiError("Error parsing error response" )
        }
    }
}



object JwtUtils {
    fun getRoleFromToken(token: String): String {
        val decoded: DecodedJWT = JWT.decode(token)
        return decoded.getClaim("roles").asString() ?: "user"
    }

}
