package com.example.plant.main_fragment.calendar.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.plant.main_fragment.calendar.model.Event
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.model.Schedule

@Database(
    entities = [Memo::class, Schedule::class, Event::class ],
    version = 18,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val memoDao : MemoDao
    abstract val scheduleDao : ScheduleDao
    abstract val eventDao : EventDao
}