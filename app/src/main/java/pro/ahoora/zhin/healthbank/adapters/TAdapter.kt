package pro.ahoora.zhin.healthbank.adapters

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import pro.ahoora.zhin.healthbank.R

class TAdapter(ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val idsArray = ArrayList<Int>()
    val context = ctx
    val tArr = arrayOf("عمومی", "جراحی عمومی", "کودکان", "جراحی مغز و اعصاب", "قلب و عروق", "چشم", "گوش و حلق وبینی", "داخلی", "آزمایشگاه", "پوست", "ارولوژی", "زنان و زایمان", "غدد داخلی و متابولیسم کودکان", "نورولوژی - اعصاب", "روانپزشکی", "قلب اطفال", "بیماری های عفونی اطفال", "عفونی", "گوارش و کبد", "نوزدان", "ریه", "دندانپزشکی", "ارتوپدی", "توانبخشی", "غدد داخلی و متابولیسم", "نفرولوژی", "خون و سرطان", "ایمونولوژی - آلرژی", "مغز و اعصاب", "داروخانه", "سونوگرافی")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.t_list_item, parent, false)
        return ItemHolder(v)
    }

    override fun getItemCount(): Int {
        return tArr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.tTitle.text = tArr[position]
            idsArray.forEach { i: Int ->
                if (position + 1 == i) {
                    holder.cb.isChecked = true
                    return
                } else {
                    holder.cb.isChecked = false
                }
            }
        }
    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var f = false

            idsArray.forEach { i: Int ->
                if (i == adapterPosition + 1) {
                    f = true
                }
            }
            if (!f) {
                idsArray.add(adapterPosition + 1)
                cb.isChecked = true

            } else {
                cb.isChecked = false
                idsArray.remove(adapterPosition + 1)
            }
            idsArray.forEach { i: Int ->
                Log.e("I", "$i")
            }
        }

        val tTitle: AppCompatTextView = itemView.findViewById(R.id.tv_t_title)
        val rl: RelativeLayout = itemView.findViewById(R.id.rl_t_item)
        val cb: AppCompatCheckBox = itemView.findViewById(R.id.cb_tItem)

        init {
            rl.setOnClickListener(this)
            cb.isClickable = false
        }
    }


}