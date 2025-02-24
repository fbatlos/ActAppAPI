package com.example.actapp.componentes_login

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.actapp.ui.theme.azullogo
import com.example.actapp.ui.theme.bordeslogo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contrasenia(contrasenia: String, onContraseniaChange: (String) -> Unit){
    var active by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = contrasenia,
        onValueChange = onContraseniaChange,
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
        label = {
            Text(
                text = "Contraseña" ,
                color = azullogo
            )},

        trailingIcon = {
            if (contrasenia.isNotEmpty()) {
                IconButton(onClick = { active=!active }) {
                    Icon(if (active) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "Ver texto" , tint = bordeslogo)
                }
            }
        },
        modifier = Modifier
            .size(200.dp, 70.dp),

        visualTransformation = if (!active) PasswordVisualTransformation() else VisualTransformation.None

    )
}