package me.tuong.chodinh.login.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.databinding.FragmentLoginBinding;
import me.tuong.chodinh.login.LoginActivity;
import me.tuong.chodinh.login.minterface.LoginInterface;
import me.tuong.chodinh.login.model.User;
import me.tuong.chodinh.login.presenter.LoginPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements LoginInterface {
    private static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    FragmentLoginBinding binding;
    private LoginPresenter loginPresenter;
    private List<User> userList;
    private LoginActivity mLoginActivity;
    private ISendData iSendData;
    private String tokenTamp;

    public interface ISendData{
        void sendData(String strEmail);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iSendData = (ISendData) getActivity();
        mLoginActivity = (LoginActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false);
        loginPresenter = new LoginPresenter(this);

        loginPresenter.getListUser();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegister();
            }
        });

        binding.tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickReset();
            }
        });

        return binding.getRoot();
    }

    private void clickReset() {
        String strEmail = binding.tvEmail.getText().toString().trim();
        mLoginActivity.sendDataReset(strEmail, userList);
    }


    private void clickRegister() {
        String strEmail = binding.tvEmail.getText().toString().trim();
        iSendData.sendData(strEmail);
    }

    private void clickLogin() {
        String strEmail = binding.tvEmail.getText().toString().trim();
        String strPass = binding.tvPass.getText().toString().trim();

        if (strEmail.equals("")){
            binding.tvEmail.setError("Sđt không được để trống");
            binding.tvEmail.requestFocus();
            return;
        }
        if (strPass.equals("")){
            binding.tvPass.setError("Mật khẩu không được để trống");
            binding.tvPass.requestFocus();
            return;
        }

        //getToken
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if (!task.isSuccessful()){
                    return;
                }
                tokenTamp = task.getResult();
                loginPresenter.login(strEmail,strPass,userList,tokenTamp,getActivity());
            }
        });
    }

    @Override
    public void loginSuccess(List<User> list) {
        userList = list;
    }

    @Override
    public void loginError(String mess) {
        Toast.makeText(getContext(),mess, Toast.LENGTH_LONG).show();
    }

    @Override
    public void gotoAuthenticLoginFragment(User mUser) {
        mLoginActivity.sendDataAuthentic(mUser);
    }
}