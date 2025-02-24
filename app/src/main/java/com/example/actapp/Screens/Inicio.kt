package com.example.actapp.Screens

import android.widget.ProgressBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.actapp.Navegation.AppScreen
import com.example.actapp.R
import com.example.actapp.ui.theme.azullogo
import kotlinx.coroutines.delay


//Pantalla inicial simple pero le he añadido la funcionalidad de google para poder iniciar con google en algun momento.
@Composable
fun pantallaInicio(modifier: Modifier = Modifier , navController: NavController) {

    var isLoading by rememberSaveable { mutableStateOf(false) }

    var progress by rememberSaveable { mutableStateOf(0.0f) }






    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable {
                if (!isLoading) {
                    isLoading = true
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        //La imagen no es la mejor
        Image(
            painter = painterResource(R.drawable.logobmw),
            contentDescription = "Imagen de usuario",
            modifier = Modifier
                .size(150.dp)

        )

        Spacer(Modifier.padding(vertical = 20.dp))

        if (isLoading){

            LinearProgressIndicator(
                progress = progress, // Progreso manual
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(8.dp),
                color = azullogo,
                trackColor = Color.LightGray
            )

            LaunchedEffect(isLoading) {
                while (progress < 1f) {
                    progress += 0.2f
                    delay(1000)
                }
                if (progress >= 1f){
                    navController.navigate(AppScreen.pantallaLogin.router)
                }
            }
        }

    }

}