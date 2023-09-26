package com.example.plant.main_fragment.calendar.adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.plant.R
//import androidx.room.processor.Context
import com.example.plant.databinding.MemoItemBinding
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.ui.calendar.AddDialogFragment
import com.example.plant.main_fragment.calendar.ui.memo.ItemTouchHelperListener
import com.example.plant.main_fragment.calendar.ui.memo.MemoModifyFragment
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishListAdapter(
    val context: Context,
    val viewModel: MemoViewModel
) : RecyclerView.Adapter<WishListAdapter.ViewHolder>(){

    lateinit var title:String
    lateinit var category:String
    lateinit var roadaddress:String

    var list = ArrayList<Memo>()
    private lateinit var binding: MemoItemBinding

    interface ItemClick {
        fun onClick(view: View, position: Int, list: ArrayList<Memo>)
    }

    var itemClick: WishListAdapter.ItemClick? = null

//    fun setItemClick(itemClick: ItemClick){
//        this.itemClick = itemClick
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = MemoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WishListAdapter.ViewHolder, position: Int) {
        Log.d(ContentValues.TAG, "WishListAdapter 호출됨")
//        if (itemClick != null) {
//            binding.completionBox.setOnClickListener { v ->
//                itemClick?.onClick(v, position, list)
//                Log.d(ContentValues.TAG, title)
//            }
//            Log.d(ContentValues.TAG, itemClick.toString())
//        }
    }

    override fun getItemCount(): Int = list.size

    fun removeAll() {
        list.clear()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun OnBind(item: Memo) {
            binding.memo = item
//            binding.tvItemTitle.text = item.title
//            binding.tvItemCategory.text = item.category
//            binding.tvItemRoadaddress.text = item.roadaddress

            binding.itemCard.setOnClickListener{ View ->
                Toast.makeText(context, "카드뷰 클릭됨", Toast.LENGTH_SHORT).show()
                Log.e("check", "카드뷰 클릭")
                title = item.title
                category = item.category
                roadaddress = item.roadaddress
            }

        }
    }
}