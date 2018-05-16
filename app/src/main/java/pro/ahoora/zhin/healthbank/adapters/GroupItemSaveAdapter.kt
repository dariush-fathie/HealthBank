package pro.ahoora.zhin.healthbank.adapters


import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.realm.Realm
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.activitys.DetailActivity
import pro.ahoora.zhin.healthbank.models.KotlinItemModel
import pro.ahoora.zhin.healthbank.utils.StaticValues

class GroupItemSaveAdapter(ctx: Context, idList: ArrayList<Int>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context = ctx
    private val ids = idList
    private var realm: Realm = Realm.getDefaultInstance()


    private fun getModelByCenterId(centerId: Int): KotlinItemModel {
        var item = KotlinItemModel()
        realm.executeTransaction({ db ->
            item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
        })
        return item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        return ItemHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ItemHolder
        val item = getModelByCenterId(ids!![position])
        Glide.with(context)
                .load(item.logoImg)
                .apply(RequestOptions().centerCrop().error(R.drawable.ic_jin).override(100, 130))
                .into(holder.image)
        try {
            holder.title.text = "${item?.firstName} ${item?.lastName}"
            Log.e("SaveAdapter", "${item.centerId} itemId")
            Log.e("SaveAdapter", "${item.specialityList?.size} spSize")
            holder.subTitle.text = item.specialityList!![0]?.name
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

    fun deleteItem(centerId: Int, position: Int) {
        realm.executeTransaction({ db ->
            val item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
            item.saved = false
        })

        ids?.remove(centerId)
        notifyItemRemoved(position)
    }


    internal inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.rl_item -> {
                    val i = Intent(context, DetailActivity::class.java)
                    i.putExtra(StaticValues.MODEL, 1)
                    i.putExtra(StaticValues.ID, ids?.get(adapterPosition))
                    context.startActivity(i)
                }
                R.id.iv_starLike -> {
                    deleteItem(ids!![adapterPosition], adapterPosition)
                }
            }

        }

        val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_subTitle)
        val t: AppCompatTextView = itemView.findViewById(R.id.tv_t)
        val item: RelativeLayout = itemView.findViewById(R.id.rl_item)
        val ivDelete: AppCompatImageView = itemView.findViewById(R.id.iv_starLike)
        val image: AppCompatImageView = itemView.findViewById(R.id.iv_itemImage)

        init {
            item.setOnClickListener(this)
            ivDelete.setImageResource(R.drawable.ic_trash)
            ivDelete.setOnClickListener(this)
        }
    }


}
