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
import pro.ahoora.zhin.healthbank.models.KotlinGroupModel
import pro.ahoora.zhin.healthbank.models.KotlinItemModel
import pro.ahoora.zhin.healthbank.utils.ApiInterface
import pro.ahoora.zhin.healthbank.utils.KotlinApiClient
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

    private fun getTitleFromDb(): String {
        Log.e("gid", "$groupId")
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val name = realm.where(KotlinGroupModel::class.java).equalTo("groupId", groupId).findFirst()?.name
        realm.commitTransaction()
        return name!!
    }

    private fun initListener() {
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
            val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
            apiInterface.getItems(groupId).enqueue(object : Callback<List<KotlinItemModel>> {
                override fun onResponse(call: Call<List<KotlinItemModel>>?, response: Response<List<KotlinItemModel>>?) {
                    val list = response?.body()
                    Log.e("success", list.toString() + " res")
                    list?.forEach { itemModel: KotlinItemModel ->
                        Log.e("centerId1", "${itemModel.centerId}")
                    }

                    val realmDatabase = Realm.getDefaultInstance()
                    realmDatabase.executeTransactionAsync { realm: Realm? ->
                        realm?.where(KotlinItemModel::class.java)
                                ?.equalTo("saved", false)
                                ?.findAll()
                                ?.deleteAllFromRealm()
                        realm?.copyToRealmOrUpdate(list!!)
                        val result1 = realm?.where(KotlinItemModel::class.java)?.sort("firstName", Sort.ASCENDING)?.findAll()
                        idArray.clear()
                        result1?.forEach { itemModel: KotlinItemModel? ->
                            idArray.add(itemModel?.centerId!!)
                            Log.e("centerId2", "${itemModel.centerId}")
                        }
                        runOnUiThread {
                            initList(idArray)
                        }
                    }
                }

                override fun onFailure(call: Call<List<KotlinItemModel>>?, t: Throwable?) {
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
        val result: RealmResults<KotlinItemModel>
        if (filterFlag) {
            result = realm.where(KotlinItemModel::class.java)
                    .`in`("specialityList.specialtyId", filterArray)
                    .sort("firstName", sort)
                    .findAll()
        } else {
            result = realm.where(KotlinItemModel::class.java).sort("firstName", sort).findAll()
        }

        realm.commitTransaction()
        idArray.clear()
        result.forEach { item: KotlinItemModel ->
            idArray.add(item.centerId)
        }
        initList(idArray)
    }

    fun filter1(array: Array<Int>) {
        filterFlag = true
        tv_filter.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result = realm.where(KotlinItemModel::class.java)
                .`in`("specialityList.specialtyId", array)
                .sort("firstName", Sort.ASCENDING)
                .findAll()
        realm.commitTransaction()
        val list = ArrayList<Int>()
        result.forEach { item: KotlinItemModel ->
            list.add(item.centerId)
        }

        initList(list)
    }

    var filterFlag = false

    fun clearFilter() {
        filterFlag = false
        tv_filter.setTextColor(ContextCompat.getColor(this, R.color.androidTextColor))
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result: RealmResults<KotlinItemModel>
        result = realm.where(KotlinItemModel::class.java).sort("firstName", Sort.ASCENDING).findAll()
        realm.commitTransaction()
        val list = ArrayList<Int>()
        result.forEach { item: KotlinItemModel ->
            list.add(item.centerId)
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
