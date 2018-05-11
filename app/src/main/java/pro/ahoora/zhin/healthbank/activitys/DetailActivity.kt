package pro.ahoora.zhin.healthbank.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.models.KotlinItemModel
import pro.ahoora.zhin.healthbank.utils.StaticValues

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction({ db: Realm? ->
            db?.where(KotlinItemModel::class.java)
                    ?.equalTo("centerId", id)
                    ?.equalTo("saved", true)
                    ?.findAll()
                    ?.deleteAllFromRealm()
            val model: KotlinItemModel = if (i == 0) {
                db?.where(KotlinItemModel::class.java)?.equalTo("centerId", id)?.findFirst()!!
            } else {
                SearchActivity.tempModel
            }
            model.saved = true
            db?.copyToRealmOrUpdate(model)
            runOnUiThread {
                Toast.makeText(this@DetailActivity, "آیتم با موفقیت ذخیره شد .", Toast.LENGTH_SHORT).show()
            }
        })

        realm.beginTransaction()
        val a = realm?.where(KotlinItemModel::class.java)
                ?.equalTo("saved", true)
                ?.equalTo("centerId", id)
                ?.findFirst()
        Log.e("A", "${a?.specialityList?.size} sSize")
        Log.e("A", "${a?.addressList?.size} sAddress")
        Log.e("A", "${a?.centerId} id")
        realm.commitTransaction()

    }

    var id = 0
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (intent != null) {
            id = intent.getIntExtra("id", 1)
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            if (intent.getIntExtra(StaticValues.MODEL, 0) == 0) {
                i = 0;
                realmItemModel(realm.where(KotlinItemModel::class.java).equalTo("centerId", id).findFirst()!!)
            } else if (intent.getIntExtra(StaticValues.MODEL, 0) == 1) {
                i = 1
                btn_save.visibility = View.GONE
                favItemModel(realm.where(KotlinItemModel::class.java).equalTo("centerId", id).findFirst()!!)
            } else if (intent.getIntExtra(StaticValues.MODEL, 0) == 2) {
                i = 2
                btn_save.visibility = View.VISIBLE
                kotlinItemModel(SearchActivity.tempModel)
            }
            realm.commitTransaction()
            btn_save.setOnClickListener(this)
        }

        rv_imageListBig.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_imageListBig.adapter = ImageAdapter()
        list_indicator.attachToRecyclerView(rv_imageListBig)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_imageListBig)

        rv_imageListBig.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val p = (recyclerView?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (p >= 0) {
                    rv_imageThumbnail.smoothScrollToPosition(p)
                    (rv_imageThumbnail.adapter as ThumbnailAdapter).select(p)
                }
            }
        })

        rv_imageThumbnail.layoutManager = LinearLayoutManager(this)
        rv_imageThumbnail.adapter = ThumbnailAdapter()

    }

    var address = ""

    private fun realmItemModel(item: KotlinItemModel) {
        tv_dName.text = item.firstName + " " + item.lastName
        tv_dt.text = item.specialityList!![0]?.name
        tv_dAddress.text = item.addressList!![0]?.locTitle
        address = item.buildingImg!!
    }

    private fun favItemModel(item: KotlinItemModel) {
        tv_dName.text = item.firstName + " " + item.lastName
        tv_dt.text = item.specialityList!![0]?.name
        tv_dAddress.text = item.addressList!![0]?.locTitle
        address = item.buildingImg!!
    }

    private fun kotlinItemModel(item: KotlinItemModel) {
        tv_dName.text = item.firstName + " " + item.lastName
        tv_dt.text = item.specialityList!![0]?.name
        tv_dAddress.text = item.addressList!![0]?.locTitle
        address = item.buildingImg!!
    }


    inner class ImageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val v = LayoutInflater.from(this@DetailActivity).inflate(R.layout.image_big_item, parent, false)
            return ImageHolder(v)
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            Glide.with(this@DetailActivity).load(address).into((holder as ImageHolder).ivImage)
        }


        internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            override fun onClick(v: View?) {
                rv_imageThumbnail.smoothScrollToPosition(adapterPosition)
            }

            val cvImage = itemView.findViewById<CardView>(R.id.cv_bigItem)
            val ivImage = itemView.findViewById<AppCompatImageView>(R.id.iv_imageBig)

            init {
                cvImage.setOnClickListener(this)
            }
        }

    }

    inner class ThumbnailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val v = LayoutInflater.from(this@DetailActivity).inflate(R.layout.image_thumbnail_item, parent, false)
            return ImageHolder(v)
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            Glide.with(this@DetailActivity).load(address).into((holder as ImageHolder).ivImage)
        }


        internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            override fun onClick(v: View?) {
                rv_imageListBig.smoothScrollToPosition(adapterPosition)
            }

            val cvImage = itemView.findViewById<CardView>(R.id.cv_smallItem)
            val ivImage = itemView.findViewById<AppCompatImageView>(R.id.iv_imageThumbnail)

            init {
                cvImage.setOnClickListener(this)
            }
        }

        fun select(position: Int) {
            Log.e("selected", "$position" + "")
        }

    }


}
