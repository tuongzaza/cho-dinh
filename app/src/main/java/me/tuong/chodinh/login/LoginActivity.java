package me.tuong.chodinh.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


import java.io.Serializable;
import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.ActivityLoginBinding;
import me.tuong.chodinh.login.fragment.AuthenticLoginFragment;
import me.tuong.chodinh.login.fragment.LoginFragment;
import me.tuong.chodinh.login.fragment.RegisterFragment;
import me.tuong.chodinh.login.fragment.ResetFragment;
import me.tuong.chodinh.login.model.Info;
import me.tuong.chodinh.login.model.User;

public class LoginActivity extends AppCompatActivity implements LoginFragment.ISendData {
    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    ActivityLoginBinding binding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.contentFrame, new LoginFragment());
        fragmentTransaction.commit();

        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void sendData(String strEmail) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.receiveData(strEmail);
        fragmentTransaction.replace(R.id.contentFrame, registerFragment);
        fragmentTransaction.addToBackStack(RegisterFragment.TAG);
        fragmentTransaction.commit();
    }

    public void nexActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void sendDataReset(String strEmail, List<User> userList) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ResetFragment resetFragment = new ResetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("strEmail", strEmail);
        bundle.putSerializable("object_user", (Serializable) userList);
        resetFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.contentFrame, resetFragment);
        fragmentTransaction.addToBackStack(ResetFragment.TAG);
        fragmentTransaction.commit();
    }

    private void loadFragment(Fragment fragment, String strEmail) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_post", strEmail);
        fragment.setArguments(bundle);
        transaction.replace(R.id.contentFrame, fragment);
        //transaction.addToBackStack(Fragment.TAG);
        transaction.commit();
    }

    public void sendDataAuthentic(User mUser) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AuthenticLoginFragment authenticLoginFragment = new AuthenticLoginFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user", mUser);
        authenticLoginFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.contentFrame, authenticLoginFragment);
        fragmentTransaction.addToBackStack(AuthenticLoginFragment.TAG);
        fragmentTransaction.commit();
    }
}