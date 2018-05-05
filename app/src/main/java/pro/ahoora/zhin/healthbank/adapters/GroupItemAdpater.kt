package pro.ahoora.zhin.healthbank.adapters;

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.models.RealmItemModel

class GroupItemAdapter(ctx: Context, idList: ArrayList<Int>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context = ctx
    private val ids = idList
    private var realm: Realm = Realm.getDefaultInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        return ItemHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Glide.with(context).load("").apply(RequestOptions().centerCrop()).into(holder.imageView)
        realm.beginTransaction()
        val item = realm.where(RealmItemModel::class.java).equalTo("_id", ids?.get(position)).findFirst()
        realm.commitTransaction()
        (holder as ItemHolder).title.text = "${item?.firstName} ${item?.lastName}"
        (holder as ItemHolder).subTitle.text = "$position"
    }

    override fun getItemCount(): Int {
        val s = ids?.size
        return if (s == 0) {
            0
        } else {
            s!!
        }
    }


    internal inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_subTitle)
        val t: AppCompatTextView = itemView.findViewById(R.id.tv_t)

        init {

        }
    }


}