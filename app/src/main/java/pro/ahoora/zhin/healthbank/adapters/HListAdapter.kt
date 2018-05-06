package pro.ahoora.zhin.healthbank.adapters;

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.realm.Realm
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.models.RealmItemModel

class HListAdapter(ctx: Context, array: ArrayList<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val context = ctx
    val idArray = array
    val realm: Realm = Realm.getDefaultInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.map_bottom_list_item, parent, false)
        return ItemHolder(v)
    }

    override fun getItemCount(): Int {
        return idArray.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        realm.beginTransaction()
        val item = realm.where(RealmItemModel::class.java).equalTo("_id", idArray.get(position)).findFirst()
        realm.commitTransaction()
        (holder as ItemHolder).title.text = item?.firstName + " " + item?.lastName
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: AppCompatTextView = itemView.findViewById(R.id.tv_bottomListTitle)
        override fun onClick(v: View?) {
            Toast.makeText(context, "$adapterPosition", Toast.LENGTH_SHORT).show()
        }

        init {
        }
    }

}