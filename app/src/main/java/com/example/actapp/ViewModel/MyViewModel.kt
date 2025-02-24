package com.example.actapp.ViewModel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actapp.BaseDeDatos.DTO.TareaInsertDTO
import com.example.actapp.BaseDeDatos.Model.Tarea
import insertarTareas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import obtenerTareas


class MyViewModel():ViewModel() {
    private val _textError = MutableLiveData<String>()
    val textError = _textError

    private val _tareas = MutableLiveData<List<Tarea>>()
    val tareas: LiveData<List<Tarea>> = _tareas

    private val _username = MutableLiveData<String>()
    val username:LiveData<String> = _username

    private val _email = MutableLiveData<String>()
    val email:LiveData<String> = _email

    private val _contrasenia = MutableLiveData<String>()
    val contrasenia:LiveData<String> = _contrasenia

    private val _municipio = MutableLiveData<String>()
    val municipio = _municipio

    private val _provincia = MutableLiveData<String>()
    val provincia = _provincia

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable:LiveData<Boolean> = _isLoginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> = _isLoading

    private val _isOpen = MutableLiveData<Boolean>()
    val isOpen:LiveData<Boolean> = _isOpen

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog:LiveData<Boolean> = _showDialog

    private val _errorMensage = MutableLiveData<Boolean>()
    val errorMensage:LiveData<Boolean> = _errorMensage



    fun onLogChange(username: String, contasenia:String){
        _username.value = username
        _contrasenia.value = contasenia
        _isLoginEnable.value = loginEneable(username,contasenia)
    }

    fun onRegisterChange(username: String, contasenia:String, municipio:String, provincia:String,email: String){
        _username.value = username
        _contrasenia.value = contasenia
        _municipio.value = municipio
        _provincia.value = provincia
        _email.value = email
        _isLoginEnable.value = loginEneable(username,contasenia)
    }

    fun cargarTareas(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultado = obtenerTareas(token).await()
            resultado.second?.let { lista ->
                withContext(Dispatchers.Main) {
                    _tareas.value = lista
                }
            }
        }
    }

    fun agregarTarea(token: String, tareaInsertDTO: TareaInsertDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultado = insertarTareas(token, tareaInsertDTO).await()
            if(resultado.second == null){

            }
            resultado.second?.let {
                cargarTareas(token)
            }
        }
    }

    fun onShowDialog(showDialog:Boolean ){
        _showDialog.value = showDialog
    }

    fun textErrorChange(textError:String){
        _textError.value = textError
    }

    fun onIsLoading(isLoading: Boolean){
        _isLoading.value = isLoading
    }

    fun onIsOpen(isOpen:Boolean){
        _isOpen.value = isOpen
    }


    fun loginEneable(username:String, contrasenia:String)=
        username.isNotEmpty() && contrasenia.length >= 3

}

