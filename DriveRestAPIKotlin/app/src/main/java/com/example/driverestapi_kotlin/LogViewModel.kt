package com.example.driverestapi_kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogViewModel : ViewModel(){
    private var _logString = MutableLiveData<String>()
    val logString : LiveData<String>
        get() = _logString

    fun logStatement(log:String){
        _logString.value = log
    }
}