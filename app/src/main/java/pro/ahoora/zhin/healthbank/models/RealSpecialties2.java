package pro.ahoora.zhin.healthbank.models;

import io.realm.RealmObject;

public class RealSpecialties2 extends RealmObject {

    private int id;
    private String name;

    public RealSpecialties2() {

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
