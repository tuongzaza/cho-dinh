package me.tuong.chodinh.main.more.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.login.model.ServerResponse;
import me.tuong.chodinh.main.home.model.UserInfo;
import me.tuong.chodinh.main.more.minterface.MMoneyInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;

public class MoneyPresenter {
    private MMoneyInterface mMoneyInterface;

    public MoneyPresenter(MMoneyInterface mMoneyInterface) {
        this.mMoneyInterface = mMoneyInterface;
    }

    public void getInfo(FragmentActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn){
            int user_id = preferences.getInt("userid",0);
            String userId= String.valueOf(user_id);
            getListUser(userId);
        }else {
            String name = "Đăng nhập/Đăng ký";
            mMoneyInterface.setLogin(name);
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
                mMoneyInterface.setUserInfo(user);
            }
        }
    }

    public void napTien(String tien, String totaldong, UserInfo userInfo) {
        int total = Integer.parseInt(totaldong.replace(".",""));
        int tiencong = Integer.parseInt(tien.replace(".",""));
        int tongtien = total + tiencong;

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str = currencyVN.format(tongtien);
        String strtongtien = str.replace(" ₫","");
        updateDong(strtongtien,userInfo);
        mMoneyInterface.setMoney(strtongtien);
    }

    private void updateDong(String strtongtien, UserInfo userInfo) {
        ApiService.apiService.updateDong(strtongtien,userInfo.getEmail()).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse message = response.body();
                if (message.getMessage().equals("Success")) {
                    String mess = "Thành công!";
                    mMoneyInterface.setNotifi(mess);
                } else {
                    String mess = "Lỗi, vui lòng thử lại";
                    mMoneyInterface.setNotifi(mess);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }
}
