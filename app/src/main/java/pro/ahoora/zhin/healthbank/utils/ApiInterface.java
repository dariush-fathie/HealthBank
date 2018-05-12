package pro.ahoora.zhin.healthbank.utils;

import java.util.List;

import pro.ahoora.zhin.healthbank.models.KotlinGroupModel;
import pro.ahoora.zhin.healthbank.models.KotlinItemModel;
import pro.ahoora.zhin.healthbank.models.KotlinSpecialityModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {


    @GET("getMainGroupList/")
    Call<List<KotlinGroupModel>> getGroupCount();

    @GET("getItems/{gId}")
    Call<List<KotlinItemModel>> getItems(@Path("gId") int groupId);

    @GET("getSpList/")
    Call<List<KotlinSpecialityModel>> getSpList();

    @GET("search/{someThing}")
    Call<List<KotlinItemModel>> search(@Path("someThing") String searchedText);

    @GET("search/{someThing}")
    Callback<String> search1(@Path("someThing") String searchedText);

}
