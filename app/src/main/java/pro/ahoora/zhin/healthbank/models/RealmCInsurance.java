package pro.ahoora.zhin.healthbank.models;

import io.realm.RealmObject;

public class RealmCInsurance extends RealmObject {
    int id;
    String name;
    String description;

    public RealmCInsurance() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
