package me.tuong.chodinh.main.more.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.api.Const;
import me.tuong.chodinh.login.model.ServerResponse;
import me.tuong.chodinh.login.model.User;
import me.tuong.chodinh.main.MyResponse;
import me.tuong.chodinh.main.home.model.UserInfo;
import me.tuong.chodinh.main.more.minterface.EditUserInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;
import static me.tuong.chodinh.main.more.fragment.EditUserFragment.MY_REQUEST_CODE;

public class EditUserPresenter {
    private EditUserInterface editUserInterface;

    public EditUserPresenter(EditUserInterface editUserInterface) {
        this.editUserInterface = editUserInterface;
    }

    public void getUserInfo(FragmentActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn){
            String name = preferences.getString("username","");
            int user_id = preferences.getInt("userid",0);
            String userId= String.valueOf(user_id);
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
                editUserInterface.setUserInfo(user);
            }
        }
    }

    public void checkPass(String password, String passwordold, UserInfo userInfo) {
        String passwordMD5 = getMD5(password);
        if (userInfo.getPassword()!=""){
            String newpassMMD5 = getMD5(userInfo.getPassword());
            userInfo.setPassword(newpassMMD5);
        }
        if (passwordMD5.equals(passwordold)){
            callApiUpdateUser(userInfo, passwordold);
        }else {
            editUserInterface.wrongPassword();
        }
    }

    private void callApiUpdateUser(UserInfo userInfo, String passwordold) {
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"),userInfo.getName());
        RequestBody requestBodySdt = RequestBody.create(MediaType.parse("text/plain"),userInfo.getEmail());
        RequestBody requestBodyPass;
        if (userInfo.getPassword().equals("d41d8cd98f00b204e9800998ecf8427e")){
            requestBodyPass = RequestBody.create(MediaType.parse("multipart/form-data"),passwordold);
        }else {
            requestBodyPass = RequestBody.create(MediaType.parse("multipart/form-data"),userInfo.getPassword());
        }
        RequestBody requestBodyAddress = RequestBody.create(MediaType.parse("text/plain"),userInfo.getAddress());
        RequestBody requestBodyEmail = RequestBody.create(MediaType.parse("multipart/form-data"),userInfo.getEmailreal());

        MultipartBody.Part avatar = null;
        if (userInfo.getAvatar()!=null){
            File file = new File(userInfo.getAvatar());
            RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            avatar = MultipartBody.Part.createFormData(Const.KEY_AVATAR, file.getName(), requestBodyAvatar);
        }

        ApiService.apiService.updateUser(avatar,requestBodySdt,requestBodyAddress,requestBodyName,requestBodyEmail,requestBodyPass).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (!response.body().error) {
                    String mess = "Thành công";
                    editUserInterface.messenger(mess);
                } else {
                    String mess = "Lỗi, vui lòng thử lại";
                    editUserInterface.messenger(mess);
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                String mess = "Lỗi call Api";
                editUserInterface.messenger(mess);
            }
        });
    }

    private String getMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte [] messageDigest = md.digest(password.getBytes());
            BigInteger no =  new BigInteger( 1 , messageDigest);
            String strPassMD5 = no.toString( 16 );
            while (strPassMD5.length() <  32 ) {
                strPassMD5 =  "0" + strPassMD5;
            }
            return strPassMD5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return password;
    }
}
