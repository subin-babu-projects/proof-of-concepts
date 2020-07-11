package com.example.driverestapi_kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.driverestapi_kotlin.databinding.GridItemDriveFunctionsBinding

class FunctionsListAdapter(val clickListener : GridButtonListener) : ListAdapter<DriveFunction, FunctionsListAdapter.ViewHolder>(FunctionsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

    class ViewHolder private constructor(private val binding: GridItemDriveFunctionsBinding) : RecyclerView.ViewHolder(binding.root){
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridItemDriveFunctionsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(
            item: DriveFunction,
            clickListener: GridButtonListener
        ) {
            binding.driveFunction = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}

class FunctionsDiffCallback : DiffUtil.ItemCallback<DriveFunction>(){
    override fun areItemsTheSame(oldItem: DriveFunction, newItem: DriveFunction): Boolean {
        return oldItem.function == newItem.function
    }

    override fun areContentsTheSame(oldItem: DriveFunction, newItem: DriveFunction): Boolean {
        return oldItem == newItem
    }

}