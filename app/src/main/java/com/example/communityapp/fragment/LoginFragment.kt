package com.example.communityapp.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.communityapp.R
import com.example.communityapp.activity.JoinActivity
import com.example.communityapp.activity.LoginActivity
import com.example.communityapp.dto.User
import com.example.communityapp.databinding.FragmentLoginBinding


// 로그인 화면
private const val TAG = "LoginFragment_테스트"

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginActivity: LoginActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    } // End of onAttach

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.fragment_login, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
//        return inflater.inflate(R.layout.fragment_login, container, false)
    } // End of onCreateView



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 로그인 버튼 클릭
        binding.btnLogin.setOnClickListener {
//            if (binding.editTextLoginID.text!!.isNotEmpty() && binding.editTextLoginPW.text!!.isNotEmpty()) {
//                login(
//                    binding.editTextLoginID.text.toString(),
//                    binding.editTextLoginPW.text.toString()
//                )
//            } else {
//                view.showSnackBarMessage("ID 또는 패스워드를 확인해 주세요.")
//            }
            loginActivity.openFragment(1)
        }


        // 회원가입 버튼
        binding.btnJoin.setOnClickListener {
            loginActivity.openFragment(2)
        }
    } // End of onViewCreated

    // 로그인 버튼
    private fun login(loginId: String, loginPass: String) {
        val user = User(loginId, loginPass)

        // 추후 로그인 logic 추가 예정, 지금은 넘어가게만 구현
        loginActivity.openFragment(1)


//        val job2 = CoroutineScope(Dispatchers.IO).async {
//            loginViewModel.login(user)
//        }
//
//        CoroutineScope(Dispatchers.Main).launch {
//            job2.await()
//        }
    } // End of login

//    private fun observeLogin() {
//        loginViewModel.loginCheckUser.observe(viewLifecycleOwner) {
//            Log.d(TAG, "observeLogin 여기가 몇 번 동작할까요? ")
//
//            if (loginViewModel.loginCheckUser.value!!.id != binding.editTextLoginID.text.toString() || loginViewModel.loginCheckUser.value!!.pass != binding.editTextLoginPW.text.toString()) {
//                view!!.showSnackBarMessage("ID 또는 패스워드를 확인해 주세요.")
//            } else {
//                requireContext().showToastMessage("로그인 되었습니다.")
//                ApplicationClass.sharedPreferencesUtil.addUser(loginViewModel.loginCheckUser.value!!)
//                loginActivity.openFragment(1)
//            }
//        }
//    } // End of observeLogin
}
