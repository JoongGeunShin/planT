package com.example.plant.main_fragment.calendar.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plant.R
import com.example.plant.databinding.AddWishlistDialogBinding
import com.example.plant.main_fragment.calendar.adapter.MemoAdapter
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddWishlistFragment: DialogFragment(), View.OnClickListener { // 장바구니 추가 다이어로그

    //바꿔야함
    private val binding by viewBinding(AddWishlistDialogBinding::bind)
    private val memoViewModel: MemoViewModel by viewModel()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        return inflater.inflate(R.layout.add_wishlist_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serialNum = 0 //메모 일련번호
        val adapter = MemoAdapter(requireContext(), memoViewModel)

        binding.todayDate.text = "장바구니"
        binding.todayDate.text

        //모든 메모 가져오기
        memoViewModel.getAllMemo().observe(viewLifecycleOwner, Observer { list ->
            adapter.removeAll()
            list?.let {
                adapter.list = it as ArrayList<Memo>
            }
            binding.todoListView.adapter = adapter
            binding.todoListView.layoutManager = LinearLayoutManager(requireContext())
        })



    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_choose_wishlist -> {
                //선택버튼 누르면 메모의 title받아와서 내용에 적어야함



            }

            R.id.btn_cancel_wishlist -> {
                this.dismiss()
            }

            R.id.todoListView -> {
//                val text = binding.todoListView.adapter
//                text
//                val title =
//                Intent(context, AddDialogFragment::class.java).apply{
//                    putExtra("title", )
//                }
            }

        }
    }

}