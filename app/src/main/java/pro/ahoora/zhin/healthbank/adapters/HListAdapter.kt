package pro.ahoora.zhin.healthbank.adapters;

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import pro.ahoora.zhin.healthbank.R

class HListAdapter(ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val context = ctx
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.map_bottom_list_item, parent, false)
        return ItemHolder(v)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            Toast.makeText(context, "$adapterPosition", Toast.LENGTH_SHORT).show()
        }

        init {
        }
    }

}