package pro.ahoora.zhin.healthbank.customClasses;

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import pro.ahoora.zhin.healthbank.R

class CustomTabLayout : TabLayout {
    val mContext: Context
    var selectedPosition = -1

    val TAG = "CustomTabLayout"

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
    }

    override fun addTab(tab: Tab, selected: Boolean) {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab_item, null, false)
        val textView = view.findViewById<TextView>(R.id.text1)
        val imageView = view.findViewById<ImageView>(R.id.icon)
        textView.text = tab.text
        imageView.setImageDrawable(tab.icon)
        tab.customView = view
        super.addTab(tab, selected)
    }


}