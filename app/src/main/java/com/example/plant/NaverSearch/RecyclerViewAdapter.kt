package com.example.plant.NaverSearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plant.R

class RecyclerViewAdapter (private val context: Context) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var datas = mutableListOf<RecyclerViewData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_recycler_naversearch, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    // <-- 클릭 이벤트
    // ListView와 다르게 클릭 리스너가 내장되지 않아 설정
    interface OnItemClickListener{
        fun onItemClick(v:View, data: RecyclerViewData, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    // --> 클릭 이벤트
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = itemView.findViewById(R.id.tv_rv_title)
        private val category: TextView = itemView.findViewById(R.id.tv_rv_category)
        private val description: TextView = itemView.findViewById(R.id.tv_rv_description)
        private val roadAddress: TextView = itemView.findViewById(R.id.tv_rv_roadAddress)

//        private lateinit var mapX: String
//        private lateinit var mapY: String

        fun bind(item: RecyclerViewData) {
            title.text = item.title
            category.text = item.category
            description.text = item.description
            roadAddress.text = item.roadAddress

//            mapX = item.mapX
//            mapY = item.mapY
            // 클릭 이벤트 위해
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
            }
        }

    }
}
