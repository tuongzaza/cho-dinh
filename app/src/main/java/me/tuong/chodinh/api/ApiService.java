package me.tuong.chodinh.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import me.tuong.chodinh.login.model.ServerResponse;
import me.tuong.chodinh.login.model.User;
import me.tuong.chodinh.main.MyResponse;
import me.tuong.chodinh.main.home.model.Category;
import me.tuong.chodinh.main.home.model.Favorite;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.model.Province;
import me.tuong.chodinh.main.home.model.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder().baseUrl("http://domain.com/").addConverterFactory(GsonConverterFactory.create(gson)).build().create(ApiService.class);

    @GET("jsonuserlogin.php")
    Call<List<User>> getListUser();

    @GET("jsoncategory.php")
    Call<List<Category>> getCategory();

    @GET("Api.php?apicall=getallimages")
    Call<List<UserInfo>> getListUserInfo();

    @GET("jsonpost.php")
    Call<List<Post>> getPost();

    @GET("jsonpostsold.php")
    Call<List<Post>> getPostSold();

    @GET("addresslite.json")
    Call<List<Province>> getAddress();

    @GET("jsonfavorite.php")
    Call<List<Favorite>> getFavoritePost();

    @Multipart
    @POST("Api.php?apicall=upload")
    Call<MyResponse> updateUser(@Part MultipartBody.Part avatar,
                                @Part(Const.KEY_SDT) RequestBody email,
                                @Part(Const.KEY_ADDRESS) RequestBody address,
                                @Part(Const.KEY_NAME) RequestBody name,
                                @Part(Const.KEY_EMAIL) RequestBody emailreal,
                                @Part(Const.KEY_PASSWORD) RequestBody password);

    @FormUrlEncoded
    @POST("updatetoken.php")
    Call<ServerResponse> updateToken(@Field("token") String token, @Field("email") String strSdt);

    @FormUrlEncoded
    @POST("insetnews.php")
    Call<ServerResponse> insertNews(@Field("title") String title,
                                    @Field("description") String description,
                                    @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("updatedong.php")
    Call<ServerResponse> updateDong(@Field("dong") String token, @Field("email") String strSdt);

    @FormUrlEncoded
    @POST("updateTime.php")
    Call<ServerResponse> updateTimme(@Field("top") long time, @Field("id") String post_id);
}
