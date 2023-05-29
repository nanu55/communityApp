package com.example.communityapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.example.communityapp.R
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.ActivityJoinBinding
import com.example.communityapp.dto.User
import com.example.communityapp.util.showToastMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class JoinActivity : AppCompatActivity() {
    private val PICK_FROM_ALBUM = 1

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    private lateinit var imageUri: Uri
    private var pathUri: String? = null
    private var tempFile: File? = null

    private lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        auth = FirebaseManager.auth
        database = FirebaseManager.database
        storage = FirebaseManager.storage

        binding.profileIv.setOnClickListener {
            goToAlbum()
        }

        binding.signUpBtn.setOnClickListener {
            createAccount(binding.signUpIdEdt.text.toString(), binding.signUpPassEdt.text.toString())
        }
    }

    private fun goToAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            if (tempFile != null) {
                if (tempFile!!.exists()) {
                    if (tempFile!!.delete()) {
                        tempFile = null
                    }
                }
            }
            return;
        }

        when(requestCode) {
            PICK_FROM_ALBUM -> {
                //Uri
                imageUri = data?.data!!
                pathUri = getPath(imageUri)

                binding.profileIv.setImageURI(imageUri)
            }
        }
    }

    private fun getPath(uri: Uri) : String{
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursorLoader = CursorLoader(this, uri, proj, null, null, null)

        var cursor = cursorLoader.loadInBackground()
        var index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        cursor!!.moveToFirst()
        return cursor.getString(index!!)
    }

    private fun createAccount(email: String, password: String) {
        val name = binding.nameEdt.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        this.showToastMessage("アカウント作成に成功しました")

                        val uid = task.result.user!!.uid
                        if(pathUri == null) {

                        }
                        val file = Uri.fromFile(File(pathUri))

                        val storageReference = storage.reference.child("usersprofileImages").child("uid/" + file.lastPathSegment)
                        storageReference.putFile(imageUri).addOnCompleteListener { task ->
                            val imageUrl: Task<Uri> = task.result?.storage?.downloadUrl!!
                            while (!imageUrl.isComplete);

                            val user = User().apply {
                                this.userName = name
                                this.uid = uid
                                this.profileImageUrl = imageUrl.result.toString()
                            }

                            // save to database
                            database.reference.child("users").child(uid)
                                .setValue(user)
                        }
                        finish()
                    } else {
                        this.showToastMessage(task.exception.toString())
                    }
                }
        } else {
            this.showToastMessage("全ての項目を入力してください")
        }
    }
}