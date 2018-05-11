package pro.ahoora.zhin.healthbank.activitys

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_search.*
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.models.KotlinGroupModel
import pro.ahoora.zhin.healthbank.models.KotlinItemModel
import pro.ahoora.zhin.healthbank.utils.ApiInterface
import pro.ahoora.zhin.healthbank.utils.KotlinApiClient
import pro.ahoora.zhin.healthbank.utils.StaticValues
import pro.ahoora.zhin.healthbank.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search)
        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (et_search.text.toString() != "") {
                    getItems()
                    Utils.closeKeyBoard(et_search.windowToken, this@SearchActivity)
                } else {
                    Toast.makeText(this@SearchActivity, "لطفا نام دکتر را وارد کنید , ", Toast.LENGTH_SHORT).show()
                }
            }
            false
        }
    }


    private fun getItems() {
        if (Utils.isNetworkAvailable(this@SearchActivity)) {
            downloadItem()
        } else {
            tv_itemNums.text = "به اینترنت متصل نیستید"
        }

    }

    private fun downloadItem() {
        cpv_progress.visibility = View.VISIBLE
        KotlinApiClient.client.create(ApiInterface::class.java).search(et_search.text.toString()).enqueue(object : Callback<List<KotlinItemModel>> {
            override fun onResponse(call: Call<List<KotlinItemModel>>?, response: Response<List<KotlinItemModel>>?) {
                val resultList = response?.body()
                if (resultList?.size!! > 0) {
                    tv_itemNums.text = "${resultList.size} مورد یافت شد ."
                    // holding the pair of <groupId,count> for use in adapter
                    val gIdCountPair = ArrayList<Pair<Int, Int>>()
                    // list of all ids in result
                    val idsArray = ArrayList<Int>()
                    // list of unique id in result file
                    val uniqueIds = ArrayList<Int>()

                    resultList.forEach { model: KotlinItemModel ->
                        Log.e("name ", model.firstName + " " + model.lastName)
                        //collect all ids for cound later
                        idsArray.add(model.groupId)
                        // adding unique id to list
                        if (!uniqueIds.contains(model.groupId)) {
                            uniqueIds.add(model.groupId)
                        }
                    }

                    // counting unique ids and put into gIdCountPairs list
                    for (i in 0 until uniqueIds.size) {
                        val gId = uniqueIds[i]
                        var count = 0
                        for (j in 0 until idsArray.size) {
                            if (idsArray[j] == gId) {
                                count++
                            }
                        }
                        gIdCountPair.add(Pair(gId, count))
                    }

                    gIdCountPair.forEach { pair: Pair<Int, Int> ->
                        Log.e("gid: ${pair.first}", "count ${pair.second}")
                    }

                    loadAdapter(gIdCountPair, resultList)
                } else {
                    tv_itemNums.text = "متاسفانه گزینه ای یافت نشد "
                }
                cpv_progress.visibility = View.GONE
                view_shadow.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<List<KotlinItemModel>>?, t: Throwable?) {
                view_shadow.visibility = View.GONE
                Toast.makeText(this@SearchActivity, " ", Toast.LENGTH_SHORT).show()
                cpv_progress.visibility = View.GONE
            }
        })

    }


    fun loadAdapter(pairList: ArrayList<Pair<Int, Int>>, data: List<KotlinItemModel>): Unit {
        rv_search.layoutManager = LinearLayoutManager(this)
        rv_search.adapter = SearchAdapterWithHeader(pairList, data)

    }

    companion object {
        lateinit var tempModel: KotlinItemModel
    }

    inner class SearchAdapterWithHeader(pairList: ArrayList<Pair<Int, Int>>, data: List<KotlinItemModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        /**
         * @param dataSet contain all kotlinRealmModel
         */
        private val dataSet = data
        private val gIdCountPairList = pairList
        private var totalCount = 0
        private val headerPosition = ArrayList<Int>();

        init {
            val headerCount = gIdCountPairList.size
            val itemCount = data.size
            totalCount = headerCount + itemCount
            placeHeadersItem()
        }

        // find headers places in recyclerView
        private fun placeHeadersItem() {
            headerPosition.add(0)
            for (i in 1..(gIdCountPairList.size - 1)) {
                val next = getPreviousCount(i - 1)
                headerPosition.add(next + 1 + headerPosition[i - 1])
            }
        }

        // find header index in gIdCountPairList
        private fun findHeaderItemIndex(position: Int): Int {
            for (i in 0 until headerPosition.size) {
                if (headerPosition[i] == position) {
                    return i
                }
            }
            return -1
        }

        // convert groupId to title
        private fun getTitleFromDb(groupId: Int): String {
            var name = ""
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction({ realmDatabase: Realm? ->
                name = realmDatabase?.where(KotlinGroupModel::class.java)?.equalTo("groupId", groupId)?.findFirst()?.name!!
            })
            return name
        }

        // find real position of items (the default position is not true because some headers injected to recyclerView)
        private fun findRealItemPosition(position: Int): Int {
            var p = position
            for (i in 0 until headerPosition.size) {
                if (position > headerPosition[i]) {
                    p--
                } else {
                    break
                }
            }
            return p
        }

        // return the count of previous gId count
        private fun getPreviousCount(index: Int): Int {
            return gIdCountPairList[index].second
        }

        // if the position is in the headerPosition we most return true to add header item to recylcerView
        private fun isHeaderItem(position: Int): Boolean {
            if (headerPosition.contains(position))
                return true
            return false
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                0 -> HeaderItemHolder(LayoutInflater.from(this@SearchActivity).inflate(R.layout.header_layout, parent, false))
                else -> ItemHolder(LayoutInflater.from(this@SearchActivity).inflate(R.layout.group_item, parent, false))
            }
        }

        override fun getItemCount(): Int {
            // return total of headers + items
            return totalCount
        }

        /**
         * viewType = 0 -> header Item
         * viewType = 1 -> normal item
         */
        override fun getItemViewType(position: Int): Int {
            if (isHeaderItem(position)) {
                return 0
            }
            return 1
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is HeaderItemHolder) {
                val pair = gIdCountPairList[findHeaderItemIndex(position)]
                holder.headerTitle.text = getTitleFromDb(pair.first)
                holder.headerItemCount.text = "${pair.second} مورد"
            } else {
                val realPosition = findRealItemPosition(position)
                (holder as ItemHolder).title.text = dataSet[realPosition].firstName + " " + dataSet[realPosition].lastName
                holder.subTitle.text = dataSet[realPosition].specialityList?.get(0)?.name
            }
        }

        inner class HeaderItemHolder(headerView: View) : RecyclerView.ViewHolder(headerView) {
            val headerTitle: AppCompatTextView = headerView.findViewById(R.id.tv_header)
            val headerItemCount: AppCompatTextView = headerView.findViewById(R.id.tv_headerItemCount)
        }

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            override fun onClick(v: View?) {
                val realPosition = findRealItemPosition(adapterPosition)
                tempModel = dataSet[realPosition]
                val i = Intent(this@SearchActivity, DetailActivity::class.java)
                i.putExtra(StaticValues.MODEL, 2)
                i.putExtra(StaticValues.ID, tempModel.centerId)
                startActivity(i)
            }

            val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
            val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_subTitle)
            val t: AppCompatTextView = itemView.findViewById(R.id.tv_t)
            val item: RelativeLayout = itemView.findViewById(R.id.rl_item)

            init {
                item.setOnClickListener(this)
            }
        }


    }


}
