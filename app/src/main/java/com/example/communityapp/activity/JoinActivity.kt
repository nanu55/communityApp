package com.example.communityapp.activity


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CursorAdapter
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.example.communityapp.R
import com.example.communityapp.databinding.ActivityJoinBinding
import com.example.communityapp.dto.User
import com.example.communityapp.util.showToastMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File

private const val TAG = "JoinActivity_테스트"

class JoinActivity : AppCompatActivity() {
    val PICK_FROM_ALBUM = 1

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    private lateinit var imageUri: Uri
    private lateinit var pathUri: String
    private var tempFile: File? = null



    private lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        auth = Firebase.auth
        database = Firebase.database
        storage = Firebase.storage



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

        if (resultCode != RESULT_OK) { // 코드가 틀릴경우
            if (tempFile != null) {
                if (tempFile!!.exists()) {
                    if (tempFile!!.delete()) {
                        Log.e(TAG, tempFile!!.getAbsolutePath() + " 삭제 성공");
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
                        this.showToastMessage("계정 생성 성공")

                        val uid = task.result.user!!.uid
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
                        Log.e(TAG, "failure", task.exception)
                    }
                }
        }
    }
}