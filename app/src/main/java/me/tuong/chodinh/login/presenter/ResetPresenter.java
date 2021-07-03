package me.tuong.chodinh.login.presenter;

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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.tuong.chodinh.login.minterface.ResetInterface;
import me.tuong.chodinh.login.model.User;

public class ResetPresenter {
    private ResetInterface resetInterface;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private String url = "http://domain.com/updateuser.php";


    public ResetPresenter(ResetInterface resetInterface) {
        this.resetInterface = resetInterface;
    }

    public void checkSDT(String strSdt, List<User> userList, FragmentActivity activity) {
        //check sdt đã tồn tại
        boolean isHasUser = false;

        for (User user : userList) {
            if (strSdt.equals(user.getEmail())) {
                isHasUser = true;
                break;
            }
        }
        if (isHasUser){
            getMaxacnhan(strSdt, activity);
        }else {
            String mess = "Số điện thoại này chưa đăng ký tài khoản. Hãy tiến hành đăng ký tài khoản mới trên Chợ Tốt";
            resetInterface.onError(mess);
        }
    }

    private void getMaxacnhan(String strSdt, FragmentActivity activity) {
        mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + strSdt) // Phone number to verify
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
                resetInterface.setMa(code);
                //verifying the code
                //verifyVerificationCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            resetInterface.onError(e.getMessage());
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
            resetInterface.onError(mess);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            resetInterface.getPassword();

                        } else {
                            String mess = "Vui lòng nhập mã xác nhận chính xác";
                            resetInterface.onError(mess);
                        }
                    }
                });
    }

    public void updatePassword(String pass1, String strSdt, FragmentActivity activity) {
        String strMD5R = getMD5(pass1);
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Data Submit Successfully")){
                    String mess = "Cập nhật mật khẩu thành công!";
                    resetInterface.getSuccess(mess);
                }else {
                    String mess = "Lỗi!";
                    resetInterface.onError(mess);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mess = "Xảy ra lỗi!";
                resetInterface.onError(mess);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", strSdt);
                params.put("password", strMD5R);

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
}
