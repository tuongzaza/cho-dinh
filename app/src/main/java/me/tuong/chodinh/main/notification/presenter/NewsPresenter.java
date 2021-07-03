package me.tuong.chodinh.main.notification.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.main.notification.minterface.NewsInterface;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;

public class NewsPresenter {
    private NewsInterface newsInterface;

    public NewsPresenter(NewsInterface newsInterface) {
        this.newsInterface = newsInterface;
    }


    public void getInfo(MainActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn){
            newsInterface.loadList();
        } else {
            newsInterface.requestLogin();
        }
    }
}
