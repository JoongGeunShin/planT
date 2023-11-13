package com.example.plant.intro_fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.plant.IntroActivity
import com.example.plant.MainActivity
import com.example.plant.R
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import android.content.Intent
import androidx.core.widget.addTextChangedListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plant.BuildConfig
import com.example.plant.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause

class LoginFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var introActivity: IntroActivity
    private lateinit var auth: FirebaseAuth
    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 2. Context를 Activity로 형변환하여 할당
        introActivity = context as IntroActivity
        KakaoSdk.init(requireContext(), BuildConfig.KAKAO_NATIVE_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.loginBtn.isEnabled = false
        //로그인 버튼 비활성화 (이메일, 페스워드 입력해야 활성화)
        detectEmailAndPasswordEmpty()
        //로그인 버튼 활성화 함수
        navController = findNavController()

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Toast.makeText(introActivity, "카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(introActivity, MainActivity::class.java)
                startActivity(intent)

            }
        }
        //회원가입 버튼 )
        binding.registerationBtn.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerationFragment)
        }
        //이메일 로그인 버튼 기능
        binding.loginBtn.setOnClickListener {
            val email = binding.emailAddressEdt.text.toString()
            val password = binding.passwordEdt.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
                        movePage(task.result?.user)
                    } else {
                        Toast.makeText(requireContext(), "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        //카카오 로그인 버튼
        binding.kakaoLoginBtn.setOnClickListener{
            kakaoLogin()
        }
//        binding.kakaoLoginBtn.setOnClickListener {
//            Toast.makeText(introActivity, "카카오 로그인 버튼", Toast.LENGTH_SHORT).show()
//            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
//                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
//                    if (error != null) {
////                        Log.e(TAG, "카카오톡으로 로그인 실패", error)
//                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
//                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
//                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                            return@loginWithKakaoTalk
//                        }
//
//                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
//                        UserApiClient.instance.loginWithKakaoAccount(
//                            requireContext(),
//                            callback = callback
//                        )
//                    } else if (token != null) {
//                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
//
//                    }
//                }
//            } else {
//                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
//            }
//        }
        // 임시버튼
        val passMainBtn = view.findViewById<Button>(R.id.passMainBtn)
        passMainBtn.setOnClickListener {
            val intent = Intent(introActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //메인 액티비티로 넘겨주는 함수
    private fun movePage(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(introActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //로그인 버튼 활성화  (이메일, 비밀번호 비어있지 않을때)
    private fun detectEmailAndPasswordEmpty() {

        binding.emailAddressEdt.addTextChangedListener {
            val email = binding.emailAddressEdt.text.toString()
            val password = binding.passwordEdt.text.toString()
            var enalbed = email.isNotEmpty() && password.isNotEmpty()
            binding.loginBtn.isEnabled = enalbed
        }

        binding.passwordEdt.addTextChangedListener {
            val email = binding.emailAddressEdt.text.toString()
            val password = binding.passwordEdt.text.toString()
            var enalbed = email.isNotEmpty() && password.isNotEmpty()
            binding.loginBtn.isEnabled = enalbed
        }
    }

    //카카오 로그인
    private fun kakaoLogin() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(requireContext(), "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            } else if (tokenInfo != null) {
                Toast.makeText(requireContext(), "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(introActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Log.d("[카카오로그인]", "접근이 거부 됨(동의 취소)")
                    }

                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Log.d("[카카오로그인]", "유효하지 않은 앱")
                    }

                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Log.d("[카카오로그인]", "인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }

                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Log.d("[카카오로그인]", "요청 파라미터 오류")
                    }

                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Log.d("[카카오로그인]", "유효하지 않은 scope ID")
                    }

                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Log.d("[카카오로그인]", "설정이 올바르지 않음(android key hash)")
                    }

                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Log.d("[카카오로그인]", "서버 내부 에러")
                    }

                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Log.d("[카카오로그인]", "앱이 요청 권한이 없음")
                    }

                    else -> { // Unknown
                        Log.d("[카카오로그인]", "기타 에러")
                    }
                }
            } else if (token != null) {
                Log.d("[카카오로그인]", "로그인에 성공하였습니다.\n${token.accessToken}")
                val intent = Intent(introActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                Log.d("카카오로그인", "토큰==null error==null")
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    //카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        requireContext(),
                        callback = callback
                    )
                } else if (token != null) {
                    Toast.makeText(requireContext(), "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
                    val intent = Intent(introActivity, MainActivity::class.java)
                    startActivity(intent)
                }

            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }


    }
}


