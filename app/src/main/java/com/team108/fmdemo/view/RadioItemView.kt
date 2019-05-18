package com.team108.fmdemo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.team108.fmdemo.R
import com.team108.fmdemo.RadioItem
import kotterknife.bindView

class RadioItemView: ConstraintLayout {
    private val tvContent: TextView by bindView(R.id.tv_content)
    private var model: RadioItem = RadioItem("", "", "", "")

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }
    private fun initView() {
        val layoutInflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.item_view_radio, this, true)
    }


}