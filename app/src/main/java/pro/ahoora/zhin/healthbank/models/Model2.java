package pro.ahoora.zhin.healthbank.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model2 {
    @SerializedName("name")
    public List<String> name;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
}
