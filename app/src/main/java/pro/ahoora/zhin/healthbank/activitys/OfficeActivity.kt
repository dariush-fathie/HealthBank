package pro.ahoora.zhin.healthbank.activitys

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_office.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.adapters.GroupItemAdapter
import pro.ahoora.zhin.healthbank.adapters.TAdapter
import pro.ahoora.zhin.healthbank.customClasses.CustomBottomSheetBehavior
import pro.ahoora.zhin.healthbank.customClasses.TabLayoutInterface
import pro.ahoora.zhin.healthbank.models.*
import pro.ahoora.zhin.healthbank.utils.ApiClient
import pro.ahoora.zhin.healthbank.utils.ApiInterface
import pro.ahoora.zhin.healthbank.utils.StaticValues
import pro.ahoora.zhin.healthbank.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OfficeActivity : AppCompatActivity(), View.OnClickListener {

    var groupId = 0
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
    private lateinit var tabLayoutInterface: TabLayoutInterface
    var idArray = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office)

        initListener()

        if (intent != null) {
            groupId = intent.getIntExtra(StaticValues.CATEGORY, 0)
        }

        tv_officeTitle.text = getTitleFromDb()
        ctbl.addTab(ctbl.newTab().setText("روی نقشه").setIcon(R.drawable.ic_map))
        ctbl.addTab(ctbl.newTab().setText("کاوش").setIcon(R.drawable.ic_search))
        initBottomSheet()
        tabLayoutInterface = TabLayoutInterface(this, supportFragmentManager, bottomSheetBehavior, ll_progress)
        ctbl.addOnTabSelectedListener(tabLayoutInterface)
        getItems()
    }

    fun getTitleFromDb(): String {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val name = realm.where(GroupModel_Realm::class.java).equalTo("id", groupId + 1).findFirst()?.name
        realm.commitTransaction()
        return name!!
    }

    private fun initListener(){
        rl_filter.setOnClickListener(this)
        rl_sort.setOnClickListener(this)
        iv_goback.setOnClickListener(this)
    }

    private fun initList(list: ArrayList<Int>) {
        rv_items.layoutManager = LinearLayoutManager(this)
        rv_items.adapter = GroupItemAdapter(this, list)
        hideMainProgressLayout()
    }

    fun hideMainProgressLayout() {
        ll_progressMain.visibility = View.GONE
    }

    fun showMainProgressLayout() {
        ll_progressMain.visibility = View.VISIBLE
    }


    private fun getItems() {
        if (Utils.isNetworkAvailable(this)) {
            val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
            apiInterface.getItems(groupId + 1).enqueue(object : Callback<List<ItemModel>> {
                override fun onResponse(call: Call<List<ItemModel>>?, response: Response<List<ItemModel>>?) {
                    val list = response?.body()
                    Log.e("success", list.toString() + " res")
                    val realmDatabase = Realm.getDefaultInstance()
                    realmDatabase.executeTransactionAsync { realm: Realm? ->
                        realm?.where(RealmItemModel::class.java)?.findAll()?.deleteAllFromRealm()
                        list?.forEach { item: ItemModel ->
                            // inserting each record to database
                            val realmItemModel = realm?.createObject(RealmItemModel::class.java, item.id)
                            Log.e("itemId", "${item.id}")
                            realmItemModel?.naCode = item.naCode
                            realmItemModel?.system_num = item.system_num
                            realmItemModel?.firstName = item.firstName
                            realmItemModel?.lastName = item.lastName
                            realmItemModel?.regDate = item.regDate
                            realmItemModel?.validDate = item.validDate
                            realmItemModel?.active = item.active
                            realmItemModel?.logoUrl = item.logoUrl
                            realmItemModel?.buildingUrl = item.buildingUrl
                            realmItemModel?.shortDesc = item.shortDesc
                            realmItemModel?.bio = item.bio
                            realmItemModel?.equipment = item.equipment
                            realmItemModel?.services = item.services
                            realmItemModel?.workTeam = item.workTeam
                            realmItemModel?.elc_rec = item.elc_rec
                            realmItemModel?.grade = item.grade
                            realmItemModel?.groupId = item.groupId
                            item.addresses?.forEach { itemA: ItemModel.Address ->
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

                                realmItemModel?.AddressList?.add(realmAddress)

                            }

                            item.slides.forEach { itemS: ItemModel.Slides ->
                                val realmSlide = realm?.createObject(RealmSlides::class.java)
                                realmSlide?.url = itemS.url
                                realmSlide?.arrange = itemS.arrange
                                realmSlide?.description = itemS.description

                                realmItemModel?.SlidesList?.add(realmSlide)
                            }

                            item.cInsurances.forEach { itemC: ItemModel.CInsurance ->
                                val realmCInsurance = realm?.createObject(RealmCInsurance::class.java)
                                realmCInsurance?.id = itemC.id
                                realmCInsurance?.name = itemC.name
                                realmCInsurance?.description = itemC.description

                                realmItemModel?.CInsuranceList?.add(realmCInsurance)

                            }

                            item.specialties.forEach { itemSp: ItemModel.Specialties ->
                                val realSpecialties = realm?.createObject(RealSpecialties::class.java)
                                realSpecialties?.name = itemSp.name
                                realSpecialties?.id = itemSp.id

                                realmItemModel?.SpecialtiesList?.add(realSpecialties)

                            }

                            item.levels.forEach { itemL: ItemModel.Level ->
                                val realmLevel = realm?.createObject(RealmLevel::class.java)
                                realmLevel?.id = itemL.id
                                realmLevel?.name = itemL.name

                                realmItemModel?.LevelList?.add(realmLevel)

                            }
                        }
                        val result1 = realm?.where(RealmItemModel::class.java)?.sort("firstName", Sort.ASCENDING)?.findAll()
                        idArray.clear()
                        result1?.forEach { itemModel: RealmItemModel? ->
                            idArray.add(itemModel?._id!!)
                        }
                        runOnUiThread {
                            initList(idArray)
                        }

                    }
                }

                override fun onFailure(call: Call<List<ItemModel>>?, t: Throwable?) {
                    Log.e("ERR", t?.message + "  ")
                }
            })
        } else {

        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(cl_bottomSheetLayout)
        if (bottomSheetBehavior is CustomBottomSheetBehavior) {
            (bottomSheetBehavior as CustomBottomSheetBehavior).setAllowUserDragging(false)
        }
        bottomSheetBehavior.setBottomSheetCallback(getBottomSheetCallback())

    }

    private fun getBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback {
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.e("slide", "$slideOffset")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }
        }
        return bottomSheetCallback
    }


    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    fun sort(sort: Sort) {
        // todo add filters
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result: RealmResults<RealmItemModel>
        if (filterFlag) {
            result = realm.where(RealmItemModel::class.java)
                    .`in`("SpecialtiesList.id", filterArray)
                    .sort("firstName", sort)
                    .findAll()
        } else {
            result = realm.where(RealmItemModel::class.java).sort("firstName", sort).findAll()
        }

        realm.commitTransaction()
        idArray.clear()
        result.forEach { item: RealmItemModel ->
            idArray.add(item._id)
        }
        initList(idArray)
    }

    fun filter1(array: Array<Int>) {
        filterFlag = true
        tv_filter.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result = realm.where(RealmItemModel::class.java)
                .`in`("SpecialtiesList.id", array)
                .sort("firstName", Sort.ASCENDING)
                .findAll()
        realm.commitTransaction()
        val list = ArrayList<Int>()
        result.forEach { item: RealmItemModel ->
            list.add(item._id)
        }

        initList(list)
    }

    var filterFlag = false

    fun clearFilter() {
        filterFlag = false
        tv_filter.setTextColor(ContextCompat.getColor(this, R.color.androidTextColor))
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result: RealmResults<RealmItemModel>
        result = realm.where(RealmItemModel::class.java).sort("firstName", Sort.ASCENDING).findAll()
        realm.commitTransaction()
        val list = ArrayList<Int>()
        result.forEach { item: RealmItemModel ->
            list.add(item._id)
        }

        initList(list)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_filter -> onFilterClick()
            R.id.rl_sort -> onSortClick()
            R.id.iv_goback -> finish()
        }
    }

    lateinit var filterArray: Array<Int>

    private fun onFilterClick() {
        val filterView: View = LayoutInflater.from(this@OfficeActivity).inflate(R.layout.filter, null, false)
        val tList: RecyclerView = filterView.findViewById(R.id.rv_tList)
        val adapter: TAdapter = TAdapter(this@OfficeActivity)
        tList.layoutManager = GridLayoutManager(this@OfficeActivity, 3)
        tList.adapter = adapter

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCustomTitle(LayoutInflater.from(this@OfficeActivity).inflate(R.layout.filter_title, null, false))
        builder.setPositiveButton("باشه", { dialog, which ->
            if (adapter.idsArray.size > 0) {
                filterArray = adapter.idsArray.toTypedArray()
                Log.e("size", "${filterArray.size}")
                filter1(filterArray)
            }

        }).setNegativeButton("نه نمیخوام", { dialog, which ->

        }).setNeutralButton("حذف فیلتر", { dialog, which ->
            clearFilter()
        })
        builder.setView(filterView)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun onSortClick() {
        val sortView: View = LayoutInflater.from(this@OfficeActivity).inflate(R.layout.sort, null, false)
        val rl1: RelativeLayout = sortView.findViewById(R.id.rl_sort1)
        val rl2: RelativeLayout = sortView.findViewById(R.id.rl_sort2)
        val rb1: AppCompatRadioButton = sortView.findViewById(R.id.rb1)
        val rb2: AppCompatRadioButton = sortView.findViewById(R.id.rb2)

        rb1.isClickable = false
        rb2.isClickable = false

        rl1.setOnClickListener({
            rb2.isChecked = false
            rb1.isChecked = true
        })
        rl2.setOnClickListener({
            rb1.isChecked = false
            rb2.isChecked = true
        })

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCustomTitle(LayoutInflater.from(this@OfficeActivity).inflate(R.layout.sort_title, null, false))
        builder.setView(sortView)
        builder.setPositiveButton("باشه", { dialog, which ->
            if (rb1.isChecked) {
                sort(Sort.ASCENDING)
            } else {
                sort(Sort.DESCENDING)
            }
        }).setNegativeButton("نه نمیخوام", { dialog, which ->
        })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(e: String) {
        tabLayoutInterface.hideProgress()
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }


}
