package com.example.driverestapi_kotlin

data class DriveFunction (
    val name: String,
    val function : DFunction
)

enum class DFunction{
    CREATE_FOLDER,
    DELETE_FOLDER,
    CREATE_FILE,
    DELETE_FILE
}