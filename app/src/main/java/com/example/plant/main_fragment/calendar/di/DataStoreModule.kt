package com.example.plant.main_fragment.calendar.di

import com.example.plant.DateSaveModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataStoreModule = module {
    single {
        DateSaveModule(androidApplication())
    }
}