package com.example.plant.main_fragment.calendar.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_NO_LOCALIZED_COLLATORS
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plant.DateSaveModule
import com.example.plant.MainActivity
import com.example.plant.R
import com.example.plant.databinding.FragmentBottomnviCalendarBinding
import com.example.plant.main_fragment.calendar.adapter.ScheduleAdapter
import com.example.plant.main_fragment.calendar.model.Schedule
import com.example.plant.main_fragment.calendar.viewModel.EventViewModel
import com.example.plant.main_fragment.calendar.viewModel.ScheduleViewModel
import com.google.android.play.core.integrity.v
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Collections

class CalendarFragment : Fragment(R.layout.fragment_bottomnvi_calendar), View.OnClickListener {

    private val binding by viewBinding(FragmentBottomnviCalendarBinding::bind,
        onViewDestroyed = { binding ->
            binding.scheduleListview.adapter = null // free view binding
        })

    lateinit var mainActivity: MainActivity
    private lateinit var selectedDate: String // 달력에서 선택한 날짜
    private val scheduleAdapter by lazy { ScheduleAdapter() }
    private val dateSaveModule: DateSaveModule by inject() // 날짜를 저장하는 DataStore
    private val scheduleViewModel: ScheduleViewModel by viewModel()
    private val eventViewModel: EventViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddschedule.setOnClickListener(this)

        binding.calendarView.selectedDate = CalendarDay.today() // 오늘 날짜 출력
        binding.calendarView.addDecorators(SaturdayDecorator(), SundayDecorator()) // 주말 강조

        var year = binding.calendarView.selectedDate!!.year
        var month = binding.calendarView.selectedDate!!.month + 1
        var day = binding.calendarView.selectedDate!!.day
        selectedDate = "$year-$month-$day"

        lifecycleScope.launch {
            dateSaveModule.setDate(selectedDate)
        }


        // 달력 - 날짜 선택 Listener
        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            year = date.year
            month = date.month + 1
            day = date.day
            selectedDate = "$year-$month-$day"
            lifecycleScope.launch {
                dateSaveModule.setDate(selectedDate)
            }
            callList()
        }
        //조그마한 점을 찍어주는 event 수정이 필요함 (일정이있을때)
        eventViewModel.getAllDates()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
                for (i in list.indices) {
                    val eventDate = list[i].date.split("-")
                    val year = Integer.parseInt(eventDate[0])
                    val month = Integer.parseInt(eventDate[1])
                    val day = Integer.parseInt(eventDate[2])
                    binding.calendarView
                        .addDecorator(
                            EventDecorator(
                                Color.parseColor("#0E406B"),
                                Collections.singleton(
                                    CalendarDay.from(
                                        year,
                                        month - 1,
                                        day
                                    )
                                )
                            )
                        )
                }
            })

        //rc item click -> open menudialog
        scheduleAdapter.itemClick = object : ScheduleAdapter.ItemClick {
            override fun onClick(view: View, position: Int, list: ArrayList<Schedule>) {
                val dialog = MenuDialogFragment().apply {
                    serialNum = list[position].serialNum
                    selectedDate = list[position].date
                    size = list.size
                }
                activity?.let {
                    dialog.show(it.supportFragmentManager, "MenuDialogFragment")
                }
            }
        }
        callList()
    }


    private fun callList() {
        scheduleViewModel.getAllSchedule(selectedDate)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
                scheduleAdapter.removeAll()
                if (list.isEmpty()) { // 해당 날짜에 목록이 없을 때 "이벤트 없음" 표시
                    binding.emptyText.visibility = View.VISIBLE
                } else {
                    binding.emptyText.visibility = View.GONE
                    scheduleAdapter.list = list as ArrayList<Schedule>
                }
                binding.scheduleListview.apply {
                    adapter = scheduleAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_addschedule -> { // open AddDialog
                val dialog = AddDialogFragment()
                dialog.show(parentFragmentManager, "AddScheduleDialog")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.calendarView.selectedDate = CalendarDay.today()
        Log.e(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        binding.calendarView.selectedDate = CalendarDay.today()
        Log.e(TAG, "onResume()")
    }

    companion object {
        const val TAG = "CalendarFragment"
    }
}