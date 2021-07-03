package me.tuong.chodinh.login.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.login.minterface.LoginInterface;
import me.tuong.chodinh.login.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;

public class LoginPresenter {
    private LoginInterface loginInterface;
    private List<User> list;
    private User mUser;

    public LoginPresenter(LoginInterface loginInterface) {
        this.loginInterface = loginInterface;
    }


    public void login(String strEmail, String strPass, List<User> userList, String tokenTamp, FragmentActivity activity) {


        String strMD55 = getMD5(strPass);
        if (userList == null || userList.isEmpty()){
            return;
        }
        boolean isHasUser = false;

        for (User user : userList) {
            if (strEmail.equals(user.getEmail()) && strMD55.equals(user.getPassword())) {
                int userid = user.getId();
                String username = user.getName();
                String token = user.getToken();

                if (token.equals(tokenTamp)){
                    //lưu vào bộ nhớ tạm
                    SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putInt("userid",userid);
                    editor.putBoolean("loggedIn", true);
                    editor.apply();

                    //chuyển sang MainActivity
                    Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                }else {
                    mUser = new User(userid,username,strEmail,null, tokenTamp);
                    loginInterface.gotoAuthenticLoginFragment(mUser);
                }

                isHasUser = true;
                mUser = user;
                break;
            }
        }
        if (isHasUser){
            //
        } else {
            String mess = "Sai Email hoac Password";
            loginInterface.loginError(mess);
        }
    }


    private String getMD5(String strPass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte [] messageDigest = md.digest(strPass.getBytes());
            BigInteger no =  new BigInteger( 1 , messageDigest);
            String strPassMD5 = no.toString( 16 );
            while (strPassMD5.length() <  32 ) {
                strPassMD5 =  "0" + strPassMD5;
            }
            return strPassMD5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return strPass;
    }

    public void getListUser() {
        ApiService.apiService.getListUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                list = response.body();
                loginInterface.loginSuccess(list);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                String mess = "Call API Error";
                loginInterface.loginError(mess);
            }
        });
    }
}
