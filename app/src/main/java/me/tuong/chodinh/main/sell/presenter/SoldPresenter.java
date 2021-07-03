package me.tuong.chodinh.main.sell.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.sell.minterface.SoldInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;

public class SoldPresenter {
    private SoldInterface soldInterface;

    public SoldPresenter(SoldInterface soldInterface) {
        this.soldInterface = soldInterface;
    }

    public void getInfo(FragmentActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn){
            int user_id = preferences.getInt("userid",0);
            getPostUser(user_id);
        } else {
            soldInterface.requestLogin();
        }
    }

    private void getPostUser(int user_id) {
        String userId = String.valueOf(user_id);
        ApiService.apiService.getPostSold().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> list = new ArrayList<>();
                List<Post> listPost = new ArrayList<>();
                list = response.body();
                for (int $i = 0; $i < list.size(); $i++){
                    if (list.get($i).getUser_id().equals(userId)){
                        listPost.add(list.get($i));
                    }
                }
                soldInterface.setPost(listPost);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }
}
