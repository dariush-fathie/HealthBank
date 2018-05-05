package pro.ahoora.zhin.healthbank.models;

import io.realm.RealmObject;

public class RealSpecialties extends RealmObject {

    int id;
    String name;

    public RealSpecialties() {

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
