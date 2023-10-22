package com.example.plant.main_fragment.calendar.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plant.R
import com.example.plant.databinding.ModifyScheduleDialogBinding
import com.example.plant.main_fragment.calendar.model.Schedule
import com.example.plant.main_fragment.calendar.viewModel.EventViewModel
import com.example.plant.main_fragment.calendar.viewModel.ScheduleViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import io.github.muddz.styleabletoast.StyleableToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScheduleModifyFragment(
    private var serialNum: Int // 일정 일련번호
) : DialogFragment() {

    //내가추가한것 binding
    private val binding by viewBinding(ModifyScheduleDialogBinding::bind)
    private val scheduleViewModel: ScheduleViewModel by viewModel()
    private val eventViewModel: EventViewModel by viewModel()
    private lateinit var schedule: Schedule

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.modify_schedule_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timePicker.visibility = TimePicker.GONE // 타임피커 기본설정
        binding.timePicker2.visibility = TimePicker.GONE
        // 시작 클릭 listener
        binding.btnStartTime.setOnClickListener {
            binding.timePicker2.visibility = TimePicker.GONE
            binding.timePicker.visibility = TimePicker.VISIBLE
            binding.timePicker.setIs24HourView(true)
        }
        // 종료 클릭 listener
        binding.btnEndTime.setOnClickListener {
            binding.timePicker.visibility = TimePicker.GONE
            binding.timePicker2.visibility = TimePicker.VISIBLE
            binding.timePicker2.setIs24HourView(true)

        }
        //실시간으로 타임 피커의 시간 보여주는 기능
        binding.timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            binding.btnStartTime.text =
                binding.timePicker.hour.toString() + ":" + binding.timePicker.minute.toString()
        }
        binding.timePicker2.setOnTimeChangedListener { view, hourOfDay, minute ->
            binding.btnEndTime.text =
                binding.timePicker2.hour.toString() + ":" + binding.timePicker2.minute.toString()
        }

        binding.saveScheduleBtn.setOnClickListener {
            val date = binding.date.text.toString() // 날짜
            val content = binding.tvItemTitle.text.toString() // 내용
            if (content.isEmpty() ) { //내용 비었을 때 저장 X
                FancyToast.makeText(
                    context, "내용을 입력해주세요", FancyToast.LENGTH_SHORT, FancyToast.INFO, true
                ).show()
            }
            StyleableToast.makeText(requireContext(), "저장", R.style.saveToast).show()
            this.dismiss()
        }
    }
}
