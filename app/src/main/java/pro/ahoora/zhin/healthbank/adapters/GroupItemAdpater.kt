package pro.ahoora.zhin.healthbank.adapters;

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
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
import pro.ahoora.zhin.healthbank.activitys.OfficeActivity
import pro.ahoora.zhin.healthbank.models.KotlinItemModel
import pro.ahoora.zhin.healthbank.utils.StaticValues

class GroupItemAdapter(ctx: Context, idList: ArrayList<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context = ctx
    private val ids = idList
    private var realm: Realm = Realm.getDefaultInstance()
    lateinit var markedItem: BooleanArray
    var filedStarDrawable: Drawable

    init {
        realm.executeTransaction({ db ->
            val savedItems = db.where(KotlinItemModel::class.java).equalTo("saved", true).findAll()
            markedItem = BooleanArray(ids.size)
            val s = ArrayList<Int>() // to hold saved item centerId
            savedItems.forEach { model: KotlinItemModel? ->
                s.add(model?.centerId!!)
            }

            for (i in 0 until ids.size) {
                markedItem[i] = s.contains(ids[i]) // if item is saved put true in markedItem else put false
            }

        })

        filedStarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_star)!!
        filedStarDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        return ItemHolder(v)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            holder as ItemHolder
            val item = getModelByCenterId(ids[position])
            // todo use placeholders
            holder.title.text = "${item?.firstName} ${item?.lastName}"
            holder.subTitle.text = item?.specialityList!![0]?.name

            Glide.with(context)
                    .load(item.logoImg)
                    .apply(RequestOptions().centerCrop().error(R.drawable.ic_jin).override(100, 130))
                    .into(holder.image)

            if (markedItem[position]) {
                holder.ivStar.setImageDrawable(filedStarDrawable)
            } else {
                holder.ivStar.setImageResource(R.drawable.icons_star_1)
            }

        } catch (e: Exception) {
            Log.e("GroupAdapter", e.message + " ")
        }
    }

    override fun getItemCount(): Int {
        val s = ids.size
        return if (s == 0) {
            0
        } else {
            s
        }
    }


    fun getModelByCenterId(centerId: Int): KotlinItemModel {
        var item = KotlinItemModel()
        realm.executeTransaction({ db ->
            item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
        })
        return item
    }

    fun saveItem(centerId: Int) {
        realm.executeTransaction({ db ->
            val item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
            item.saved = true
        })
    }

    fun deleteItem(centerId: Int) {
        realm.executeTransaction({ db ->
            val item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
            item.saved = false
        })
    }


    fun onItemMarkedEvent(centerId: Int, savedOrDelete: Boolean) {
        if (clickedItemPosition != -1 && centerId == ids[clickedItemPosition]) {
            markedItem[clickedItemPosition] = savedOrDelete
            notifyItemChanged(clickedItemPosition)
        }
    }

    var clickedItemPosition = -1

    internal inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.rl_item -> {
                    val i = Intent(context, DetailActivity::class.java)
                    i.putExtra(StaticValues.MODEL, 0)
                    i.putExtra(StaticValues.ID, ids.get(adapterPosition))
                    clickedItemPosition = adapterPosition
                    (context as OfficeActivity).startActivityForResult(i, StaticValues.requestCodeOfficeDetail)
                    return
                }
                R.id.iv_starLike -> {
                    val item = getModelByCenterId(ids.get(adapterPosition))
                    if (item.saved) {
                        deleteItem(item.centerId) // set saved flag to false
                        ivStar.setImageResource(R.drawable.icons_star_1)
                        markedItem[adapterPosition] = false
                    } else {
                        saveItem(item.centerId) // set save flag to ture
                        ivStar.setImageDrawable(filedStarDrawable)
                        markedItem[adapterPosition] = true
                    }
                }
            }

        }

        val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_subTitle)
        val t: AppCompatTextView = itemView.findViewById(R.id.tv_t)
        val ivStar: AppCompatImageView = itemView.findViewById(R.id.iv_starLike)
        val item: RelativeLayout = itemView.findViewById(R.id.rl_item)
        val image: AppCompatImageView = itemView.findViewById(R.id.iv_itemImage)

        init {
            item.setOnClickListener(this)
            ivStar.setOnClickListener(this)
        }
    }


}
