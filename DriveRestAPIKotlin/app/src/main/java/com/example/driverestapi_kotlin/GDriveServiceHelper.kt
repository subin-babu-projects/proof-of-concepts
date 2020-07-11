package com.example.driverestapi_kotlin

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import java.io.IOException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.math.log

class GDriveServiceHelper(private val driveService : Drive, private val logViewModel: LogViewModel) {

    private val _TAG = "GDriveServiceHelper"
    private val mExecutor = Executors.newSingleThreadExecutor()

    fun createFolderOnDrive(folderName: String) {
        queryFolder().addOnSuccessListener {
            if (it.files.size == 0) {
                createFolder(folderName).addOnSuccessListener { folderID ->
                    Log.e(_TAG, "Folder created : $folderID")
                    logViewModel.logStatement("Folder created : $folderID")
                }
            } else {
                Log.d(_TAG, "1 too many Folders")
                logViewModel.logStatement( "1 too many Folders")
            }
        }.addOnFailureListener { exception ->
            Log.e(_TAG, "Folder Query Failure : $exception")
            logViewModel.logStatement( "Folder Query Failure : $exception")
        }
    }

    fun createFileOnDrive(fileName : String){

    }

    private fun isFolderReady(folderName: String) : Task<Boolean>{
        return Tasks.call(mExecutor, Callable {
            queryFolder().isSuccessful && queryFolder().result?.files?.size == 1
        })
    }

    private fun queryFolder(): Task<FileList> {
        return Tasks.call(mExecutor, Callable {
            driveService.files().list()
                .setSpaces("drive")
                .setQ("mimeType='application/vnd.google-apps.folder'")
                .execute()
        })
    }


    private fun createFolder(folderName : String):Task<String>{
        return Tasks.call(mExecutor, Callable {
            val folderMetaData = File().setName(folderName).setMimeType("application/vnd.google-apps.folder")
            val folder = driveService.files().create(folderMetaData).setFields("id").execute()
                ?: throw IOException("Null result when requesting folder creation")
            folder.id
        })
    }

    fun deleteFolderInDrive(folderName:String){
        driveService.files().emptyTrash()
        queryFolder().addOnSuccessListener {
            if (it.files.size == 0) {
                Log.e(_TAG,"Folder doesn't exist. Nothing to delete")
                logViewModel.logStatement("Folder doesn't exist. Nothing to delete")
            }else{
                for (file in it.files){
                    Tasks.call(mExecutor, Callable { driveService.files().delete(file.id).execute() })
                    Log.e(_TAG,"Folder with id : ${file.id} deleted")
                    logViewModel.logStatement("Folder with id : ${file.id} deleted")
                }
            }
        }
    }
}