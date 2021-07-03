package me.tuong.chodinh.main.more.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.login.LoginActivity;
import me.tuong.chodinh.main.home.model.UserInfo;
import me.tuong.chodinh.main.more.minterface.MoreInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;

public class MorePresenter {
    private MoreInterface moreInterface;

    public MorePresenter(MoreInterface moreInterface) {
        this.moreInterface = moreInterface;
    }

    public void getInfo(FragmentActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn){
//            String name = preferences.getString("username","");
//            moreInterface.setName(name);
            int userid = preferences.getInt("userid",0);
            String userId= String.valueOf(userid);
            getListUser(userId);
        }else {
            String name = "Đăng nhập/Đăng ký";
            moreInterface.setLogin(name);
        }
    }

    private void getListUser(String userId) {
        ApiService.apiService.getListUserInfo().enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                List<UserInfo> list = new ArrayList<>();
                list = response.body();

                checkUser(userId,list);

            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {

            }
        });
    }

    private void checkUser(String userId, List<UserInfo> list) {
        for (UserInfo user : list) {
            if (userId.equals(user.getId())) {
                moreInterface.setName(user);
            }
        }
    }

    public void getLogout(FragmentActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn) {
            moreInterface.setLogout();
        }
    }

    public void logout(FragmentActivity activity) {
        try{
            SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("loggedIn", false);
            editor.apply();

            Intent i = new Intent(activity.getApplicationContext(), LoginActivity.class);
            activity.startActivity(i);
            activity.finish();
        } catch (Exception e){

        }
    }
}
