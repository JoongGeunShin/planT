package com.example.plant.main_fragment.calendar.ui.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.plant.R
import com.example.plant.databinding.MenuDialogBinding
import com.example.plant.databinding.ModifyMemoDialogBinding
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel


class MemoModifyFragment() : DialogFragment() {

    private lateinit var binding: ModifyMemoDialogBinding
//    private val binding by viewBinding(ModifyMemoDialogBinding::bind)
    private val memoViewModel : MemoViewModel by viewModel()
    var content: String = "" // 메모 내용
    var serialNum : Int = 0 // 메모 일련번호

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.modify_memo_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contentEdit.setText(content) // 기존 메모 내용 출력
        binding.modifyBtn.setOnClickListener {
            val newContent = binding.contentEdit.text.toString() // 변경된 메모 내용
            lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    memoViewModel.changeContent(newContent, serialNum) // 메모 수정
                }
            }
            this.dismiss()
        }
    }
}