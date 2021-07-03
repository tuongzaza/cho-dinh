package me.tuong.chodinh.login.presenter;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.login.minterface.RegisterInterface;
import me.tuong.chodinh.login.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenter {
    private RegisterInterface registerInterface;
    private User mUser;
    String ServerURL = "http://dommain.com/get_data.php";
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private String token;
    private List<User> listU;

    public RegisterPresenter(RegisterInterface registerInterface) {
        this.registerInterface = registerInterface;
    }


    public void register(String strEmailR, String strNameR, String strPassR, String code, List<User> userList, FragmentActivity activity) {

        //check email đã tồn tại
        boolean isHasUser = true;

        for (User user : userList) {
            if (strEmailR.equals(user.getEmail())) {
                isHasUser = false;
                break;
            }
        }
        if (isHasUser){
            String strMD5R = getMD5(strPassR);
            verifyVerificationCode(strEmailR,strNameR, strMD5R,code,activity);

        } else {
            String mess = "Số điện thoại đã tồn tại";
            registerInterface.loginError(mess);
        }

    }

    private void InsertData(String strEmailR, String strNameR, String strMD5R, FragmentActivity activity) {

        //getToken
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    token = task.getResult();
                } else {
                    String mess = "Token Not Generated";
                    registerInterface.loginError(mess);
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                registerInterface.nextFragment();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                String mess = "Xảy ra lỗi!";
//                postInterface.getError(mess);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", strEmailR);
                params.put("name", strNameR);
                params.put("password", strMD5R);
                params.put("token", token);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String getMD5(String strPassR) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte [] messageDigest = md.digest(strPassR.getBytes());
            BigInteger no =  new BigInteger( 1 , messageDigest);
            String strPassMD5 = no.toString( 16 );
            while (strPassMD5.length() <  32 ) {
                strPassMD5 =  "0" + strPassMD5;
            }
            return strPassMD5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return strPassR;
    }

    public void onConfirm(String mobile, FragmentActivity activity) {
        mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + mobile) // Phone number to verify
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
                registerInterface.setMa(code);
                //verifying the code
                //verifyVerificationCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            registerInterface.loginError(e.getMessage());
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String strEmailR, String strNameR, String strMD5R, String otp, FragmentActivity activity) {
        try {
            //creating the credential
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
            //signing the user
            signInWithPhoneAuthCredential(credential,strEmailR,strNameR, strMD5R, activity);
        }catch (Exception e){
            String mess = "Vui lòng nhập mã xác nhận chính xác";
            registerInterface.loginError(mess);
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, String strEmailR, String strNameR, String strMD5R, FragmentActivity activity) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            InsertData(strEmailR,strNameR, strMD5R, activity);

                        } else {
                            registerInterface.loginError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void getListUser() {
        ApiService.apiService.getListUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                listU = response.body();
                registerInterface.setListUser(listU);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                String mess = "Call API Error";
                registerInterface.loginError(mess);
            }
        });
    }
}
