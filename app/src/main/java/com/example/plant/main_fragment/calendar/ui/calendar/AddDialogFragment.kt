package com.example.plant.main_fragment.calendar.ui.calendar

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plant.DateSaveModule
import com.example.plant.Importance
import com.example.plant.R
import com.example.plant.databinding.AddScheduleDialogBinding
import com.example.plant.databinding.ModifyMemoDialogBinding
import com.example.plant.main_fragment.calendar.adapter.WishListAdapter
import com.example.plant.main_fragment.calendar.model.Event
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.model.Schedule
import com.example.plant.main_fragment.calendar.ui.memo.MemoFragment
import com.example.plant.main_fragment.calendar.viewModel.EventViewModel
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import com.example.plant.main_fragment.calendar.viewModel.ScheduleViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.toDuration

class AddDialogFragment : DialogFragment(), View.OnClickListener { // 수정 다이얼로그

    private val binding by viewBinding(AddScheduleDialogBinding::bind)
    private val dateSaveModule: DateSaveModule by inject()
    private val scheduleViewModel: ScheduleViewModel by viewModel()
    private val eventViewModel: EventViewModel by viewModel()
    private val viewModel: MemoViewModel by viewModel()


    lateinit var addWishlistFragment: AddWishlistFragment

    // 알람 데이터
    private lateinit var selectedDate: String // 선택된 날짜
    var serialNum = 0 // 일련번호
    var content: String = "" //메모내용
    var livetitle: MutableLiveData<String> = MutableLiveData<String>().apply{
        value=""
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        return inflater.inflate(R.layout.add_schedule_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //여기만 바꾸면 됨
        livetitle.observe(viewLifecycleOwner, Observer {
            binding.tvItemTitle.setText(livetitle.value)
            Log.d(ContentValues.TAG, "livetitle실시간: ${livetitle.value}")
        })


        binding.saveScheduleBtn.setOnClickListener(this)
        binding.cancelDialogBtn.setOnClickListener(this)

        // 선택된 날짜 가져오기
        lifecycleScope.launch {
            selectedDate = dateSaveModule.date.first()
            binding.dateText.text = selectedDate
        }

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
        //장바구니 버튼 2023-09-23-신중근
        binding.btnTogoWishlist.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.saveScheduleBtn -> {
                content = binding.tvItemTitle.text.toString()
                if (content.isEmpty()) { //내용 비었을 때, 설정 안하면 저장 X
                    FancyToast.makeText(
                        context,
                        "내용을 입력해주세요",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.INFO,
                        true
                    ).show()
                } else {
                    lifecycleScope.launch {
                        val start_hour = binding.timePicker.hour.toString()
                        val start_minute = binding.timePicker.minute.toString()
                        val end_hour = binding.timePicker2.hour.toString()
                        val end_minute = binding.timePicker2.minute.toString()
                        withContext(Dispatchers.IO) {
                            scheduleViewModel.addSchedule(
                                Schedule(
                                    serialNum,
                                    selectedDate,
                                    content,
                                    start_hour,
                                    start_minute,
                                    end_hour,
                                    end_minute
                                )
                            )
                            eventViewModel.addDate(Event(selectedDate))
                        }
                    }
                    StyleableToast.makeText(requireContext(), "저장", R.style.saveToast).show()
                    this.dismiss()
                }
            }

            R.id.cancelDialogBtn -> {
                this.dismiss()
            }

            R.id.btn_togo_wishlist -> { // open 장바구니
                val wishlist = AddWishlistFragment()
                wishlist.show(parentFragmentManager, "AddWishlistDialog")
            }
        }
    }
}