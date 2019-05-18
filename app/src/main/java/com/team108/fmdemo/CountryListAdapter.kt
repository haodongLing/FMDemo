package com.team108.fmdemo

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotterknife.bindView

class CountryListAdapter(dataList: MutableList<String>): RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {

    private var dataList = mutableListOf<String>()
    private var listener: IOnClickCountryItemListener? = null

    init {
        this.dataList = dataList
    }

    fun setOnClickItemListener(listener: IOnClickCountryItemListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val layoutInflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = layoutInflater.inflate(R.layout.item_view_country, parent, false)
        val viewHolder = CountryViewHolder(itemView)
        viewHolder.setOnClickListener(object : IOnClickCountryItemListener {
            override fun getCurrentSelectedIndex(): Int {
                return listener?.getCurrentSelectedIndex() ?: 0
            }

            override fun onClickCountryItm(index: Int) {
                listener?.onClickCountryItm(index)
            }
        })
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.setData(dataList[position], position)
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val tvContent: TextView by bindView(R.id.tv_content)
        private var model: String = ""
        private var index: Int = 0
        private lateinit var listener: IOnClickCountryItemListener

        fun setData(model: String, index: Int) {
            this.model = model
            this.index = index
            tvContent.text = model
            if (index == listener.getCurrentSelectedIndex()) {
                tvContent.background = ColorDrawable(Color.parseColor("#008577"))
            } else {
                tvContent.background = ColorDrawable(Color.parseColor("#00000000"))
            }
        }
        fun setOnClickListener(listener: IOnClickCountryItemListener) {
            this.listener = listener
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            this.listener.onClickCountryItm(index)
        }
    }

    interface IOnClickCountryItemListener {
        fun onClickCountryItm(index: Int)
        fun getCurrentSelectedIndex(): Int
    }
}