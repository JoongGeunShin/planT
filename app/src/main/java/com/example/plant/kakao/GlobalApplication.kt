package com.example.plant.kakao

import android.app.Application
import com.example.plant.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
    }
}