package com.example.communityapp.config

import android.Manifest
import android.app.Application
import android.util.Log
import com.example.communityapp.util.showToastMessage
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

private const val TAG = "ApplicationClass_테스트"
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "oncreate")
        FirebaseManager.initialize(this)
    }
}
object FirebaseManager {
    private lateinit var application: Application
    lateinit var firebaseApp: FirebaseApp
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage


    fun initialize(app: Application) {
        application = app
        // context 사용 가능
        Log.i(TAG, "후")
        // 파이어베이스 초기화
        firebaseApp = FirebaseApp.initializeApp(application)!!
        // ...
        auth = getFirebaseAuth()
        database = getFirebaseDatabase()
        storage = getFirebaseStorage()
    }

    fun getFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance(firebaseApp)
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance(firebaseApp)
    }

    fun getFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance(firebaseApp)
    }
}