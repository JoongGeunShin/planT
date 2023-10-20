package com.example.plant.main_fragment.calendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule") // 일정 테이블
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    var serialNum : Int = 0, // 일련번호
    var date : String, // 날짜(년/월/일)
    var content : String, // 제목
    var start_hour : String,
    var start_minute : String,
    var end_hour : String,
    var end_minute : String,
    var start_end_time :String = "시작시간: ${start_hour}:${start_minute} ~ 종료시간: ${end_hour}:$end_minute"
)