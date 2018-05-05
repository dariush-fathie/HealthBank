package pro.ahoora.zhin.healthbank.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GroupModel_Realm extends RealmObject {

    private String name;
    private int count;
    private int id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
