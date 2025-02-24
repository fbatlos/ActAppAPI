package com.example.actapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.actapp.BaseDeDatos.API
import com.example.actapp.BaseDeDatos.Model.AuthResponse
import com.example.actapp.BaseDeDatos.Model.Direccion
import com.example.actapp.BaseDeDatos.DTO.UsuarioRegisterDTO
import com.example.actapp.Navegation.AppScreen
import com.example.actapp.ViewModel.MyViewModel
import com.example.actapp.componentes_login.BottonLogin
import com.example.actapp.componentes_login.Contrasenia
import com.example.actapp.componentes_login.ErrorDialog
import com.example.actapp.componentes_login.Usuario
import com.example.actapp.ui.theme.azullogo
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun registro(modifier: Modifier, viewModel: MyViewModel, navContralador: NavHostController) {
    val usuario by viewModel.username.observeAsState("")
    val contrasenia by viewModel.contrasenia.observeAsState("")
    val municipio by viewModel.municipio.observeAsState("")
    val provincia by viewModel.provincia.observeAsState("")
    val email by viewModel.email.observeAsState("")
    var contraseniaRepeat by remember { mutableStateOf("") }

    val showDialog by viewModel.showDialog.observeAsState(false)
    val textError by viewModel.textError.observeAsState("Error al iniciar.")
    val isEnable by viewModel.isLoginEnable.observeAsState(false)
    val isLoading by viewModel.isLoading.observeAsState(false)

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ErrorDialog(showDialog = showDialog, textError = textError) {
            viewModel.onShowDialog(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Usuario(usuario, "Usuario") {
            viewModel.onRegisterChange(it, contrasenia, municipio, provincia, email)
        }

        Usuario(email, "Email") {
            viewModel.onRegisterChange(usuario, contrasenia, municipio, provincia, it)
        }

        Contrasenia(contrasenia) {
            viewModel.onRegisterChange(usuario, it, municipio, provincia, email)
        }

        Contrasenia(contraseniaRepeat) { contraseniaRepeat = it }

        Usuario(municipio, "Municipio") {
            viewModel.onRegisterChange(usuario, contrasenia, it, provincia, email)
        }

        Usuario(provincia, "Provincia") {
            viewModel.onRegisterChange(usuario, contrasenia, municipio, it, email)
        }

        LoadingOverlay(isLoading)

        Spacer(modifier = Modifier.height(16.dp))

        BottonLogin(
            onBotonChange = {
                scope.launch {
                    viewModel.onIsLoading(true)
                    val registroCode = registrarUsuario(
                        usuario, email, contrasenia, contraseniaRepeat, Direccion(municipio, provincia)
                    ).await()
                    viewModel.onIsLoading(false)

                    if (registroCode.first == null && registroCode.second != null) {
                        //Ya tengo el Auth
                        navContralador.navigate(AppScreen.pantallaMenu.router + "/${
                            Json.encodeToString(registroCode.second)}")
                    } else {
                        viewModel.textErrorChange(registroCode.first ?: "Error al iniciar sesión")
                        viewModel.onShowDialog(true)
                    }
                }
            },
            enable = isEnable,
            texto = "Registrarse"
        )

        Spacer(modifier = Modifier.height(16.dp))
        Iniciarse(navContralador)
    }
}

fun registrarUsuario(
    username: String, email: String, password: String, repeatPassword: String, direccion: Direccion
): Deferred<Pair<String?, AuthResponse?>> {
    val scope = CoroutineScope(Dispatchers.IO)
    val usuarioRegistroDTO = UsuarioRegisterDTO(username, email, password, repeatPassword, direccion = direccion)

    return scope.async(Dispatchers.IO) {
        try {
            val response = API.retrofitService.postRegister(usuarioRegistroDTO)
            if (response.isSuccessful) {
                return@async Pair(null, response.body())
            } else {
                return@async Pair(API.parseError(response).message, null)
            }
        } catch (e: Exception) {
            return@async Pair(e.message, null)
        }
    }
}

@Composable
fun Iniciarse(navController: NavController) {
    Text(
        text = "Yo ya tengo cuenta.",
        color = azullogo,
        modifier = Modifier.clickable { navController.navigate(AppScreen.pantallaLogin.router) }
    )
}