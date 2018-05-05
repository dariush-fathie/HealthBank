package pro.ahoora.zhin.healthbank.models;

import com.google.gson.annotations.SerializedName;

public class GroupModel {

    @SerializedName("name")
    private String gName;
    @SerializedName("counter")
    private int count;
    @SerializedName("group_id")
    public int id;

    public String getGName() {
        return gName;
    }

    public void setGName(String gName) {
        this.gName = gName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
