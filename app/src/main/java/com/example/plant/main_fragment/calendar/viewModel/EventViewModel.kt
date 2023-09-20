package com.example.plant.main_fragment.calendar.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.plant.main_fragment.calendar.db.EventDao
import com.example.plant.main_fragment.calendar.model.Event

class EventViewModel(
    private val eDao : EventDao
): ViewModel() {

    fun getAllDates() : LiveData<List<Event>> = eDao.getAllDates()

    fun addDate(data : Event){ // 날짜 추가
        eDao.addDate(data)
    }

    fun deleteDate(date : String){ // 날짜 삭제
        eDao.deleteDate(date)
    }
}