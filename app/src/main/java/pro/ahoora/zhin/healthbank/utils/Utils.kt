package pro.ahoora.zhin.healthbank.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import io.realm.Realm
import pro.ahoora.zhin.healthbank.activitys.DetailActivity
import pro.ahoora.zhin.healthbank.models.*

object Utils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun saveItem(ctx: Context, id: Int) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction({ realmDatabase ->
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
            }
            item.levelList.forEach { itemL: RealmLevel ->
                val realmLevel = realm?.createObject(RealmLevel::class.java)
                realmLevel?.id = itemL.id
                realmLevel?.name = itemL.name
                newItem.LevelList?.add(realmLevel)
            }

            val i = realm.where(RealmItemModelSave::class.java).equalTo("_id", id).findAll().count()
            Log.e("count", "$i" + " ")
            (ctx as DetailActivity).runOnUiThread({
                Toast.makeText(ctx, "آیتم با موفقیت ذخیره شد ", Toast.LENGTH_SHORT).show()
            })
        })
    }

    fun deleteSavedItem(id: Int) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where(RealmItemModelSave::class.java).equalTo("_id", id).findFirst()?.deleteFromRealm()
        realm.commitTransaction()
    }

    fun isItemSaved(id: Int): Boolean {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val item = realm.where(RealmItemModelSave::class.java).equalTo("_id", id).findFirst()
        realm.commitTransaction()
        try {
            val name = item?.firstName
            return !name.equals("")
        } catch (e: Exception) {
            Log.e("Utils", "item not Saved")
            return false
        }
    }
}
