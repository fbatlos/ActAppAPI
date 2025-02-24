package com.example.actapp.ViewModel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class MyViewModel():ViewModel() {
    private val _textError = MutableLiveData<String>()
    val textError = _textError

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

