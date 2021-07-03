package me.tuong.chodinh.login.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.TimeUnit;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.login.minterface.AuthenticLoginInterface;
import me.tuong.chodinh.login.model.ServerResponse;
import me.tuong.chodinh.login.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;

public class AuthenticLoginPresenter {
    private AuthenticLoginInterface authenticLoginInterface;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private String url = "http://domain.com/updateuser.php";

    public AuthenticLoginPresenter(AuthenticLoginInterface authenticLoginInterface) {
        this.authenticLoginInterface = authenticLoginInterface;
    }

    public void checkSDT(String strSDT, FragmentActivity activity) {
        mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + strSDT) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                authenticLoginInterface.setMa(code);
                //verifying the code
                //verifyVerificationCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            authenticLoginInterface.onError(e.getMessage());
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
    };

    public void checkCode(String strCode) {
        try {
            //creating the credential
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, strCode);
            //signing the user
            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){
            String mess = "Vui lòng nhập mã xác nhận chính xác";
            authenticLoginInterface.onError(mess);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            authenticLoginInterface.getSdt();

                        } else {
                            String mess = "Vui lòng nhập mã xác nhận chính xác";
                            authenticLoginInterface.onError(mess);
                        }
                    }
                });
    }

    public void saveNewToken(User mUser, FragmentActivity activity) {

        //lưu vào bộ nhớ tạm
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", mUser.getName());
        editor.putInt("userid", mUser.getId());
        editor.putBoolean("loggedIn", true);
        editor.apply();

        ApiService.apiService.updateToken(mUser.getToken(),mUser.getEmail()).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse message = response.body();
                if (message.getMessage().equals("Success")) {
                    Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                } else {
                    String mess = "Lỗi, vui lòng thử lại";
                    authenticLoginInterface.onError(mess);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }
}
