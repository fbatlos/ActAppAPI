package com.example.actapp.componentes_login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.actapp.ui.theme.azullogo
import com.example.actapp.ui.theme.bordeslogo


@Composable
fun BottonLogin(texto:String="Iniciar Sesión", onBotonChange:  () -> Unit, enable:Boolean){
    Spacer(Modifier.height(15.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(50.dp),

        onClick = {
            onBotonChange()
        },

        colors = ButtonDefaults.buttonColors(
            containerColor = azullogo,    // Color de fondo de nuestro boton
            contentColor = azullogo, // Color del texto
            disabledContainerColor = bordeslogo
        ),

        shape =  RoundedCornerShape(10.dp),
        enabled = enable
    ) {
        Text(
            text = texto,
            color = Color.White
        )
    }
}