package me.tuong.chodinh.main.more.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.main.home.model.Favorite;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.more.minterface.FavoriteInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;

public class FavoritePresenter {
    private FavoriteInterface favoriteInterface;

    public FavoritePresenter(FavoriteInterface favoriteInterface) {
        this.favoriteInterface = favoriteInterface;
    }

    public void getInfo(MainActivity mainActivity) {
        SharedPreferences preferences = mainActivity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn){
            int user_id = preferences.getInt("userid",0);
            getPostUser(user_id);
        } else {
            favoriteInterface.requestLogin();
        }
    }

    private void getPostUser(int user_id) {
        String userId = String.valueOf(user_id);
        List<String> listFavorite = new ArrayList<>();

        ApiService.apiService.getFavoritePost().enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                List<Favorite> list = new ArrayList<>();
                list = response.body();
                for (int $i = 0; $i < list.size(); $i++){
                    if (list.get($i).getUser_id().equals(userId)){
                        listFavorite.add(list.get($i).getPost_id());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {

            }
        });

        ApiService.apiService.getPost().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> list = new ArrayList<>();
                List<Post> listPost = new ArrayList<>();
                list = response.body();
                for (int $i = 0; $i < list.size(); $i++){
                    for (int $j = 0; $j < listFavorite.size(); $j++){
                        if (list.get($i).getId().equals(listFavorite.get($j))){
                            listPost.add(list.get($i));
                        }
                    }
                }
                favoriteInterface.setPost(listPost);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }
}
