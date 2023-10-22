package com.example.plant.main_fragment.calendar.ui.calendar

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plant.R
import com.example.plant.databinding.AddWishlistDialogBinding
import com.example.plant.main_fragment.calendar.adapter.WishListAdapter
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.viewModel.DialogViewModel
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddWishlistFragment : DialogFragment() { // 장바구니 추가 다이어로그

    //바꿔야함
    private val binding by viewBinding(AddWishlistDialogBinding::bind)
    private val memoViewModel: MemoViewModel by viewModel()
    private val dialogViewModel: DialogViewModel by activityViewModels()

    var serialNum: Int = 0 //메모 일련번호

    lateinit var title: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        return inflater.inflate(R.layout.add_wishlist_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WishListAdapter(requireContext(), memoViewModel)
        val serialNum = 0 //메모 일련번호)
        //모든 메모 가져오기
        memoViewModel.getAllMemo().observe(viewLifecycleOwner, Observer { list ->
            adapter.removeAll()
            list?.let {
                adapter.list = it as ArrayList<Memo>
            }
            binding.todoListView.adapter = adapter
            binding.todoListView.layoutManager = LinearLayoutManager(requireContext())
        })

        adapter.itemClick = object : WishListAdapter.ItemClick {
            override fun onClick(view: View, position: Int, list: ArrayList<Memo>) {

                val fragmentTransaction = parentFragmentManager.beginTransaction()
                var titleAndCategory = "(${list[position].category}) ${list[position].title}"
                dialogViewModel.updateText(titleAndCategory)
                dialogViewModel.getData().observe(viewLifecycleOwner, Observer {
                    Log.d(ContentValues.TAG, it.toString())
                })

                Log.d(ContentValues.TAG, list[position].title)

                fragmentTransaction.commit()
                dismiss()
            }
        }

        binding.btnCancelWishlist.setOnClickListener {
            this.dismiss()
        }

    }

}

