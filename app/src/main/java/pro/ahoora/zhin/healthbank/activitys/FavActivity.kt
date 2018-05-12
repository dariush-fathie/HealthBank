package pro.ahoora.zhin.healthbank.activitys

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_fav.*
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.adapters.GroupItemSaveAdapter
import pro.ahoora.zhin.healthbank.models.KotlinGroupModel
import pro.ahoora.zhin.healthbank.models.KotlinItemModel


class FavActivity : AppCompatActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener {

    val tabListId = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)
        addTabs()
    }

    private fun addTabs() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        val result = realm.where(KotlinItemModel::class.java)
                .equalTo("saved", true)
                .findAll()

        val groups = realm.where(KotlinItemModel::class.java)
                .equalTo("saved", true)
                .distinct("groupId")
                .findAll()

        realm.commitTransaction()

        tabListId.clear()
        if (result.size > 0) {
            val drawable = ContextCompat.getDrawable(this@FavActivity, R.drawable.ic_all)
            drawable?.setColorFilter(ContextCompat.getColor(this@FavActivity, R.color.green), PorterDuff.Mode.SRC_IN)
            ctb.addTab(ctb.newTab().setText("همه").setIcon(drawable))
            tabListId.add(0)
            groups.forEach { savedItem: KotlinItemModel? ->
                tabListId.add(savedItem?.groupId!!)
                ctb.addTab(ctb.newTab()
                        .setText(getTitleFromDb(savedItem.groupId))
                        .setIcon(getDrawableId(savedItem.groupId)))
            }

        } else {
            Toast.makeText(this, "هیج آیتمی ذخیره نشده است", Toast.LENGTH_LONG).show()
        }

        if (result.size > 0) {
            filter(0)
        }
        ctb.addOnTabSelectedListener(this)
        iv_goback.setOnClickListener(this)
    }


    private fun getTitleFromDb(groupId: Int): String {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val name = realm.where(KotlinGroupModel::class.java)
                .equalTo("groupId", groupId)
                .findFirst()?.name
        realm.commitTransaction()
        return name!!
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        tab?.icon?.setColorFilter(ContextCompat.getColor(this@FavActivity, R.color.title), PorterDuff.Mode.SRC_IN)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.icon?.setColorFilter(ContextCompat.getColor(this@FavActivity, R.color.green), PorterDuff.Mode.SRC_IN)
        Log.e("tab", "${tab?.position} +  s")
        if (tab?.position == 0) {
            filter(0)
        } else {
            val pos = tab?.position
            filter(tabListId.get(pos!!))
        }
    }

    override fun onClick(p0: View?) {
        finish()
    }

    private fun getDrawableId(i: Int): Int {
        when (i) {
            1 -> return R.drawable.ic_office
            2 -> return R.drawable.ic_hospital
            3 -> return R.drawable.ic_centers
            4 -> return R.drawable.ic_net
            5 -> return R.drawable.ic_clinic
            6 -> return R.drawable.ic_support
            7 -> return R.drawable.ic_forum
            8 -> return R.drawable.ic_pharmacy
            9 -> return R.drawable.ic_lab
            10 -> return R.drawable.ic_health
            11 -> return R.drawable.ic_imaging
            12 -> return R.drawable.ic_ctscan
            13 -> return R.drawable.ic_mri
            14 -> return R.drawable.ic_sono
            15 -> return R.drawable.ic_radio
            16 -> return R.drawable.ic_tools
            17 -> return R.drawable.ic_material
            18 -> return R.drawable.ic_emmergency
            19 -> return R.drawable.ic_eye
            20 -> return R.drawable.ic_ear
            21 -> return R.drawable.ic_sport
            22 -> return R.drawable.ic_kidney
            23 -> return R.drawable.ic_atomic
            24 -> return R.drawable.ic_galss
            25 -> return R.drawable.ic_clinic
            26 -> return R.drawable.ic_sport
            27 -> return 0
        }
        return 0
    }

    fun filter(id: Int) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result: RealmResults<KotlinItemModel>
        if (id != 0) {

            result = realm.where(KotlinItemModel::class.java)
                    .equalTo("saved", true)
                    .equalTo("groupId", id).findAll()

        } else {

            result = realm.where(KotlinItemModel::class.java)
                    .equalTo("saved", true).findAll()

        }
        realm.commitTransaction()
        val list = ArrayList<Int>()
        result.forEach { item: KotlinItemModel ->
            list.add(item.centerId)
        }
        initList(list)
    }

    private fun initList(list: ArrayList<Int>) {
        Log.e("size", "${list.size}  l")
        rv_items_marked.layoutManager = LinearLayoutManager(this)
        rv_items_marked.adapter = GroupItemSaveAdapter(this, list)
    }

}
