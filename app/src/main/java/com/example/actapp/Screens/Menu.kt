import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.actapp.BaseDeDatos.API
import com.example.actapp.BaseDeDatos.DTO.TareaInsertDTO
import com.example.actapp.BaseDeDatos.DTO.UsuarioRegisterDTO
import com.example.actapp.BaseDeDatos.Model.AuthResponse
import com.example.actapp.BaseDeDatos.Model.Direccion
import com.example.actapp.BaseDeDatos.Model.Tarea
import com.example.actapp.R
import com.example.actapp.Screens.LoadingOverlay
import com.example.actapp.ViewModel.MyViewModel
import com.example.actapp.cuerpoMenu.AddTareaDialog
import com.example.actapp.cuerpoMenu.TareaItem
import com.example.actapp.ui.theme.azullogo
import com.example.actapp.ui.theme.bordeslogo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMenu(modifier: Modifier = Modifier, viewModel: MyViewModel, navController: NavController, auth: String?) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isOpen by viewModel.isOpen.observeAsState(false)
    val tareas by viewModel.tareas.observeAsState(emptyList()) // 🔥 Observa la lista de tareas en el ViewModel

    if (auth == null) throw Exception("Algo salió mal al parsear del L/R")
    val authResponse = Json.decodeFromString<AuthResponse>(auth)

    LaunchedEffect(Unit) {
        viewModel.cargarTareas(authResponse.token)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        FloatingActionButton(
                            onClick = {
                                viewModel.onIsOpen(true)
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar tarea")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Usuario", modifier = modifier.size(48.dp))
                    }
                },
            )
        }
    ) { padding ->
        if (isOpen) {
            AddTareaDialog(
                isOpen = isOpen,
                onDismiss = { viewModel.onIsOpen(false) },
                username = authResponse.user.username,
                viewModel, authResponse
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
        ) {
            LoadingOverlay(isLoading)

            if (tareas.isEmpty() && !isLoading) {
                Text("No hay tareas disponibles", color = Color.White)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tareas.size) { index ->
                        TareaItem(tarea = tareas[index], onCheckedChange = { /* TODO: PUT actualizar estado */ })
                    }
                }
            }
        }
    }
}


@Composable
fun DrawerMenuItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(azullogo)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = bordeslogo
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

fun obtenerTareas(
    token: String
): Deferred<Pair<String?, List<Tarea>?>> {
    val scope = CoroutineScope(Dispatchers.IO)

    return scope.async(Dispatchers.IO) {
        try {
            val response = API.retrofitService.getTareas(authHeader = "Bearer $token")

            if (response.isSuccessful) {
                Log.i("OBTENER TAREAAAAAAA",response.toString())
                return@async Pair(null, response.body())

            } else {
                Log.i("TAREAAAA ERROORR POST",response.toString())
                return@async Pair(API.parseError(response).message, null)
            }
        } catch (e: Exception) {
            return@async Pair(e.message, null)
        }
    }
}

fun insertarTareas(
    token: String,
    tareaInsertDTO: TareaInsertDTO
): Deferred<Pair<String?, Tarea?>> {
    val scope = CoroutineScope(Dispatchers.IO)

    return scope.async(Dispatchers.IO) {
        try {
            val response = API.retrofitService.postTareas(authHeader = "Bearer $token", tareaInsertDTO = tareaInsertDTO)

            Log.i("RESPUESTA TAREAAAAS",response.toString())

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


