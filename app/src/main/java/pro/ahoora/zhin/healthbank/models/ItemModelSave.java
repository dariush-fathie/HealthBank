package pro.ahoora.zhin.healthbank.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemModelSave {

    @SerializedName("Addr")
    private List<Address> addresses;
    @SerializedName("Slideshow")
    private List<Slides> Slides;
    @SerializedName("C_insurance")
    private List<CInsurance> cInsurances;
    @SerializedName("Specialties")
    private List<Specialties> specialties;
    @SerializedName("Levels")
    private List<Level> levels;
    @SerializedName("center_id")
    private int id;
    @SerializedName("naCode")
    private String naCode;
    @SerializedName("system_num")
    private int system_num;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("reg_date")
    private String regDate;
    @SerializedName("valid_date")
    private String validDate;
    @SerializedName("active")
    private int active;
    @SerializedName("logo_img")
    private String logoUrl;
    @SerializedName("building_img")
    private String buildingUrl;
    @SerializedName("short_desc")
    private String shortDesc;
    @SerializedName("bio")
    private String bio;
    @SerializedName("equipment")
    private String equipment;
    @SerializedName("service_list")
    private String services;
    @SerializedName("work_team")
    private String workTeam;
    @SerializedName("elc_rec")
    private String elc_rec;
    @SerializedName("grade")
    private int grade;
    @SerializedName("group_id")
    private int groupId;

    /*@Override
    public String toString() {
        return
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
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

    public void setGrade(int gradle) {
        this.grade = grade;
    }

    public List<CInsurance> getCInsurances() {
        return cInsurances;
    }

    public void setCInsurances(List<CInsurance> cInsurances) {
        this.cInsurances = cInsurances;
    }

    public List<Specialties> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Specialties> specialties) {
        this.specialties = specialties;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<ItemModelSave.Slides> getSlides() {
        return Slides;
    }

    public void setSlides(List<ItemModelSave.Slides> slides) {
        Slides = slides;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

  public class Address {

        @SerializedName("auto_id")
        int id;
        @SerializedName("loc_title")
        String title;

       /* @SerializedName("region")
        String region;
        @SerializedName("mainSt")
        String mainStreet;
        @SerializedName("bySt")
        String byStreet;
        @SerializedName("alley")
        String alley;
        @SerializedName("building")
        String building;
        @SerializedName("floor")
        String floor;
        @SerializedName("plaque")
        String plaque;*/
        @SerializedName("postal_code")
        String postalCode;
        @SerializedName("tel1")
        String tel1;
        @SerializedName("tel1_desc")
        String tel1Desc;
        @SerializedName("tel2")
        String tel2;
        @SerializedName("tel2_desc")
        String tel2Desc;
        @SerializedName("mob1")
        String mobile1;
        @SerializedName("mob1_desc")
        String mobile1Desc;
        @SerializedName("mob2")
        String mobile2;
        @SerializedName("mob2_desc")
        String mobile2Desc;
        @SerializedName("gen_desc")
        String GenDesc;
        @SerializedName("default_add")
        String defaultAdd;
        @SerializedName("latitude")
        String lat;
        @SerializedName("longitude")
        String lng;
        @SerializedName("site")
        String site;
        @SerializedName("mail")
        String mail;
        @SerializedName("sat_desc")
        String satDesc;
        @SerializedName("sun_desc")
        String sunDesc;
        @SerializedName("mon_desc")
        String monDesc;
        @SerializedName("tues_desc")
        String tuesDesc;
        @SerializedName("wed_desc")
        String wedDesc;
        @SerializedName("thurs_desc")
        String thursDesc;
        @SerializedName("fri_desc")
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
        }
*/
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
            return GenDesc;
        }

        public void setGenDesc(String genDesc) {
            GenDesc = genDesc;
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
    }

   public class Slides {
        @SerializedName("file_url")
        String url;
        @SerializedName("description")
        String description;
        @SerializedName("arrange")
        String arrange;

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

   public class Level {
        @SerializedName("level_id")
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

   public class Specialties {
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

   public class CInsurance {
        @SerializedName("insurance_id")
        int id;
        @SerializedName("name")
        String name;
        @SerializedName("description")
        String description;


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


}
