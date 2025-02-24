package com.example.actapp.componentes_login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.actapp.ui.theme.azullogo
import com.example.actapp.ui.theme.bordeslogo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Usuario(Usuario:String, cabecero:String,onUsuarioChange: (String) -> Unit){
    OutlinedTextField(
        value = Usuario,
        onValueChange = onUsuarioChange,
        label = {
            Text(
                text = cabecero,
                color = azullogo
            )},
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = azullogo,
            unfocusedBorderColor = bordeslogo,
            cursorColor = azullogo,
            focusedLabelColor = azullogo,
            unfocusedLabelColor = bordeslogo,
            focusedTextColor = azullogo,
            unfocusedTextColor = bordeslogo
        ),
        modifier = Modifier
            .size(200.dp, 70.dp)
    )
}