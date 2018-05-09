package pro.ahoora.zhin.healthbank.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Addr {
    @SerializedName("auto_id")
    @Expose
    open var autoId: String? = null
    @SerializedName("loc_title")
    @Expose
    open var locTitle: String? = null
    @SerializedName("postal_code")
    @Expose
    open var postalCode: String? = null
    @SerializedName("tel1")
    @Expose
    open var tel1: String? = null
    @SerializedName("tel1_desc")
    @Expose
    open var tel1Desc: String? = null
    @SerializedName("tel2")
    @Expose
    open var tel2: String? = null
    @SerializedName("tel2_desc")
    @Expose
    open var tel2Desc: String? = null
    @SerializedName("mob1")
    @Expose
    open var mob1: String? = null
    @SerializedName("mob1_desc")
    @Expose
    open var mob1Desc: String? = null
    @SerializedName("mob2")
    @Expose
    open var mob2: String? = null
    @SerializedName("mod2_desc")
    @Expose
    open var mod2Desc: String? = null
    @SerializedName("gen_desc")
    @Expose
    open var genDesc: String? = null
    @SerializedName("default_add")
    @Expose
    open var defaultAdd: String? = null
    @SerializedName("longitude")
    @Expose
    open var longitude: String? = null
    @SerializedName("latitude")
    @Expose
    open var latitude: String? = null
    @SerializedName("site")
    @Expose
    open var site: String? = null
    @SerializedName("mail")
    @Expose
    open var mail: String? = null
    @SerializedName("sat_desc")
    @Expose
    open var satDesc: String? = null
    @SerializedName("sun_desc")
    @Expose
    open var sunDesc: String? = null
    @SerializedName("mon_desc")
    @Expose
    open var monDesc: String? = null
    @SerializedName("tues_desc")
    @Expose
    open var tuesDesc: String? = null
    @SerializedName("wed_desc")
    @Expose
    open var wedDesc: String? = null
    @SerializedName("thurs_desc")
    @Expose
    open var thursDesc: String? = null
    @SerializedName("fri_desc")
    @Expose
    open var friDesc: String? = null

}

@RealmClass
open class CInsurance {

    @SerializedName("insurance_id")
    @Expose
    open var insuranceId: String? = null
    @SerializedName("name")
    @Expose
    open var name: String? = null
    @SerializedName("description")
    @Expose
    open var description: String? = null

}

@RealmClass
open class KotlinModel {

    @PrimaryKey
    @SerializedName("center_id")
    @Expose
    open var centerId: String? = null
    @SerializedName("naCode")
    @Expose
    open var naCode: String? = null
    @SerializedName("system_num")
    @Expose
    open var systemNum: String? = null
    @SerializedName("firstName")
    @Expose
    open var firstName: String? = null
    @SerializedName("lastName")
    @Expose
    open var lastName: String? = null
    @SerializedName("reg_date")
    @Expose
    open var regDate: String? = null
    @SerializedName("valid_date")
    @Expose
    open var validDate: Any? = null
    @SerializedName("active")
    @Expose
    open var active: String? = null
    @SerializedName("logo_img")
    @Expose
    open var logoImg: String? = null
    @SerializedName("building_img")
    @Expose
    open var buildingImg: String? = null
    @SerializedName("short_desc")
    @Expose
    open var shortDesc: String? = null
    @SerializedName("bio")
    @Expose
    open var bio: String? = null
    @SerializedName("equipment")
    @Expose
    open var equipment: Any? = null
    @SerializedName("service_list")
    @Expose
    open var serviceList: Any? = null
    @SerializedName("work_team")
    @Expose
    open var workTeam: String? = null
    @SerializedName("elc_rec")
    @Expose
    open var elcRec: Any? = null
    @SerializedName("grade")
    @Expose
    open var grade: String? = null
    @SerializedName("group_id")
    @Expose
    open var groupId: String? = null
    @SerializedName("Addr")
    @Expose
    open var addr: List<Addr>? = null
    @SerializedName("Slideshow")
    @Expose
    open var slideshow: List<Slideshow>? = null
    @SerializedName("Specialties")
    @Expose
    open var specialties: List<Specialty>? = null
    @SerializedName("Levels")
    @Expose
    open var levels: List<Level>? = null
    @SerializedName("C_insurance")
    @Expose
    open var cInsurance: List<CInsurance>? = null

}

@RealmClass
open class Level {
    @SerializedName("level_id")
    @Expose
    open var levelId: String? = null
    @SerializedName("name")
    @Expose
    open var name: String? = null
}

@RealmClass
open class Slideshow {

    @SerializedName("file_url")
    @Expose
    open var fileUrl: String? = null
    @SerializedName("description")
    @Expose
    open var description: String? = null
    @SerializedName("arrange")
    @Expose
    open var arrange: String? = null

}

@RealmClass
open class Specialty {
    @SerializedName("specialty_id")
    @Expose
    open var specialtyId: String? = null
    @SerializedName("name")
    @Expose
    open var name: String? = null

}