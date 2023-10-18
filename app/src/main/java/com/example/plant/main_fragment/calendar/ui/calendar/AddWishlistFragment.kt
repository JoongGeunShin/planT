package com.example.plant.main_fragment.calendar.ui.calendar

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plant.R
import com.example.plant.databinding.AddWishlistDialogBinding
import com.example.plant.databinding.FragmentBottomnviHomeBinding
import com.example.plant.databinding.MemoItemBinding
import com.example.plant.main_fragment.calendar.adapter.MemoAdapter
import com.example.plant.main_fragment.calendar.adapter.WishListAdapter
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.viewModel.DialogViewModel
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddWishlistFragment : DialogFragment() { // 장바구니 추가 다이어로그

    //바꿔야함
    private val binding by viewBinding(AddWishlistDialogBinding::bind)
    private val memoViewModel: MemoViewModel by viewModel()
    private val dialogViewmodel: DialogViewModel by activityViewModels()
    var serialNum: Int = 0 //메모 일련번호
    var titlename: String = ""

    //    lateinit var addDialogFragment: AddDialogFragment
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

        //클릭이벤트
        adapter.itemClick = object : WishListAdapter.ItemClick {
            override fun onClick(view: View, position: Int, list: ArrayList<Memo>) {
                titlename = list[position].title
                //memoViewModel의 livetitle 라이브 데이터에 titlename 넣어줌
//                memoViewModel.livetitle.value = titlename
                dialogViewmodel.inputText.value=(titlename)
//                Log.d(ContentValues.TAG, "${dialogViewmodel.getData().value}")
                Toast.makeText(requireContext(), titlename, Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

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


    }

}

