package com.example.plant.main_fragment.calendar.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_NO_LOCALIZED_COLLATORS
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

class CalendarFragment : Fragment(R.layout.fragment_bottomnvi_calendar) {
    // binding
    private var _binding: FragmentBottomnviCalendarBinding? = null

    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity

    //캘린더 변수
    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String

    //
    private lateinit var selectedDate: String // 달력에서 선택한 날짜
    private val scheduleAdapter by lazy { ScheduleAdapter() }
    private val dateSaveModule: DateSaveModule by inject() // 날짜를 저장하는 DataStore
    private val scheduleViewModel: ScheduleViewModel by viewModel()
    private val eventViewModel: EventViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomnviCalendarBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //addSEchedule버튼으로 adddialog프래그먼트로 이동 코드 수정필요할지도
        binding.btnAddschedule.setOnClickListener{
            val dialog = AddDialogFragment()
            dialog.show(parentFragmentManager, "AddScheduleDialog")
        }


        binding.calendarView.selectedDate = CalendarDay.today() // 오늘 날짜 출력
//        binding.calendarView.addDecorators(SaturdayDecorator(), SundayDecorator()) // 주말 강조

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
}



//        binding.calendarView.setOnDateChangedListener { widget:MaterialCalendarView, date: CalendarDay, selected: Boolean  ->
//            binding.diaryTextView.visibility = View.VISIBLE
//            binding.btnPlus.visibility = View.VISIBLE
//            binding.contextEditText.visibility = View.INVISIBLE
//            binding.diaryContent.visibility = View.INVISIBLE
//            binding.btnUpdate.visibility = View.INVISIBLE
//            binding.btnDelete.visibility = View.INVISIBLE
//            binding.btnComplete.visibility = View.INVISIBLE
//            binding.diaryTextView.text = String.format("%d / %d / %d", date.year, date.month, date.day)
//            binding.contextEditText.setText("")
//            checkDay(date.year, date.month, date.day, userID)
//        }
//
//        binding.btnPlus.setOnClickListener {
//            binding.contextEditText.visibility = View.VISIBLE
//            binding.btnPlus.visibility = View.INVISIBLE
//            binding.btnComplete.visibility = View.VISIBLE
//            binding.btnUpdate.visibility = View.INVISIBLE
//            binding.btnDelete.visibility = View.INVISIBLE
////            str = binding.contextEditText.text.toString()
////            binding.diaryContent.text = str
////            binding.diaryContent.visibility = View.VISIBLE
//        }
//        binding.btnComplete.setOnClickListener {
//            saveDiary(fname)
//            binding.contextEditText.visibility = View.VISIBLE
//            binding.btnPlus.visibility = View.INVISIBLE
//            binding.btnComplete.visibility = View.INVISIBLE
//            binding.btnUpdate.visibility = View.VISIBLE
//            binding.btnDelete.visibility = View.VISIBLE
//            str = binding.contextEditText.text.toString()
//            binding.diaryContent.text = str
//            binding.diaryContent.visibility = View.VISIBLE
//        }







//    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String) {
//        //저장할 파일 이름설정
//        fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"
//
//        var fileInputStream: FileInputStream
//        try {
//            fileInputStream = mainActivity.openFileInput(fname)
//            val fileData = ByteArray(fileInputStream.available())
//            fileInputStream.read(fileData)
//            fileInputStream.close()
//            str = String(fileData)
//            binding.contextEditText.visibility = View.INVISIBLE
//            binding.diaryContent.visibility = View.VISIBLE
//            binding.diaryContent.text = str
//            binding.btnPlus.visibility = View.INVISIBLE
//            binding.btnUpdate.visibility = View.VISIBLE
//            binding.btnDelete.visibility = View.VISIBLE
//            binding.btnComplete.visibility = View.INVISIBLE
//            binding.btnUpdate.setOnClickListener {
//                binding.contextEditText.visibility = View.VISIBLE
//                binding.diaryContent.visibility = View.INVISIBLE
//                binding.contextEditText.setText(str)
//                //여기바꿈
//                binding.btnPlus.visibility = View.INVISIBLE
//                binding.btnComplete.visibility = View.VISIBLE
//                binding.btnUpdate.visibility = View.INVISIBLE
//                binding.btnDelete.visibility = View.INVISIBLE
//                binding.diaryContent.text = binding.contextEditText.text
//            }
//            binding.btnDelete.setOnClickListener {
//                binding.diaryContent.visibility = View.INVISIBLE
//                binding.btnUpdate.visibility = View.INVISIBLE
//                binding.btnDelete.visibility = View.INVISIBLE
//                binding.btnComplete.visibility = View.INVISIBLE
//                binding.contextEditText.setText("")
//                binding.contextEditText.visibility = View.VISIBLE
//                binding.btnPlus.visibility = View.VISIBLE
//                removeDiary(fname)
//            }
//            if (binding.diaryContent.text == null) {
//                binding.diaryContent.visibility = View.INVISIBLE
//                binding.btnUpdate.visibility = View.INVISIBLE
//                binding.btnDelete.visibility = View.INVISIBLE
//                binding.btnComplete.visibility = View.INVISIBLE
//                binding.diaryTextView.visibility = View.VISIBLE
//                binding.btnPlus.visibility = View.VISIBLE
//                binding. contextEditText.visibility = View.VISIBLE
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//
//    // 달력 내용 제거
//    @SuppressLint("WrongConstant")
//    fun removeDiary(readDay: String?) {
//        var fileOutputStream: FileOutputStream
//        try {
//            fileOutputStream = mainActivity.openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
//
//            val content = ""
//            fileOutputStream.write(content.toByteArray())
//            fileOutputStream.close()
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//    }
//
//
//    // 달력 내용 추가
//    @SuppressLint("WrongConstant")
//    fun saveDiary(readDay: String?) {
//        var fileOutputStream: FileOutputStream
//        try {
//            fileOutputStream = mainActivity.openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
//            val content = binding.contextEditText.text.toString()
//            fileOutputStream.write(content.toByteArray())
//            fileOutputStream.close()
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//    }
