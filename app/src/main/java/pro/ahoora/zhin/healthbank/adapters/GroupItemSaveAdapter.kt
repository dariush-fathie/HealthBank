package pro.ahoora.zhin.healthbank.adapters


import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import io.realm.Realm
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.activitys.DetailActivity
import pro.ahoora.zhin.healthbank.models.KotlinItemModel
import pro.ahoora.zhin.healthbank.utils.StaticValues

class GroupItemSaveAdapter(ctx: Context, idList: ArrayList<Int>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context = ctx
    private val ids = idList
    private var realm: Realm = Realm.getDefaultInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        return ItemHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Glide.with(context).load("").apply(RequestOptions().centerCrop()).into(holder.imageView)
        try {
            realm.beginTransaction()
            val item = realm.where(KotlinItemModel::class.java).equalTo("centerId", ids?.get(position)).findFirst()
            realm.commitTransaction()
            (holder as ItemHolder).title.text = "${item?.firstName} ${item?.lastName}"
            Log.e("SaveAdapter", "${item?.centerId} itemId")
            Log.e("SaveAdapter", "${item?.specialityList?.size} spSize")
            holder.subTitle.text = item?.specialityList!![0]?.name
        } catch (e: Exception) {
            Log.e("SaveAdapter", e.message + " ")
        }
    }

    override fun getItemCount(): Int {
        val s = ids?.size
        return if (s == 0) {
            0
        } else {
            s!!
        }
    }


    internal inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val i = Intent(context, DetailActivity::class.java)
            i.putExtra(StaticValues.MODEL, 1)
            i.putExtra(StaticValues.ID, ids?.get(adapterPosition))
            context.startActivity(i)
        }

        val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_subTitle)
        val t: AppCompatTextView = itemView.findViewById(R.id.tv_t)
        val item: RelativeLayout = itemView.findViewById(R.id.rl_item)

        init {
            item.setOnClickListener(this)
        }
    }


}
