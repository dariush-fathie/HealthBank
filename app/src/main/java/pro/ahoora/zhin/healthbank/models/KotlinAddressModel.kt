package pro.ahoora.zhin.healthbank.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinAddressModel : RealmObject() {
    @SerializedName("auto_id")
    @Expose
    open var autoId: Int = -1
    @SerializedName("loc_title")
    @Expose
    open var locTitle: String? = ""
    @SerializedName("postal_code")
    @Expose
    open var postalCode: String? = ""
    @SerializedName("tel1")
    @Expose
    open var tel1: String? = ""
    @SerializedName("tel1_desc")
    @Expose
    open var tel1Desc: String?= ""
    @SerializedName("tel2")
    @Expose
    open var tel2: String? = ""
    @SerializedName("tel2_desc")
    @Expose
    open var tel2Desc: String? = ""
    @SerializedName("mob1")
    @Expose
    open var mob1: String? = ""
    @SerializedName("mob1_desc")
    @Expose
    open var mob1Desc: String? = ""
    @SerializedName("mob2")
    @Expose
    open var mob2: String? = ""
    @SerializedName("mod2_desc")
    @Expose
    open var mod2Desc: String? = ""
    @SerializedName("gen_desc")
    @Expose
    open var genDesc: String? = ""
    @SerializedName("default_add")
    @Expose
    open var defaultAdd: String? = ""
    @SerializedName("longitude")
    @Expose
    open var longitude: String? = ""
    @SerializedName("latitude")
    @Expose
    open var latitude: String? = ""
    @SerializedName("site")
    @Expose
    open var site: String? = ""
    @SerializedName("mail")
    @Expose
    open var mail: String?= ""
    @SerializedName("sat_desc")
    @Expose
    open var satDesc: String? = ""
    @SerializedName("sun_desc")
    @Expose
    open var sunDesc: String? = ""
    @SerializedName("mon_desc")
    @Expose
    open var monDesc: String? = ""
    @SerializedName("tues_desc")
    @Expose
    open var tuesDesc: String? = ""
    @SerializedName("wed_desc")
    @Expose
    open var wedDesc: String? = ""
    @SerializedName("thurs_desc")
    @Expose
    open var thursDesc: String? = ""
    @SerializedName("fri_desc")
    @Expose
    open var friDesc: String? = ""

}
