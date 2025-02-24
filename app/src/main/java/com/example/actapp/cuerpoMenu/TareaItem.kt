package com.example.actapp.cuerpoMenu

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actapp.BaseDeDatos.Model.Tarea
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.example.actapp.BaseDeDatos.DTO.TareaInsertDTO
import com.example.actapp.BaseDeDatos.Model.AuthResponse
import com.example.actapp.Screens.LoadingOverlay
import com.example.actapp.ViewModel.MyViewModel
import com.example.actapp.ui.theme.azullogo
import insertarTareas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TareaItem(tarea: Tarea, onCheckedChange: (Boolean) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { onCheckedChange(it) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = tarea.titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = azullogo,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )

                Text(
                    text = formatDate(tarea.fecha_pub),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }

    if (showDialog) {
        TareaDialog(tarea, onCheckedChange) { showDialog = false }
    }
}

@Composable
fun TareaDialog(tarea: Tarea, onCheckedChange: (Boolean) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = tarea.completada,
                    onCheckedChange = { onCheckedChange(it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tarea.titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )
            }
        },
        text = {
            Column {
                Text(
                    text = tarea.cuerpo,
                    fontSize = 16.sp,
                    color = azullogo,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Fecha: ${formatDate(tarea.fecha_pub)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    )
}

fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(date)
}


@Composable
fun AddTareaDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    username:String,

    viewModel: MyViewModel,
    authResponse: AuthResponse
) {
    var titulo by remember { mutableStateOf("") }
    var cuerpo by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    if (isOpen) {


            viewModel.onIsLoading(false)  // ✅ Ahora se ejecuta en el hilo principal



        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Agregar Tarea")
            },
            text = {
                Column {

                    TextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Título") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = cuerpo,
                        onValueChange = { cuerpo = it },
                        label = { Text("Cuerpo de la tarea") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newTarea = TareaInsertDTO(
                            titulo = titulo,
                            cuerpo = cuerpo,
                            username = username,
                        )

                            scope.launch(Dispatchers.IO) {
                                if (newTarea != null) {
                                    viewModel.onIsLoading(true)

                                    Log.i("ESTOY EN EL INSERT", newTarea.toString())

                                    val resultado =
                                        insertarTareas(authResponse.token, newTarea).await()
                                    viewModel.onIsLoading(false)
                                    if (resultado.first == null) {

                                    } else {

                                    }
                                }
                            }

                        onDismiss()
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}
