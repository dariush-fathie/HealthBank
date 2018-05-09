package pro.ahoora.zhin.healthbank.utils;

import java.util.List;

import pro.ahoora.zhin.healthbank.models.GroupModel;
import pro.ahoora.zhin.healthbank.models.ItemModel;
import pro.ahoora.zhin.healthbank.models.Model2;
import pro.ahoora.zhin.healthbank.models.Specialties2;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("hi/{name}")
    Call<Model2> getUsers(@Path("name") String name);

    @GET("getMainGroupList/")
    Call<List<GroupModel>> getGroupCount();

    @GET("getItems/{gId}")
    Call<List<ItemModel>> getItems(@Path("gId") int groupId);

    @GET("getSpList/")
    Call<List<Specialties2>> getSpList();
}
