package com.example.actapp.Navegation

sealed class AppScreen (val router:String){

    object pantallaMenu:AppScreen("pantallaMenu")

    object pantallaLogin:AppScreen("pantallaLogin")

    object pantallaRegistro:AppScreen("pantallaRegistro")

}