package com.example.plant.main_fragment.calendar.adapter

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
//import androidx.room.processor.Context
import com.example.plant.databinding.MemoItemBinding
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.ui.memo.ItemTouchHelperListener
import com.example.plant.main_fragment.calendar.ui.memo.MemoModifyFragment
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoAdapter (
    //private val context: Context,
    private val context: android.content.Context,
    private val viewModel: MemoViewModel
) : RecyclerView.Adapter<MemoAdapter.Holder>(), ItemTouchHelperListener {

    var list = ArrayList<Memo>()
    private lateinit var binding: MemoItemBinding

    lateinit var title: String

    interface ItemClick {
        fun onClick(view: View, position: Int, list: ArrayList<Memo>)

    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(context)
        binding = MemoItemBinding.inflate(inflater, parent, false)
        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.d(ContentValues.TAG,"MemoAdapter 호출됨")
        holder.onBind(list[position])
        if (itemClick != null) {
            binding.completionBox.setOnClickListener { v ->
                itemClick?.onClick(v, position, list)
                Log.d(ContentValues.TAG,title)
            }
            Log.d(ContentValues.TAG,itemClick.toString())
        }
    }

    override fun getItemCount(): Int = list.size

    fun removeAll() {
        list.clear()
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(item: Memo) {
            binding.memo = item
            binding.itemCard.setOnClickListener{
                title = item.title
            }

        }
        private fun changeCompletion(completion: Boolean, serialNum: Int) { // 체크 유무 변경
            viewModel.changeCompletion(completion, serialNum)
        }
    }

    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) { // 메모 변경
        val dialog = MemoModifyFragment().apply {
            content = list[position].title
            serialNum = list[position].serialNum
        }
        dialog.show((context as FragmentActivity).supportFragmentManager, "MemoModifyFragment")
    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) { // 메모 삭제
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteMemo(list[position].serialNum)
        }
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean = false

    override fun onItemSwipe(position: Int) { // }
    }

}