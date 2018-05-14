package pro.ahoora.zhin.healthbank.activitys

import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.net_err_layout.*
import kotlinx.android.synthetic.main.progress_layout.*
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.customClasses.GridItemDecoration
import pro.ahoora.zhin.healthbank.customClasses.OnSpinerItemClick
import pro.ahoora.zhin.healthbank.customClasses.SpinnerDialog
import pro.ahoora.zhin.healthbank.models.KotlinAboutContactModel
import pro.ahoora.zhin.healthbank.models.KotlinGroupModel
import pro.ahoora.zhin.healthbank.models.KotlinSpecialityModel
import pro.ahoora.zhin.healthbank.utils.ApiInterface
import pro.ahoora.zhin.healthbank.utils.KotlinApiClient
import pro.ahoora.zhin.healthbank.utils.StaticValues
import pro.ahoora.zhin.healthbank.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var adapter: CategoryAdapter

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_more -> loadAllCategory()
            R.id.btn_fav -> openDrawerLayout()
            R.id.rl_exit -> showExitDialog()
            R.id.btn_tryAgain -> tryAgain()
            R.id.rl_drawer1 -> drawerClick(0)
            R.id.rl_drawer2 -> drawerClick(1)
            R.id.rl_drawer3 -> drawerClick(2)
            R.id.rl_drawer4 -> drawerClick(3)
            R.id.iv_menu -> openDrawerLayout()
            R.id.fab_search -> search()
            R.id.rl_salamat -> startActivity(Intent(this@MainActivity, HeaIncServiceActivity::class.java))
            R.id.tv_city -> openCityDialog()
            R.id.tv_prov -> openProvDialog()
        }
    }

    private fun showExitDialog() {
        closeDrawerLayout()
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("مطمئنید می خواهید از برنامه خارج شوید ؟")
                .setPositiveButton("نه نمیخوام", DialogInterface.OnClickListener({ dialog, _ ->
                    dialog.dismiss()
                }))
                .setNegativeButton("بله خارج میشوم", DialogInterface.OnClickListener({ _, _ ->
                    finish()
                }))
        alertDialog.show()
    }


    private fun openCityDialog() {
        val cityArray = ArrayList<String>()
        cityArray.add("سنندج")
        cityArray.add("سقز")
        cityArray.add("قروه")
        cityArray.add("مریوان")
        cityArray.add("بانه")
        cityArray.add("کامیاران")
        cityArray.add("بیجار")
        cityArray.add("دیواندره")
        cityArray.add("دهگلان")
        val dialog = SpinnerDialog(this@MainActivity, cityArray, "شهر خود را انتخاب کنید :", "نه نمیخوام")
        dialog.bindOnSpinerListener(OnSpinerItemClick { name, index ->
            tv_city.text = name
        })
        dialog.showSpinerDialog()
    }

    private fun openProvDialog() {

    }

    private fun drawerClick(position: Int) {
        closeDrawerLayout()
        Log.e("hggh", "$position")
        when (position) {
            0 -> closeDrawerLayout()
            1 -> startActivity(Intent(this@MainActivity, FavActivity::class.java))
            2 -> startActivity(Intent(this@MainActivity, AboutUs::class.java))
            3 -> startActivity(Intent(this@MainActivity, ContactUs::class.java))
        }
    }

    private fun tryAgain() {
        initList()
    }

    private fun showNetErrLayout() {
        btn_more.visibility = View.GONE
        ll_netErr.visibility = View.VISIBLE
    }

    private fun hideNetErrLayout() {
        btn_more.visibility = View.VISIBLE
        ll_netErr.visibility = View.GONE
    }

    private fun openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private fun closeDrawerLayout() {
        drawerLayout.closeDrawers()
    }

    private fun loadAllCategory() {
        adapter.flagLoadAll = true
        adapter.notifyItemRangeChanged(6, 14)
        btn_more.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, SplashScreen::class.java))
        setContentView(R.layout.activity_main)
        setClickListeners()
        initList()

    }


    private fun setClickListeners() {
        rl_exit.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        rl_drawer1.setOnClickListener(this)
        rl_drawer2.setOnClickListener(this)
        rl_drawer3.setOnClickListener(this)
        rl_drawer4.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)
        btn_more.setOnClickListener(this)
        btn_tryAgain.setOnClickListener(this)
        btn_fav.setOnClickListener(this)
        fab_search.setOnClickListener(this)
        tv_prov.setOnClickListener(this)
        tv_city.setOnClickListener(this)
    }

    private fun search() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    private fun initList() {
        if (Utils.isNetworkAvailable(this@MainActivity)) {
            getGroupCount()
            getSpList()// from server
            getAboutContact()
        } else {
            showNetErrLayout()
            Toast.makeText(this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProgressLayout() {
        ll_progressLayout.visibility = View.VISIBLE
    }

    private fun hideProgressLayout() {
        ll_progressLayout.visibility = View.GONE
    }

    private fun getSpList() {
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        val response = apiInterface.spList
        response.enqueue(object : Callback<List<KotlinSpecialityModel>> {
            override fun onResponse(call: Call<List<KotlinSpecialityModel>>?, response: Response<List<KotlinSpecialityModel>>?) {
                val list: List<KotlinSpecialityModel>? = response?.body()
                val realmDatabase = Realm.getDefaultInstance()
                realmDatabase.executeTransactionAsync { realm: Realm? ->
                    realm?.copyToRealmOrUpdate(list!!)
                    /*val savedSps = realm?.where(KotlinSpecialityModel::class.java)?.findAll()
                    realm?.where(KotlinSpecialityModel::class.java)?.findAll()?.deleteAllFromRealm()
                    list?.forEach { spl: KotlinSpecialityModel ->
                        spl.saved = true
                        realm?.copyToRealm(spl)
                    }*/
                    /*val r = realm?.where(KotlinSpecialityModel::class.java)?.findAll()
                    r!!.forEach { model: KotlinSpecialityModel? ->
                        Log.e("SP", "${model?.name}:${model?.specialtyId}:${model?.saved}")
                    }
                    */
                }
            }

            override fun onFailure(call: Call<List<KotlinSpecialityModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
            }
        })
    }

    private fun getGroupCount() {
        showProgressLayout()
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        val response = apiInterface.groupCount
        response.enqueue(object : Callback<List<KotlinGroupModel>> {
            override fun onResponse(call: Call<List<KotlinGroupModel>>?, response: Response<List<KotlinGroupModel>>?) {
                val list: List<KotlinGroupModel>? = response?.body()
                val realm = Realm.getDefaultInstance()
                realm.executeTransactionAsync { db: Realm? ->
                    db?.where(KotlinGroupModel::class.java)?.findAll()?.deleteAllFromRealm()
                    db?.copyToRealm(list!!)

                    /*val r = db?.where(KotlinGroupModel::class.java)?.findAll()
                    r?.forEach { model: KotlinGroupModel? ->
                        Log.e("GM", "${model?.name}:${model?.groupId}")
                    }*/
                }
                loadAdapter(list!!)
                hideProgressLayout()
                hideNetErrLayout()
            }

            override fun onFailure(call: Call<List<KotlinGroupModel>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
                Log.e(ERR, t?.message + " aaa")
                hideProgressLayout()
                showNetErrLayout()
            }
        })
    }

    private fun getAboutContact() {
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        val response = apiInterface.ac
        response.enqueue(object : Callback<KotlinAboutContactModel> {
            override fun onResponse(call: Call<KotlinAboutContactModel>?, response: Response<KotlinAboutContactModel>?) {
                val list: KotlinAboutContactModel? = response?.body()
                val realm = Realm.getDefaultInstance()
                realm.executeTransactionAsync { db: Realm? ->
                    db?.where(KotlinAboutContactModel::class.java)?.findAll()?.deleteAllFromRealm()
                    db?.copyToRealm(list!!)
                }

            }

            override fun onFailure(call: Call<KotlinAboutContactModel>?, t: Throwable?) {
                Log.e("about", t.toString());
            }
        })
    }

    val ERR = "ERROR>>"
    private fun loadAdapter(list: List<KotlinGroupModel>) {
        adapter = CategoryAdapter(list)
        rv_category.layoutManager = GridLayoutManager(this, 2)
        val itemDecoration = GridItemDecoration(this, 10)
        rv_category.addItemDecoration(itemDecoration)
        rv_category.adapter = adapter
    }

    inner class CategoryAdapter(gList: List<KotlinGroupModel>) : RecyclerView.Adapter<CategoryAdapter.ItemHolder>() {

        var groupsList: List<KotlinGroupModel> = gList
        var flagLoadAll = false

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view: View = LayoutInflater.from(this@MainActivity).inflate(R.layout.main_category_item, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount(): Int {

            if (flagLoadAll) {
                rv_category.overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
                return groupsList.size
            }
            rv_category.overScrollMode = View.OVER_SCROLL_NEVER
            return 6

        }


        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            if (groupsList.size > position) {
                holder.subTitle.text = "${groupsList.get(position).counter}  مورد"
                holder.titleTv.text = groupsList.get(position).name
            } else {
                holder.subTitle.text = "0  مورد"
            }
            setAnimation(holder.container, position)

            val i = groupsList.get(position).groupId
            Log.e("i", "$i")
            Glide.with(this@MainActivity).load(getDrawableId(i)).into(holder.imageView)
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
                21 -> return R.drawable.ic_speak
                22 -> return R.drawable.ic_kidney
                23 -> return R.drawable.ic_atomic
                24 -> return R.drawable.ic_galss
                25 -> return R.drawable.ic_clinic
                26 -> return R.drawable.ic_sport
                27 -> return 0
            }
            return 0
        }

        private fun setAnimation(viewToAnimate: View, position: Int) {
            if (position > 6) {
                val a = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 200f, 100f, 50f, 0f)
                a.duration = 400
                a.start()
            }
        }


        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            override fun onClick(v: View?) {
                // start list activity
                val i = Intent(this@MainActivity, OfficeActivity::class.java)
                i.putExtra(StaticValues.CATEGORY, groupsList.get(adapterPosition).groupId)
                startActivity(i)
            }

            val imageView: AppCompatImageView = itemView.findViewById(R.id.iv_mainCategoryImage)
            val titleTv: AppCompatTextView = itemView.findViewById(R.id.tv_mainCategoryTitle)
            val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_mainCategorySubTitle)
            val container: CardView = itemView.findViewById(R.id.cv_mainCategoryContainer)

            init {
                container.setOnClickListener(this)
            }
        }

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            closeDrawerLayout()
        } else {
            super.onBackPressed()
        }
    }

}


