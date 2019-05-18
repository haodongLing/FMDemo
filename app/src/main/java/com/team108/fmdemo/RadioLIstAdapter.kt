package com.team108.fmdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotterknife.bindView

class RadioLIstAdapter(dataList: MutableList<RadioItem2>): RecyclerView.Adapter<RadioLIstAdapter.RadioViewHolder>() {

    private var dataList = mutableListOf<RadioItem2>()
    private var listener: IOnClickRadioItemListener? = null

    init {
        this.dataList = dataList
    }

    fun setOnClickItemListener(listener: IOnClickRadioItemListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioViewHolder {
        val layoutInflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = layoutInflater.inflate(R.layout.item_view_radio, parent, false)
        val viewHolder = RadioViewHolder(itemView)
        viewHolder.setOnClickListener(object : IOnClickRadioItemListener {
            override fun onClickRadioItm(model: RadioItem2) {
                listener?.onClickRadioItm(model)
            }
        })
        return viewHolder
    }

    override fun getItemCount(): Int {
        return this.dataList.size
    }

    override fun onBindViewHolder(holder: RadioViewHolder, position: Int) {
        holder.setData(dataList[position])
    }

    class RadioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val tvContent: TextView by bindView(R.id.tv_content)
        private var model: RadioItem2 = RadioItem2("", "", "")
        private lateinit var listener: IOnClickRadioItemListener

        fun setData(model: RadioItem2) {
            this.model = model
            tvContent.text = model.name
        }
        fun setOnClickListener(listener: IOnClickRadioItemListener) {
            this.listener = listener
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            this.listener.onClickRadioItm(model)
        }
    }

    interface IOnClickRadioItemListener {
        fun onClickRadioItm(model: RadioItem2)
    }

}