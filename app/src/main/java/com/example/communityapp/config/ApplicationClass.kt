package com.example.communityapp.config

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
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

        firebaseApp = FirebaseApp.initializeApp(application)!!
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