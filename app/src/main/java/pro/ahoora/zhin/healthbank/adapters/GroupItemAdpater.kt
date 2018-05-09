package pro.ahoora.zhin.healthbank.adapters;

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import io.realm.Realm
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.activitys.DetailActivity
import pro.ahoora.zhin.healthbank.models.RealmItemModel
import pro.ahoora.zhin.healthbank.utils.StaticValues

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
        realm.executeTransaction({ realmDatabase ->
            val item = realm.where(RealmItemModel::class.java).equalTo("_id", ids?.get(position)).findFirst()
            (holder as ItemHolder).title.text = "${item?.firstName} ${item?.lastName}"
            holder.subTitle.text = item?.specialtiesList?.get(0)?.name
        })

        /*item?.specialtiesList?.forEach { special: RealSpecialties? ->

        }*/
        /*if (Utils.isItemSaved(ids!![position])) {
            holder.ivStar.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        } else {
            holder.ivStar.clearColorFilter()
        }*/
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
            when (v?.id) {
                R.id.rl_item -> {
                    val i = Intent(context, DetailActivity::class.java)
                    i.putExtra(StaticValues.MODEL, StaticValues.DEFAULT)
                    i.putExtra(StaticValues.ID, ids?.get(adapterPosition))
                    context.startActivity(i)
                    return
                }
                R.id.iv_starLike -> {
                    /*val id = ids!![adapterPosition]
                    if (!Utils.isItemSaved(id)) {
                        ivStar.clearColorFilter()
                        Utils.deleteSavedItem(id)
                    } else {
                        ivStar.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
                        Utils.saveItem(id)
                        Toast.makeText(context, "نشان شد ", Toast.LENGTH_SHORT).show()
                    }*/
                }
            }

        }

        val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_subTitle)
        val t: AppCompatTextView = itemView.findViewById(R.id.tv_t)
        val ivStar: AppCompatImageView = itemView.findViewById(R.id.iv_starLike)
        val item: RelativeLayout = itemView.findViewById(R.id.rl_item)

        init {
            item.setOnClickListener(this)
            ivStar.setOnClickListener(this)
        }
    }


}
