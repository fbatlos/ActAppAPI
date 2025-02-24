package com.example.act1_pdm.Navegation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.actapp.Screens.pantallaInicio
import PantallaMenu
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.actapp.Navegation.AppScreen
import com.example.actapp.Screens.login
import com.example.actapp.Screens.registro
import com.example.actapp.ViewModel.MyViewModel


@SuppressLint("RestrictedApi")
@Composable
fun AppNavegation(modifier: Modifier, viewModel: MyViewModel){
    val navContralador = rememberNavController()

    NavHost(
        navController = navContralador,
        startDestination = AppScreen.pantallaLogin.router
    ){
        composable (AppScreen.pantallaMenu.router + "/{text}" , arguments = listOf(navArgument(name = "text"){
            type = NavType.StringType
        }))
        {
            PantallaMenu(modifier = modifier,viewModel,navContralador , auth = it?.arguments?.getString("text") )
        }

        composable (AppScreen.pantallaLogin.router){
            login(modifier,viewModel,navContralador)
        }

        composable (AppScreen.pantallaRegistro.router){
            registro(modifier,viewModel,navContralador)
        }
    }
}