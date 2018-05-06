package pro.ahoora.zhin.healthbank.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.models.*
import pro.ahoora.zhin.healthbank.utils.StaticValues

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        saveItem()
    }


    fun saveItem() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val item = realm.where(RealmItemModel::class.java).equalTo("_id", id).findFirst()
        realm.where(RealmItemModelSave::class.java).equalTo("_id", id).findAll().deleteFirstFromRealm()
        val newItem = realm.createObject(RealmItemModelSave::class.java, item?._id)
        newItem.naCode = item?.naCode
        newItem.system_num = item?.system_num!!
        newItem.firstName = item.firstName
        newItem.lastName = item.lastName
        newItem.regDate = item.regDate
        newItem.validDate = item.validDate
        newItem.active = item.active
        newItem.logoUrl = item.logoUrl
        newItem.buildingUrl = item.buildingUrl
        newItem.shortDesc = item.shortDesc
        newItem.bio = item.bio
        newItem.equipment = item.equipment
        newItem.services = item.services
        newItem.workTeam = item.workTeam
        newItem.elc_rec = item.elc_rec
        newItem.grade = item.grade
        newItem.groupId = item.groupId
        item.addressList?.forEach { itemA: RealmAddress ->
            val realmAddress = realm?.createObject(RealmAddress::class.java)
            realmAddress?.title = itemA.title
            realmAddress?.id = itemA.id
            realmAddress?.postalCode = itemA.postalCode
            realmAddress?.tel1 = itemA.tel1
            realmAddress?.tel1Desc = itemA.tel1Desc
            realmAddress?.tel2 = itemA.tel2
            realmAddress?.tel2Desc = itemA.tel2Desc
            realmAddress?.mobile1 = itemA.mobile1
            realmAddress?.mobile1Desc = itemA.mobile1Desc
            realmAddress?.mobile2 = itemA.mobile2
            realmAddress?.mobile2Desc = itemA.mobile2Desc
            realmAddress?.genDesc = itemA.genDesc
            realmAddress?.defaultAdd = itemA.defaultAdd
            realmAddress?.lat = itemA.lat
            realmAddress?.lng = itemA.lng
            realmAddress?.site = itemA.site
            realmAddress?.mail = itemA.mail
            realmAddress?.satDesc = itemA.satDesc
            realmAddress?.sunDesc = itemA.sunDesc
            realmAddress?.monDesc = itemA.monDesc
            realmAddress?.tuesDesc = itemA.tuesDesc
            realmAddress?.wedDesc = itemA.wedDesc
            realmAddress?.thursDesc = itemA.thursDesc
            realmAddress?.friDesc = itemA.friDesc
            newItem.AddressList?.add(realmAddress)

        }

        item.SlidesList.forEach { itemS: RealmSlides ->
            val realmSlide = realm?.createObject(RealmSlides::class.java)
            realmSlide?.url = itemS.url
            realmSlide?.arrange = itemS.arrange
            realmSlide?.description = itemS.description

            newItem.SlidesList?.add(realmSlide)
        }

        item.cInsuranceList.forEach { itemC: RealmCInsurance ->
            val realmCInsurance = realm?.createObject(RealmCInsurance::class.java)
            realmCInsurance?.id = itemC.id
            realmCInsurance?.name = itemC.name
            realmCInsurance?.description = itemC.description

            newItem.CInsuranceList?.add(realmCInsurance)

        }

        item.specialtiesList.forEach { itemSp: RealSpecialties ->
            val realSpecialties = realm?.createObject(RealSpecialties::class.java)
            realSpecialties?.name = itemSp.name
            realSpecialties?.id = itemSp.id

            newItem.SpecialtiesList?.add(realSpecialties)

            Toast.makeText(this, "آیتم با موفقیت ذخیره شد ", Toast.LENGTH_LONG).show()
        }

        item.levelList.forEach { itemL: RealmLevel ->
            val realmLevel = realm?.createObject(RealmLevel::class.java)
            realmLevel?.id = itemL.id
            realmLevel?.name = itemL.name
            newItem.LevelList?.add(realmLevel)

        }

        val i = realm.where(RealmItemModelSave::class.java).equalTo("_id", id).findAll().count()
        Log.e("count", "$i" + " ")
        realm.commitTransaction()
    }

    var id = 0
    var model = StaticValues.DEFAULT // default model is RealmItemModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (intent != null) {
            id = intent.getIntExtra("id", 1)
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            if (intent.getStringExtra(StaticValues.MODEL).equals(StaticValues.DEFAULT)) {
                realmItemModel(realm.where(RealmItemModel::class.java).equalTo("_id", id).findFirst()!!)
            } else {
                btn_save.visibility = View.GONE
                favItemModel(realm.where(RealmItemModelSave::class.java).equalTo("_id", id).findFirst()!!)
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

    private fun realmItemModel(item: RealmItemModel) {
        tv_dName.text = item.firstName + " " + item.lastName
        tv_dt.text = item.specialtiesList[0]?.name
        tv_dAddress.text = item.addressList[0]?.title
    }

    fun favItemModel(item: RealmItemModelSave) {
        tv_dName.text = item.firstName + " " + item.lastName
        tv_dt.text = item.specialtiesList[0]?.name
        tv_dAddress.text = item.addressList[0]?.title
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
        }


        internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            override fun onClick(v: View?) {
                rv_imageThumbnail.smoothScrollToPosition(adapterPosition)
            }

            val cvImage = itemView.findViewById<CardView>(R.id.cv_bigItem)

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
        }


        internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            override fun onClick(v: View?) {
                rv_imageListBig.smoothScrollToPosition(adapterPosition)
            }

            val cvImage = itemView.findViewById<CardView>(R.id.cv_smallItem)

            init {
                cvImage.setOnClickListener(this)
            }
        }

        fun select(position: Int) {
            Log.e("selected", "$position" + "")
        }

    }


}
