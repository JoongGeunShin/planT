package com.example.plant.main_fragment.calendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo") // 메모 테이블
data class Memo(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int, // 일련번호
    val title: String, // 가게 이름
    val category: String,// 가게 분류
    val roadaddress: String, // 가게 도로명 주소
    val completion: Boolean // 체크 유무
)