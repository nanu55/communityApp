package com.example.communityapp.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.communityapp.R
import com.example.communityapp.activity.LoginActivity
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.FragmentMypageBinding
import com.example.communityapp.dto.Comment
import com.example.communityapp.dto.Post
import com.example.communityapp.dto.User
import com.example.communityapp.ui.viewmodel.MypageViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.io.File

private const val TAG = "MypageFragment_테스트"
class MypageFragment : Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private val model: MypageViewModel by activityViewModels()
    val PICK_FROM_ALBUM = 1
    private lateinit var imageUri: Uri
    private lateinit var pathUri: String
    private var tempFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        binding.apply {
            viewModel = model
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameEdt.visibility = View.GONE
        binding.nameSaveBtn.visibility = View.GONE
        binding.nameEdtBtn.setOnClickListener {
            binding.nameTv.visibility = View.GONE
            binding.nameEdt.visibility = View.VISIBLE
            binding.nameSaveBtn.visibility = View.VISIBLE
            binding.nameEdtBtn.visibility = View.GONE
        }

        binding.nameSaveBtn.setOnClickListener {
            binding.nameTv.visibility = View.VISIBLE
            binding.nameEdt.visibility = View.GONE
            binding.nameSaveBtn.visibility = View.GONE
            binding.nameEdtBtn.visibility = View.VISIBLE

            binding.viewModel?.nameChange()

            updateUserProfile()
        }

        binding.myPageProfileImageEdtBtn.setOnClickListener {
            goToAlbum()
        }

        // sign out
        binding.signOutBtn.setOnClickListener {
            FirebaseManager.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent)
        }

        binding.nameTv.text = model.user.value?.userName

        activity?.let {
            Glide.with(it)
                .load(model.user.value?.profileImageUrl.toString())
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_error)
                .into(binding.myPageProfileImageIv)

        }
    }

    private fun updatePosts() {
        val postsQuery = FirebaseManager.database.reference.child("posts")
        postsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let {
                        if(it.user!!.uid == model.user.value!!.uid) {
                            val updatedPost = it.copy(user = User(model.user.value!!.uid, model.user.value!!.userName, model.user.value!!.uid))
                            postSnapshot.ref.setValue(updatedPost)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 쿼리 중단 시 처리
            }
        })
    }

    private fun updateComments() {
        val commentsQuery = FirebaseManager.database.reference.child("comments")
        commentsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (commentSnapshot in snapshot.children) {
                    val comment = commentSnapshot.getValue(Comment::class.java)
                    comment?.let {
                        if(it.user!!.uid == model.user.value!!.uid) {
                            val updatedComment = it.copy(user = User(model.user.value!!.uid, model.user.value!!.userName, model.user.value!!.uid))
                            commentSnapshot.ref.setValue(updatedComment)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 쿼리 중단 시 처리
            }
        })
    }

    // 작성자 프로필 업데이트 시 해당 게시물의 작성자 정보 업데이트
    private fun updateUserProfile() {
        val userRef = FirebaseManager.database.reference.child("users").child(model.user.value!!.uid)

        // 사용자 프로필 업데이트
        userRef.child("userName").setValue(model.user.value!!.userName)
        updatePosts()
        updateComments()
    }
    private fun updateUserProfileImage() {
        val userRef = FirebaseManager.database.reference.child("users").child(model.user.value!!.uid)
        val file = Uri.fromFile(File(pathUri))


        val storageReference = FirebaseManager.storage.reference.child("usersprofileImages").child("uid/" + file.lastPathSegment)
        storageReference.putFile(imageUri).addOnCompleteListener { task ->
            val imageUrl: Task<Uri> = task.result?.storage?.downloadUrl!!
            while (!imageUrl.isComplete);
            userRef.child("profileImageUrl").setValue(imageUrl.result.toString())
            model.imageUrlChange(imageUrl.result.toString())
        }

        updatePosts()
        updateComments()
    }

    private fun goToAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(TAG, "requestCode : " + requestCode)
        if (resultCode != AppCompatActivity.RESULT_OK) { // 코드가 틀릴경우
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

                binding.myPageProfileImageIv.setImageURI(imageUri)
                updateUserProfileImage()
            }
        }
    }

    private fun getPath(uri: Uri) : String{
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursorLoader = CursorLoader(requireActivity(), uri, proj, null, null, null)

        var cursor = cursorLoader.loadInBackground()
        var index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        cursor!!.moveToFirst()
        return cursor.getString(index!!)
    }
}