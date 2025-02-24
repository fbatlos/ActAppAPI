package com.example.actapp.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.actapp.BaseDeDatos.API
import com.example.actapp.BaseDeDatos.API.retrofitService
import com.example.actapp.BaseDeDatos.DTO.UsuarioDTO
import com.example.actapp.BaseDeDatos.JwtUtils
import com.example.actapp.BaseDeDatos.DTO.UsuarioLoginDTO
import com.example.actapp.BaseDeDatos.Model.AuthResponse
import com.example.actapp.Navegation.AppScreen
import com.example.actapp.ViewModel.MyViewModel
import com.example.actapp.componentes_login.BottonLogin
import com.example.actapp.componentes_login.Contrasenia
import com.example.actapp.componentes_login.ErrorDialog
import com.example.actapp.componentes_login.Usuario
import com.example.actapp.ui.theme.azullogo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun login(modifier: Modifier, viewModel: MyViewModel, navController: NavController) {

    val Usuario by viewModel.username.observeAsState("")
    val Contrasenia by viewModel.contrasenia.observeAsState("")
    val IsEnable by viewModel.isLoginEnable.observeAsState(false)
    val textError by viewModel.textError.observeAsState("Error al iniciar.")
    val scope = rememberCoroutineScope()
    val showDialog by viewModel.showDialog.observeAsState(false)

    val focusManager = LocalFocusManager.current

    val isLoading by viewModel.isLoading.observeAsState(false)

    Column(
        modifier =modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        ErrorDialog(showDialog = showDialog, textError = textError){
            viewModel.onShowDialog(it)
        }

        LoadingOverlay(isLoading)


        Usuario(Usuario, cabecero = "Usuario"){
            viewModel.onLogChange(username = it , contasenia = Contrasenia)
        }

        Contrasenia(Contrasenia){
            viewModel.onLogChange(username = Usuario , contasenia = it)
        }

        BottonLogin(
            onBotonChange = {
                viewModel.onIsLoading(true)
                scope.launch {
                    val login = validarUsuario(viewModel.username.value ?: "", viewModel.contrasenia.value ?: "").await()

                    viewModel.onIsLoading(false)
                    if (login.first) {
                        val auth = AuthResponse(login.second,UsuarioDTO(Usuario,JwtUtils.getRoleFromToken(token = login.second)))

                        navController.navigate(AppScreen.pantallaMenu.router + "/${Json.encodeToString(auth)}")
                    }
                    else {
                        Log.i("Token Trans",login.second)
                        viewModel.textErrorChange(login.second)
                        viewModel.onShowDialog(true)
                        viewModel.onLogChange(username = "", contasenia = "")
                    }
                }
            },
            enable = IsEnable
        )

        Spacer(Modifier.height(9.dp))

        Resgistrarse(navController)
        
    }
}

@Composable
fun LoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)), // Fondo semitransparente
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun Resgistrarse(navController: NavController){
    Text(
        text = "No tengo cuenta." ,
        color = azullogo,
        modifier = Modifier.clickable {
            navController.navigate(AppScreen.pantallaRegistro.router)
        }

    )
}


//Mirar muy bien lo de las corrutinas que es muy lioso y no sabes como funciona bien
fun validarUsuario(username: String, password: String): Deferred<Pair<Boolean,String>> {
    val scope = CoroutineScope(Dispatchers.IO)
    val usuarioLoginDTO = UsuarioLoginDTO(username = username, password = password)

    return scope.async(Dispatchers.IO) {
        try {
            val response = retrofitService.postLogin(usuarioLoginDTO)
            Log.i("RESPUESTA", response.toString())
            if (response.isSuccessful) {
                val tokenbd = response.body()?.token ?: ""
                Log.i("Token", tokenbd)
                return@async Pair(true,tokenbd)
            } else {
                val error = API.parseError(response)
                Log.e("Error controladoooo", "$error")
                return@async Pair(false,error.message)
            }
        } catch (e: Exception) {
            Log.e("Error", "Error cargando el usuario", e)
            return@async Pair(false,e.toString())
        }
    }
}
