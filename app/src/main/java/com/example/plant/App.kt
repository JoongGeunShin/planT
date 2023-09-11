package com.example.plant

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.plant.main_fragment.calendar.di.dataStoreModule
import com.example.plant.main_fragment.calendar.di.databaseModule
import com.example.plant.main_fragment.calendar.di.eventViewModelModule
import com.example.plant.main_fragment.calendar.di.memoViewModelModule
import com.example.plant.main_fragment.calendar.di.scheduleViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.inject

class App : Application() {

    companion object{
        private lateinit var app : App
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        startKoin {
            androidContext(this@App)
            modules(
                dataStoreModule,
                databaseModule,
                memoViewModelModule,
                scheduleViewModelModule,
                eventViewModelModule
            )
        }
    }
}