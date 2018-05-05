package pro.ahoora.zhin.healthbank.fragments;

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.greenrobot.eventbus.EventBus
import pro.ahoora.zhin.healthbank.R

class TFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {

    }

    lateinit var tList: RecyclerView
    lateinit var ok: AppCompatTextView
    lateinit var cancel: AppCompatTextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_t, container, false)
        init(v)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().post("loaded")
    }

    fun init(v: View) {
        tList = v.findViewById(R.id.rv_ts)
        ok = v.findViewById(R.id.tv_ok)
        cancel = v.findViewById(R.id.tv_cancel)
        ok.setOnClickListener(this)
        cancel.setOnClickListener(this)
        tList.layoutManager = GridLayoutManager(activity, 3)
        tList.adapter = TAdapter()
    }


    inner class TAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val tArr = arrayOf("گوارش و کبد" , "پوست و مو" , "قلب و عروق" , "مغز و اعصاب", "بینایی" , "دندانپزشکی" , "عمومی", "جراح" , "داخلی" , "اطفال")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val v = LayoutInflater.from(activity).inflate(R.layout.t_list_item, parent , false)
            return ItemHolder(v)
        }

        override fun getItemCount(): Int {
            return tArr.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ItemHolder){
                holder.tTitle.text = tArr[position]
            }
        }


        inner class ItemHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
            val tTitle:AppCompatTextView = itemView.findViewById(R.id.tv_t_title)
            init {

            }
        }

    }

}
