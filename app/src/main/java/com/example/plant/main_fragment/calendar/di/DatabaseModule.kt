package com.example.plant.main_fragment.calendar.di

import android.app.Application
import androidx.room.Room
import com.example.plant.main_fragment.calendar.db.AppDatabase
import com.example.plant.main_fragment.calendar.db.EventDao
import com.example.plant.main_fragment.calendar.db.MemoDao
import com.example.plant.main_fragment.calendar.db.ScheduleDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Application) : AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "SCHEDULE_DB")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideMemoDao(database: AppDatabase) : MemoDao {
        return database.memoDao
    }

    fun provideDao(database: AppDatabase) : ScheduleDao {
        return database.scheduleDao
    }
    fun provideEventDao(database: AppDatabase) : EventDao {
        return database.eventDao
    }



    single {
        provideDatabase(androidApplication())
    }

    single {
        provideMemoDao(get())
    }

    single {
        provideDao(get())
    }
    single {
        provideEventDao(get())
    }


}