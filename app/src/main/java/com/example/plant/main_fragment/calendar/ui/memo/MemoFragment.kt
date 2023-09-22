package com.example.plant.main_fragment.calendar.ui.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plant.R
import com.example.plant.databinding.FragmentBottomnviCalendarBinding
import com.example.plant.databinding.FragmentBottomnviMemoBinding
import com.example.plant.main_fragment.calendar.adapter.MemoAdapter
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MemoFragment : Fragment(R.layout.fragment_bottomnvi_memo) {

    private val binding by viewBinding(FragmentBottomnviMemoBinding::bind,
        onViewDestroyed = { binding ->
            binding.todoListView.adapter = null
        })

    private val memoViewModel: MemoViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serialNum = 0 // 메모 일련번호
        val adapter = MemoAdapter(requireContext(), memoViewModel)

        // RecyclerView 스와이프 기능
        val itemTouchHelper = ItemTouchHelper(SwipeController(adapter))
        itemTouchHelper.attachToRecyclerView(binding.todoListView)

        binding.todayDate.text = "장바구니"
        binding.todayDate.text

        // 모든 메모 가져오기
        memoViewModel.getAllMemo().observe(viewLifecycleOwner, Observer { list ->
            adapter.removeAll()
            list?.let {
                adapter.list = it as ArrayList<Memo>
            }
            binding.todoListView.adapter = adapter
            binding.todoListView.layoutManager = LinearLayoutManager(requireContext())
        })
    }
}
