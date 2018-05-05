package pro.ahoora.zhin.healthbank.models;

public class MappingUtils {


    public static GroupModel_Realm convertGroupModel(GroupModel model) {
        GroupModel_Realm r = new GroupModel_Realm();
        r.setCount(model.getCount());
        r.setId(model.getId());
        r.setName(model.getGName());
        return r;
    }


}
