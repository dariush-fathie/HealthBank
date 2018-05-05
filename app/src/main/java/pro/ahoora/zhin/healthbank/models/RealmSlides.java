package pro.ahoora.zhin.healthbank.models;

import io.realm.RealmObject;

public class RealmSlides extends RealmObject {


    String url;
    String description;
    String arrange;

    public RealmSlides() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArrange() {
        return arrange;
    }

    public void setArrange(String arrange) {
        this.arrange = arrange;
    }
}
