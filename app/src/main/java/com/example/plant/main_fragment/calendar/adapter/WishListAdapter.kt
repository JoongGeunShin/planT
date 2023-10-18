package com.example.plant.main_fragment.calendar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plant.databinding.MemoItemBinding
import com.example.plant.main_fragment.calendar.model.Memo
import com.example.plant.main_fragment.calendar.ui.calendar.AddDialogFragment
import com.example.plant.main_fragment.calendar.ui.calendar.AddWishlistFragment
import com.example.plant.main_fragment.calendar.viewModel.MemoViewModel

class WishListAdapter(
    val context: Context,
    val viewModel: MemoViewModel
) : RecyclerView.Adapter<WishListAdapter.ViewHolder>(){
    lateinit var title: String
    lateinit var category: String
    lateinit var roadaddress: String

    var list = ArrayList<Memo>()
    private lateinit var binding: MemoItemBinding

    interface ItemClick {
        fun onClick(view: View, position: Int, list: ArrayList<Memo>)
    }
    var itemClick: WishListAdapter.ItemClick? = null

    fun setItemClick(itemClick: ItemClick, function: () -> Unit){
        this.itemClick = itemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListAdapter.ViewHolder {

        val inflater = LayoutInflater.from(context)
        binding = MemoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: WishListAdapter.ViewHolder, position: Int) {
        holder.OnBind(list[position])
//        Log.d(ContentValues.TAG, "WishListAdapter 호출됨")
        if (itemClick!=null){
            holder.itemView.setOnClickListener{ v ->
                itemClick?.onClick(v, position, list)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun removeAll() {
        list.clear()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun OnBind(item: Memo) {
            binding.memo = item
            binding.tvItemTitle.text = item.title
            binding.tvItemCategory.text = item.category
            binding.tvItemRoadaddress.text = item.roadaddress

            binding.itemCard.setOnClickListener{ View ->
                Toast.makeText(context, "카드뷰 클릭됨", Toast.LENGTH_SHORT).show()
                title = item.title
                category = item.category
                roadaddress = item.roadaddress


                // 바꿔야할 부분

//                val dialog = AddDialogFragment().apply{
//                    content = list[position].title
//                    serialNum = list[position].serialNum
//                }
//                val fragmentTransaction = dialog.childFragmentManager.beginTransaction()
//                fragmentTransaction.remove(AddDialogFragment())
//                fragmentTransaction.commit()
            //                val fragmentTransaction = dialog.childFragmentManager.beginTransaction()
//                fragmentTransaction.remove(AddWishlistFragment())
//                fragmentTransaction.commit()
//                activity?.let {
//                    dialog.show(it.supportFragmentManager, "MenuDialogFragment")
//                }
//                viewModel.getTitle(list[position].serialNum)
            }

        }
    }
}