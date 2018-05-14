package pro.ahoora.zhin.healthbank.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.activitys.DetailActivity
import pro.ahoora.zhin.healthbank.models.KotlinItemModel
import pro.ahoora.zhin.healthbank.utils.SharedPer
import pro.ahoora.zhin.healthbank.utils.StaticValues

class SearchAdapter(ctx: Context, data: List<KotlinItemModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context = ctx
    private val dataSet = data
    private lateinit var shp: SharedPer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        return ItemHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ItemHolder
        holder.title.text = dataSet.get(position).firstName + " " + dataSet.get(position).lastName
        holder.subTitle.text = dataSet.get(position).specialityList!![0]?.name

        Glide.with(context)
                .load(dataSet[position].logoImg)
                .apply(RequestOptions().centerCrop().error(R.drawable.ic_jin).override(100, 130))
                .into(holder.image)

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


    internal inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val i = Intent(context, DetailActivity::class.java)
            i.putExtra("id", dataSet[adapterPosition].centerId)
            i.putExtra(StaticValues.MODEL, 0)
            context.startActivity(i)
        }

        val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_subTitle)
        val t: AppCompatTextView = itemView.findViewById(R.id.tv_t)
        val item: RelativeLayout = itemView.findViewById(R.id.rl_item)
        val image: AppCompatImageView = itemView.findViewById(R.id.iv_itemImage)
        val ivStar: AppCompatImageView = itemView.findViewById(R.id.iv_starLike)

        init {
            item.setOnClickListener(this)
            ivStar.visibility = View.GONE
        }
    }
}
