package com.example.driverestapi_kotlin

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("driveFunctionName")
fun TextView.setFunctionName(item: DriveFunction){
    text = item.name
}