package com.example.driverestapi_kotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.driverestapi_kotlin.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val REQUEST_CODE_SIGN_IN = 1
    private val REQUEST_CODE_OPEN_DOCUMENT = 2

    private lateinit var mDriveServiceHelper : GDriveServiceHelper

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private val logViewModel : LogViewModel by lazy {
        ViewModelProvider(this).get(LogViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainActivityViewModel = mainActivityViewModel
        binding.logViewModel = logViewModel

        val adapter = FunctionsListAdapter(
            GridButtonListener { functionID ->
                handleClick(functionID)
            })
        val manager = GridLayoutManager(this, 2)
        binding.gridView.layoutManager = manager
        binding.gridView.adapter = adapter


        mainActivityViewModel.functions.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.executePendingBindings()
        requestSignIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_SIGN_IN -> {
                if(resultCode == Activity.RESULT_OK && data!=null)
                    handleSignInResult(data)
            }
            REQUEST_CODE_OPEN_DOCUMENT ->{

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
            .addOnSuccessListener{ googleAccount ->
                run {
                    Log.d(TAG, "Signed in as ${googleAccount.email}")

                    val credential = GoogleAccountCredential.usingOAuth2(
                        this,
                        Collections.singleton(DriveScopes.DRIVE_FILE)
                    )
                    credential.setSelectedAccount(googleAccount.account)
                    val googleDriveService = Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        GsonFactory(),
                        credential
                    ).setApplicationName("Drive API Migration - Kotlin").build()

                    mDriveServiceHelper = GDriveServiceHelper(googleDriveService,logViewModel)
                }
            }.addOnFailureListener{ exception -> Log.e(TAG,"Unable to sign in : $exception") }
    }

    private fun handleClick(functionID : DFunction){
        logViewModel.logStatement("Option clicked $functionID")
        when(functionID){
            DFunction.CREATE_FOLDER -> createFolder("test-folder-kt")
            DFunction.DELETE_FOLDER -> deleteFolder("test-folder-kt")
        }
    }

    private fun createFolder(folderName: String) {
        mDriveServiceHelper.createFolderOnDrive(folderName)
    }

    private fun deleteFolder(folderName: String){
        mDriveServiceHelper.deleteFolderInDrive(folderName)
    }

    private fun createFile(fileName : String){
        mDriveServiceHelper.createFileOnDrive(fileName)
    }

    private fun requestSignIn(){
        Log.d(TAG, "Requesting sign-in")
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        val client = GoogleSignIn.getClient(this,signInOptions)

        startActivityForResult(client.signInIntent, REQUEST_CODE_SIGN_IN)
    }
}