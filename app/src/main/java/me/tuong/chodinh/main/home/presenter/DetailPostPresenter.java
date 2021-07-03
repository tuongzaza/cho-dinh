package me.tuong.chodinh.main.home.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.login.model.ServerResponse;
import me.tuong.chodinh.main.home.minterface.DetailPostInterface;
import me.tuong.chodinh.main.home.model.Favorite;
import me.tuong.chodinh.main.home.model.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;

public class DetailPostPresenter {
    private DetailPostInterface detailPostInterface;
    private UserInfo userInfo;

    public DetailPostPresenter(DetailPostInterface detailPostInterface) {
        this.detailPostInterface = detailPostInterface;
    }

    public void sendSMS(String callnumber, String titleSMS) {

    }

    public void getTime(String time) {
        String times = null;
        try {
            long currentTime = System.currentTimeMillis();
            long posttime = Long.parseLong(time);
            long timee = (currentTime-posttime)/1000;
            if (timee >= 31104000){
                times = (timee/31104000)+" năm trước";
            }else {
                if (timee >= 2592000){
                    times = (timee/2592000)+" tháng trước";
                }else {
                    if (timee >= 86400){
                        times = (timee/86400)+" ngày trước";
                    } else {
                        if (timee >= 3600){
                            times = (timee/3600)+" giờ trước";
                        }else {
                            if (timee >= 60){
                                times = (timee/60)+ " phút trước";
                            }else {
                                times = "Vừa mới đăng";
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        detailPostInterface.setTime(times);
    }

    public void getEdit(MainActivity mainActivity) {
        SharedPreferences preferences = mainActivity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn){
            int userid = preferences.getInt("userid",0);
            String userId= String.valueOf(userid);
            getListUser(userId);
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
                detailPostInterface.setEditPost(userId,user);
            }
        }
    }

    public void setStatus(String post_id, MainActivity mainActivity) {
        String url = "http://tuong.xyz/setstatuspost.php";
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Data Submit Successfully")){
                    String mess = "Cập nhật tin thành công!";
                    detailPostInterface.getSuccess(mess);
                }else {
                    String mess = "Lỗi!";
                    detailPostInterface.getError(mess);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mess = "Xảy ra lỗi!";
                detailPostInterface.getError(mess);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("post_id", post_id);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void savePostFavorite(String post_id, String user_id, MainActivity mainActivity) {
        String url = "http://tuong.xyz/insertfavorite.php";
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Data Submit Successfully")){
                    String mess = "Lưu tin thành công!";
                    detailPostInterface.getToast(mess);
                }else {
                    String mess = "Lỗi!";
                    detailPostInterface.getError(mess);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mess = "Xảy ra lỗi!";
                detailPostInterface.getError(mess);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("post_id", post_id);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getCheckFavorite(String user_id, String post_id) {
        ApiService.apiService.getFavoritePost().enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                List<Favorite> list = new ArrayList<>();
                list = response.body();
                for (int $i = 0; $i < list.size(); $i++){
                    if (list.get($i).getUser_id().equals(user_id) && list.get($i).getPost_id().equals(post_id)){
                        detailPostInterface.setFavoriteOK();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {

            }
        });
    }

    public void dayTin(String mua, UserInfo user, String post_id) {
        long currentTime = System.currentTimeMillis();
        String[] types = {"3 ngày (30.000đ)", "7 ngày (70.000đ)", "1 tháng (300.000đ)"};
        int dongmua = 0;
        if (mua.equals(types[0])){
            dongmua = 30000;
        }
        if (mua.equals(types[1])){
            dongmua = 60000;
        }
        if (mua.equals(types[2])){
            dongmua = 200000;
        }

        int tongtien = Integer.parseInt(user.getDong().replace(".",""));

        if (dongmua>tongtien){
            String mess = "Tài khoản của bạn không đủ, hãy nạp thêm đồng để sử dụng dịch vụ!";
            detailPostInterface.getError(mess);
        }else {
            int totalhave = tongtien - dongmua;
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
            String str = currencyVN.format(totalhave);
            String strtongtien = str.replace(" ₫","");

            long timeExp = currentTime + (dongmua*24*60*6);

            updateDong(strtongtien, user);
            updateTime(timeExp, post_id);
        }
    }

    private void updateTime(long timeExp, String post_id) {
        ApiService.apiService.updateTimme(timeExp,post_id).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse message = response.body();
                if (message.getMessage().equals("Success")) {
                    String mess = "Thành công!";
                    detailPostInterface.showTimeExp(mess, timeExp);
                } else {
                    String mess = "Lỗi, vui lòng thử lại";
                    detailPostInterface.getError(mess);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }

    private void updateDong(String totalhave, UserInfo user) {
        ApiService.apiService.updateDong(totalhave,user.getEmail()).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse message = response.body();
                if (message.getMessage().equals("Success")) {
                    String mess = "Thành công!";
                    //mMoneyInterface.setNotifi(mess);
                } else {
                    String mess = "Lỗi, vui lòng thử lại";
                    //mMoneyInterface.setNotifi(mess);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }

    public void getUserInfo(String user_id_post) {
        ApiService.apiService.getListUserInfo().enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                List<UserInfo> list = new ArrayList<>();
                list = response.body();
                for (int $i = 0; $i < list.size(); $i++){
                    if (list.get($i).getId().equals(user_id_post)){

                        userInfo = list.get($i);

                        detailPostInterface.setUserPostInfo(userInfo);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                detailPostInterface.getError("Lỗi");
            }
        });
    }
}
