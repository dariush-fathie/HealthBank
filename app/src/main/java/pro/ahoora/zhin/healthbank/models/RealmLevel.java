package pro.ahoora.zhin.healthbank.models;

import io.realm.RealmObject;

public class RealmLevel extends RealmObject {
    int id;
    String name;

    public RealmLevel() {

    }

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
