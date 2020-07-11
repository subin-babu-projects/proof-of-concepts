package com.example.driverestapi_kotlin

class GridButtonListener(val clickListener : (functionID : DFunction) -> Unit) {
    fun onClick(driveFunction: DriveFunction) = clickListener(driveFunction.function)
}