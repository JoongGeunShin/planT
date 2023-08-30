package com.example.plant.main_fragment.calendar.di

import com.example.plant.main_fragment.calendar.viewModel.EventViewModel
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import com.example.plant.main_fragment.calendar.viewModel.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val memoViewModelModule = module {
    viewModel {
        MemoViewModel(get())
    }
}

val scheduleViewModelModule = module {
    viewModel {
        ScheduleViewModel(get())
    }
}
val eventViewModelModule = module {
    viewModel {
        EventViewModel(get())
    }
}

