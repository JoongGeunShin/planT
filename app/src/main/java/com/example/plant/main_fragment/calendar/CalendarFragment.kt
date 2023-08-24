package com.example.plant.main_fragment.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_NO_LOCALIZED_COLLATORS
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plant.MainActivity
import com.example.plant.R
import com.example.plant.databinding.FragmentBottomnviCalendarBinding
import java.io.FileInputStream
import java.io.FileOutputStream

class CalendarFragment : Fragment(R.layout.fragment_bottomnvi_calendar) {
    // binding
    private var _binding: FragmentBottomnviCalendarBinding? = null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity

    //캘린더 변수
    var userID: String = "userID"
    lateinit var fname: String
    lateinit var  str: String

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
        return  view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            binding.diaryTextView.visibility = View.VISIBLE
            binding.btnPlus.visibility = View.VISIBLE
            binding.contextEditText.visibility = View.INVISIBLE
            binding.diaryContent.visibility = View.INVISIBLE
            binding.btnUpdate.visibility = View.INVISIBLE
            binding.btnDelete.visibility = View.INVISIBLE
            binding.btnComplete.visibility = View.INVISIBLE
            binding.diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            binding.contextEditText.setText("")
            checkDay(year, month, dayOfMonth, userID)
        }
        binding.btnPlus.setOnClickListener {
            binding.contextEditText.visibility = View.VISIBLE
            binding.btnPlus.visibility = View.INVISIBLE
            binding.btnComplete.visibility = View.VISIBLE
            binding.btnUpdate.visibility = View.INVISIBLE
            binding.btnDelete.visibility = View.INVISIBLE
//            str = binding.contextEditText.text.toString()
//            binding.diaryContent.text = str
//            binding.diaryContent.visibility = View.VISIBLE
        }
        binding.btnComplete.setOnClickListener {
            saveDiary(fname)
            binding.contextEditText.visibility = View.VISIBLE
            binding.btnPlus.visibility = View.INVISIBLE
            binding.btnComplete.visibility = View.INVISIBLE
            binding.btnUpdate.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.VISIBLE
            str = binding.contextEditText.text.toString()
            binding.diaryContent.text = str
            binding.diaryContent.visibility = View.VISIBLE
        }




    }


    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String) {
        //저장할 파일 이름설정
        fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"

        var fileInputStream: FileInputStream
        try {
            fileInputStream = mainActivity.openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            str = String(fileData)
            binding.contextEditText.visibility = View.INVISIBLE
            binding.diaryContent.visibility = View.VISIBLE
            binding.diaryContent.text = str
            binding.btnPlus.visibility = View.INVISIBLE
            binding.btnUpdate.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnComplete.visibility = View.INVISIBLE
            binding.btnUpdate.setOnClickListener {
                binding.contextEditText.visibility = View.VISIBLE
                binding.diaryContent.visibility = View.INVISIBLE
                binding.contextEditText.setText(str)
                //여기바꿈
                binding.btnPlus.visibility = View.INVISIBLE
                binding.btnComplete.visibility = View.VISIBLE
                binding.btnUpdate.visibility = View.INVISIBLE
                binding.btnDelete.visibility = View.INVISIBLE
                binding.diaryContent.text = binding.contextEditText.text
            }
            binding.btnDelete.setOnClickListener {
                binding.diaryContent.visibility = View.INVISIBLE
                binding.btnUpdate.visibility = View.INVISIBLE
                binding.btnDelete.visibility = View.INVISIBLE
                binding.btnComplete.visibility = View.INVISIBLE
                binding.contextEditText.setText("")
                binding.contextEditText.visibility = View.VISIBLE
                binding.btnPlus.visibility = View.VISIBLE
                removeDiary(fname)
            }
            if (binding.diaryContent.text == null) {
                binding.diaryContent.visibility = View.INVISIBLE
                binding.btnUpdate.visibility = View.INVISIBLE
                binding.btnDelete.visibility = View.INVISIBLE
                binding.btnComplete.visibility = View.INVISIBLE
                binding.diaryTextView.visibility = View.VISIBLE
                binding.btnPlus.visibility = View.VISIBLE
                binding. contextEditText.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    fun removeDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = mainActivity.openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)

            val content = ""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = mainActivity.openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = binding.contextEditText.text.toString()
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }



}