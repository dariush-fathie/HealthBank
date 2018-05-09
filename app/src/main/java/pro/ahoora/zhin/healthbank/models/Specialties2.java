package pro.ahoora.zhin.healthbank.models;

import com.google.gson.annotations.SerializedName;

public class Specialties2 {
    @SerializedName("specialty_id")
    int id;
    @SerializedName("name")
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
