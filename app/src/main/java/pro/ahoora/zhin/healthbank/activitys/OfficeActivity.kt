package pro.ahoora.zhin.healthbank.activitys

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
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
    private var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office)

        initListener()

        if (intent != null) {
            groupId = intent.getIntExtra(StaticValues.CATEGORY, 0)
        }

        ctbl.addTab(ctbl.newTab().setText("روی نقشه").setIcon(R.drawable.ic_map))
        if (groupId == 0) {
            ctbl.addTab(ctbl.newTab().setText("تخصص ها").setIcon(R.drawable.ic_category))
            flag = true
        }
        ctbl.addTab(ctbl.newTab().setText("کاوش").setIcon(R.drawable.ic_search))
        initBottomSheet()
        tabLayoutInterface = TabLayoutInterface(this, supportFragmentManager, bottomSheetBehavior, ll_progress, flag)
        ctbl.addOnTabSelectedListener(tabLayoutInterface)

        getItems()
    }

    private fun initListener(): Unit {
        rl_filter.setOnClickListener(this)
        rl_sort.setOnClickListener(this)
        iv_goback.setOnClickListener(this)
    }

    private fun initList(list: ArrayList<Int>) {
        rv_items.layoutManager = LinearLayoutManager(this)
        rv_items.adapter = GroupItemAdapter(this, list)
    }

    private fun getItems() {
        if (Utils.isNetworkAvailable(this)) {
            val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
            apiInterface.getItems(groupId + 1).enqueue(object : Callback<List<ItemModel>> {
                override fun onResponse(call: Call<List<ItemModel>>?, response: Response<List<ItemModel>>?) {
                    val list = response?.body()
                    Log.e("success", list.toString() + " res")

                    val realmDatabase = Realm.getDefaultInstance()
                    val idArray = ArrayList<Int>()
                    realmDatabase.executeTransactionAsync { realm: Realm? ->
                        realm?.where(RealmItemModel::class.java)?.findAll()?.deleteAllFromRealm()
                        list?.forEach { item: ItemModel ->
                            // inserting each record to database
                            val realmItemModel = realm?.createObject(RealmItemModel::class.java, item.id)
                            Log.e("itemId", "${item.id}")
                            idArray.add(item.id)
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
                        runOnUiThread {
                            Log.e("size", "${idArray.size}")
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

    var filterField = ""
    var filterValue = ""
    var sortField = ""
    var sort: Sort = Sort.ASCENDING // default sort

    fun sort(fieldName: String, sort: Sort) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result = realm.where(RealmItemModel::class.java).sort("", sort).findAll()
        realm.commitTransaction()
        val list = ArrayList<Int>()
        result.forEach { item: RealmItemModel ->
            list.add(item._id)
        }
        initList(list)
    }

    fun filter(fieldName: String, fieldValue: String) {
        filterField = fieldName
        filterValue = fieldValue
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result = realm.where(RealmItemModel::class.java).equalTo(fieldName, fieldValue).findAll()
        realm.commitTransaction()
        val list = ArrayList<Int>()
        result.forEach { item: RealmItemModel ->
            list.add(item._id)
        }
        initList(list)
    }

    fun clearSort() {
        sortField = ""
        sort = Sort.ASCENDING

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result: RealmResults<RealmItemModel>
        if (filterField != "") {
            result = realm.where(RealmItemModel::class.java).equalTo(filterField, filterValue).findAll()
        } else {
            result = realm.where(RealmItemModel::class.java).findAll()
        }

        realm.commitTransaction()
        val list = ArrayList<Int>()
        result.forEach { item: RealmItemModel ->
            list.add(item._id)
        }
        initList(list)

    }

    fun clearFilter() {
        filterValue = ""
        filterField = ""
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result: RealmResults<RealmItemModel>
        if (sortField != "") {
            result = realm.where(RealmItemModel::class.java).sort(sortField, sort).findAll()
        } else {
            result = realm.where(RealmItemModel::class.java).findAll()
        }

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

    private fun onFilterClick() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("فیلترینگ")

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun onSortClick() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("مرتب سازی بر اساس :")
        builder.setSingleChoiceItems(arrayOf("مهم نیست","حروف الفبا- نزولی", "حروف الفبا - صعودی", "att1", "attr2"), 0, { dialog, which ->

        })
        builder.setPositiveButton("باشه", { dialog, which ->
        })
        builder.setNegativeButton("نه نمیخوام", { dialog, which ->

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
