package com.example.plant.main_fragment.calendar.db

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.plant.main_fragment.calendar.model.Event

@Dao
interface EventDao { // 특정 날짜 저장용 테이블 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 날짜는 중복되지 않게 저장
    fun addDate(item: Event)

    @Query("DELETE FROM event WHERE date = :date") // 날짜로 삭제
    fun deleteDate(date : String)

    @Query("SELECT * FROM event")
    fun getAllDates() : LiveData<List<Event>>
}