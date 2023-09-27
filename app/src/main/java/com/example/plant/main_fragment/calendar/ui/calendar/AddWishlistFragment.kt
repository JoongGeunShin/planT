package com.example.plant.main_fragment.calendar.ui.calendar

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.plant.databinding.MemoItemBinding
import com.example.plant.main_fragment.calendar.adapter.MemoAdapter
import com.example.plant.main_fragment.calendar.adapter.WishListAdapter
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddWishlistFragment: DialogFragment(){ // 장바구니 추가 다이어로그

    //바꿔야함
    private val binding by viewBinding(AddWishlistDialogBinding::bind)
    private val memoViewModel: MemoViewModel by viewModel()
    var serialNum : Int = 0 //메모 일련번호
    var title: String = "" // 메모 제목

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

        Log.d(ContentValues.TAG, "AddWishlistFragment 열림")

        val adapter = WishListAdapter(requireContext(), memoViewModel)
        val serialNum = 0 //메모 일련번호

        //모든 메모 가져오기
        memoViewModel.getAllMemo().observe(viewLifecycleOwner, Observer { list ->
            adapter.removeAll()
            list?.let {
                adapter.list = it as ArrayList<Memo>
            }
            binding.todoListView.adapter = adapter
            binding.todoListView.layoutManager = LinearLayoutManager(requireContext())
        })

        binding.btnCancelWishlist.setOnClickListener {
            this.dismiss()
        }

        // wishlist 카드 클릭 이벤트
//        adapter.setItemClick(object : WishListAdapter.ItemClick{
//            override fun onClick(view: View, position: Int, list: ArrayList<Memo>) {
////                addDialogFragment.content = adapter.title
////                addWishlistFragment.dismiss()
//
//
//
//            }
//        })
//        binding.todoListView.setOnClickListener{
//            addDialogFragment.content = adapter.title
//            this.dismiss()
//        }



    }

}

