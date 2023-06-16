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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.plant.IntroActivity
import com.example.plant.MainActivity
import com.example.plant.R
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
import android.content.Intent

class LoginFragment : Fragment() {

    lateinit var navController : NavController
    lateinit var introActivity : IntroActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 Activity로 형변환하여 할당
        introActivity = context as IntroActivity
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

        navController = findNavController()

        val registerationButton = view.findViewById<Button>(R.id.registerationBtn)
        registerationButton.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerationFragment)
        }

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Toast.makeText(introActivity,"카카오 로그인 성공",Toast.LENGTH_SHORT).show()
                val intent = Intent(introActivity,MainActivity::class.java)
                startActivity(intent)

            }
        }

        val kakaoLoginButton = view.findViewById<ImageButton>(R.id.kakaoLoginBtn)
        kakaoLoginButton.setOnClickListener{
            Toast.makeText(introActivity,"카카오 로그인 버튼",Toast.LENGTH_SHORT).show()
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
//                        Log.e(TAG, "카카오톡으로 로그인 실패", error)
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }

    }

}


