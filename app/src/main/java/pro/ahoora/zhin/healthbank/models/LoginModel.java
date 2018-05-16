package pro.ahoora.zhin.healthbank.models;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
    @SerializedName("x")
    String val;
}
