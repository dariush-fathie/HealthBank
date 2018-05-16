package pro.ahoora.zhin.healthbank.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class SimpleResponseModel {

    @SerializedName("response")
    @Expose
    open var response: String? = ""

}