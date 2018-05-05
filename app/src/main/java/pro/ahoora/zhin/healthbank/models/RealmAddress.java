package pro.ahoora.zhin.healthbank.models;

import io.realm.RealmObject;

public class RealmAddress extends RealmObject {

    int id;
    String title;
    /*String region;
    String mainStreet;
    String byStreet;
    String alley;
    String building;
    String floor;
    String plaque;*/
    String postalCode;
    String tel1;
    String tel1Desc;
    String tel2;
    String tel2Desc;
    String mobile1;
    String mobile1Desc;
    String mobile2;
    String mobile2Desc;
    String genDesc;
    String defaultAdd;
    String lat;
    String lng;
    String site;
    String mail;
    String satDesc;
    String sunDesc;
    String monDesc;
    String tuesDesc;
    String wedDesc;
    String thursDesc;
    String friDesc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    /*public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMainStreet() {
        return mainStreet;
    }

    public void setMainStreet(String mainStreet) {
        this.mainStreet = mainStreet;
    }

    public String getByStreet() {
        return byStreet;
    }

    public void setByStreet(String byStreet) {
        this.byStreet = byStreet;
    }

    public String getAlley() {
        return alley;
    }

    public void setAlley(String alley) {
        this.alley = alley;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPlaque() {
        return plaque;
    }

    public void setPlaque(String plaque) {
        this.plaque = plaque;
    }*/

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel1Desc() {
        return tel1Desc;
    }

    public void setTel1Desc(String tel1Desc) {
        this.tel1Desc = tel1Desc;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getTel2Desc() {
        return tel2Desc;
    }

    public void setTel2Desc(String tel2Desc) {
        this.tel2Desc = tel2Desc;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile1Desc() {
        return mobile1Desc;
    }

    public void setMobile1Desc(String mobile1Desc) {
        this.mobile1Desc = mobile1Desc;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getMobile2Desc() {
        return mobile2Desc;
    }

    public void setMobile2Desc(String mobile2Desc) {
        this.mobile2Desc = mobile2Desc;
    }

    public String getGenDesc() {
        return genDesc;
    }

    public void setGenDesc(String genDesc) {
        genDesc = genDesc;
    }

    public String getDefaultAdd() {
        return defaultAdd;
    }

    public void setDefaultAdd(String defaultAdd) {
        this.defaultAdd = defaultAdd;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSatDesc() {
        return satDesc;
    }

    public void setSatDesc(String satDesc) {
        this.satDesc = satDesc;
    }

    public String getSunDesc() {
        return sunDesc;
    }

    public void setSunDesc(String sunDesc) {
        this.sunDesc = sunDesc;
    }

    public String getMonDesc() {
        return monDesc;
    }

    public void setMonDesc(String monDesc) {
        this.monDesc = monDesc;
    }

    public String getTuesDesc() {
        return tuesDesc;
    }

    public void setTuesDesc(String tuesDesc) {
        this.tuesDesc = tuesDesc;
    }

    public String getWedDesc() {
        return wedDesc;
    }

    public void setWedDesc(String wedDesc) {
        this.wedDesc = wedDesc;
    }

    public String getThursDesc() {
        return thursDesc;
    }

    public void setThursDesc(String thursDesc) {
        this.thursDesc = thursDesc;
    }

    public String getFriDesc() {
        return friDesc;
    }

    public void setFriDesc(String friDesc) {
        this.friDesc = friDesc;
    }



    public RealmAddress() {
    }
}
