package pro.ahoora.zhin.healthbank.activitys

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.models.KotlinItemModel
import pro.ahoora.zhin.healthbank.utils.StaticValues

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    val realm = Realm.getDefaultInstance()!!

    override fun onClick(v: View?) {
        change = true
        if (isSaved) {
            btn_save.text = "ذخیره در نشان شده ها"
            deleteItem(id)
            itemSaved = false

        } else {
            btn_save.text = "حذف از نشان شده ها"
            saveItem(id)
            itemSaved = true
        }

    }

    var id = 0
    var i = 0
    var isSaved = false
    var itemSaved = false
    var change = false

    lateinit var item: KotlinItemModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (intent != null) {
            id = intent.getIntExtra("id", 1)
            isSaved = checkItemIsSaved()

            if (isSaved) {
                btn_save.text = "حذف از نشان شده ها"
            } else {
                btn_save.text = "ذخیره در نشان شده ها"
            }

            if (intent.getIntExtra(StaticValues.MODEL, 0) == 0) {
                i = 0
            } else if (intent.getIntExtra(StaticValues.MODEL, 0) == 1) {
                i = 1
                btn_save.visibility = View.GONE
            } else if (intent.getIntExtra(StaticValues.MODEL, 0) == 2) {
                i = 2
                btn_save.visibility = View.VISIBLE
            }

            if (i != 2) {
                val realm = Realm.getDefaultInstance()
                realm.beginTransaction()
                item = realm.where(KotlinItemModel::class.java).equalTo("centerId", id).findFirst()!!
                realm.commitTransaction()
            } else {
                item = SearchActivity.tempModel
            }

            loadDetails()
            btn_save.setOnClickListener(this)
        }

        initLists()
    }

    private fun saveItem(centerId: Int) {
        realm.executeTransaction({ db ->
            val item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
            item.saved = true
        })
    }

    private fun deleteItem(centerId: Int) {
        realm.executeTransaction({ db ->
            val item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
            item.saved = false
        })
    }

    private fun checkItemIsSaved(): Boolean {
        var isSaved = false
        realm.executeTransaction({ db ->
            val model = db.where(KotlinItemModel::class.java)
                    ?.equalTo("centerId", id)
                    ?.equalTo("saved", true)
                    ?.findAll()!!
            isSaved = model.count() > 0
        })
        return isSaved
    }

    private fun initLists() {
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


    private fun loadDetails() {
        tv_dName.text = item.firstName + " " + item.lastName
        tv_dt.text = item.specialityList!![0]?.name
        tv_dAddress.text = item.addressList!![0]?.locTitle
    }

    override fun onBackPressed() {
        if (change) {
            val resultPayload = Intent(this@DetailActivity, OfficeActivity::class.java)
            resultPayload.putExtra("save", itemSaved)
            resultPayload.putExtra("centerId", id)
            setResult(Activity.RESULT_OK, resultPayload)
        }
        super.onBackPressed()
    }

    inner class ImageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val v = LayoutInflater.from(this@DetailActivity).inflate(R.layout.image_big_item, parent, false)
            return ImageHolder(v)
        }

        override fun getItemCount(): Int {
            return item.slideList?.size!!
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            try {
                Glide.with(this@DetailActivity)
                        .load(item.slideList!![position]?.fileUrl)
                        .apply(RequestOptions()
                                .fitCenter()
                                .placeholder(R.drawable.ic_jin))
                        .into((holder as ImageHolder)
                                .ivImage)
            } catch (e: Exception) {
                Log.e("glideErr", e.message + " ")
            }
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
            return item.slideList?.size!!
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            try {
                Glide.with(this@DetailActivity)
                        .load(item.slideList!![position]?.fileUrl)
                        .apply(RequestOptions()
                                .fitCenter()
                                .placeholder(R.drawable.ic_jin))
                        .thumbnail(0.5f)
                        .into((holder as ImageHolder)
                                .ivImage)
            } catch (e: Exception) {
                Log.e("glideErr", e.message + " ")
            }
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
