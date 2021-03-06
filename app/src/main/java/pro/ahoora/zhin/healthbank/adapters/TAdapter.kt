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
import io.realm.Realm
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.models.KotlinSpecialityModel

class TAdapter(ctx: Context, filterList: ArrayList<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val idsArray = ArrayList<Int>()
    val context = ctx
    val tArr = ArrayList<String>()
    val tIds = ArrayList<Int>()
    var filters = filterList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.t_list_item, parent, false)
        return ItemHolder(v)
    }

    override fun getItemCount(): Int {
        return tArr.size
    }

    init {
        fillBuffer()
        filterList.forEach { i: Int ->
            idsArray.add(i)
        }
    }

    private fun fillBuffer() {
        tArr.clear()
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result = realm.where(KotlinSpecialityModel::class.java).distinct("specialtyId").findAll()
        realm.commitTransaction()
        result.forEach { item: KotlinSpecialityModel ->
            tArr.add(item.name!!)
            tIds.add(item.specialtyId)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemHolder).tTitle.text = tArr[position]
        val x = tIds[position]
        holder.cb.isChecked = filters.contains(x)
    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var f = false

            idsArray.forEach { i: Int ->
                if (i == tIds[adapterPosition]) {
                    f = true
                }
            }
            if (!f) {
                idsArray.add(tIds[adapterPosition])
                cb.isChecked = true

            } else {
                cb.isChecked = false
                idsArray.remove(tIds[adapterPosition])
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