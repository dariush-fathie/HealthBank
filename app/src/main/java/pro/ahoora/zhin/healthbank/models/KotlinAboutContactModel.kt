package pro.ahoora.zhin.healthbank.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinAboutContactModel : RealmObject() {

    @SerializedName("id")
    @Expose
    open var id: Int? = 1
    @SerializedName("tAbout")
    @Expose
    open var tAbout: String? = ""
    @SerializedName("tContact")
    @Expose
    open var tContact: String? = ""
}

