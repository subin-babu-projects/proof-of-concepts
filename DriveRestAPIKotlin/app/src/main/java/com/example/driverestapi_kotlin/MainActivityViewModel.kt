package com.example.driverestapi_kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val _functions = MutableLiveData<List<DriveFunction>>()
    val functions: LiveData<List<DriveFunction>>
        get() = _functions

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        initFunctionList()
    }

    private fun initFunctionList() {
        coroutineScope.launch {
            val functionList: List<DriveFunction> = listOf(
                DriveFunction("Create Folder", DFunction.CREATE_FOLDER),
                DriveFunction("Delete Folder", DFunction.DELETE_FOLDER),
                DriveFunction("Create File", DFunction.CREATE_FILE),
                DriveFunction("Delete File", DFunction.DELETE_FILE)
            )
            _functions.value = functionList
        }
    }
}