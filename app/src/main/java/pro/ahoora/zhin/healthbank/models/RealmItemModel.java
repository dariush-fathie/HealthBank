package pro.ahoora.zhin.healthbank.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmItemModel extends RealmObject {

    public RealmItemModel() {

    }

    @PrimaryKey
    private int _id;
    private String naCode;
    private int system_num;
    private String firstName;
    private String lastName;
    private String regDate;
    private String validDate;
    private int active;
    private String logoUrl;
    private String buildingUrl;
    private String shortDesc;
    private String bio;
    private String equipment;
    private String services;
    private String workTeam;
    private String elc_rec;
    private int grade;
    private int groupId;

    public RealmList<RealmAddress> AddressList;
    public RealmList<RealmCInsurance> CInsuranceList;
    public RealmList<RealmLevel> LevelList;
    public RealmList<RealmSlides> SlidesList;
    public RealmList<RealSpecialties> SpecialtiesList;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getNaCode() {
        return naCode;
    }

    public void setNaCode(String naCode) {
        this.naCode = naCode;
    }

    public int getSystem_num() {
        return system_num;
    }

    public void setSystem_num(int system_num) {
        this.system_num = system_num;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getBuildingUrl() {
        return buildingUrl;
    }

    public void setBuildingUrl(String buildingUrl) {
        this.buildingUrl = buildingUrl;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getWorkTeam() {
        return workTeam;
    }

    public void setWorkTeam(String workTeam) {
        this.workTeam = workTeam;
    }

    public String getElc_rec() {
        return elc_rec;
    }

    public void setElc_rec(String elc_rec) {
        this.elc_rec = elc_rec;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public RealmList<RealmAddress> getAddressList() {
        return AddressList;
    }

    public void setAddressList(RealmList<RealmAddress> addressList) {
        AddressList = addressList;
    }

    public RealmList<RealmCInsurance> getCInsuranceList() {
        return CInsuranceList;
    }

    public void setCInsuranceList(RealmList<RealmCInsurance> CInsuranceList) {
        this.CInsuranceList = CInsuranceList;
    }

    public RealmList<RealmLevel> getLevelList() {
        return LevelList;
    }

    public void setLevelList(RealmList<RealmLevel> levelList) {
        LevelList = levelList;
    }

    public RealmList<RealmSlides> getSlidesList() {
        return SlidesList;
    }

    public void setSlidesList(RealmList<RealmSlides> slidesList) {
        SlidesList = slidesList;
    }

    public RealmList<RealSpecialties> getSpecialtiesList() {
        return SpecialtiesList;
    }

    public void setSpecialtiesList(RealmList<RealSpecialties> specialtiesList) {
        SpecialtiesList = specialtiesList;
    }


}
