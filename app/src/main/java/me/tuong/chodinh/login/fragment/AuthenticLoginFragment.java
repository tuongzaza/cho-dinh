package me.tuong.chodinh.login.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentAuthenticLoginBinding;
import me.tuong.chodinh.login.minterface.AuthenticLoginInterface;
import me.tuong.chodinh.login.model.User;
import me.tuong.chodinh.login.presenter.AuthenticLoginPresenter;

public class AuthenticLoginFragment extends Fragment implements AuthenticLoginInterface {
    public static final String TAG = AuthenticLoginFragment.class.getName();
    FragmentAuthenticLoginBinding binding;
    private AuthenticLoginPresenter authenticLoginPresenter;

    private String strSDT;
    private User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_authentic_login, container, false);
        authenticLoginPresenter = new AuthenticLoginPresenter(this);

        //lấy dữ liệu
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null){
            mUser = (User) bundleReceive.get("object_user");
            if (mUser != null){
                binding.etSdt.setText(mUser.getEmail());
            }
        }

        binding.btnLayMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressDialog.show();
                strSDT = binding.etSdt.getText().toString().trim();
                authenticLoginPresenter.checkSDT(strSDT, getActivity());
                binding.llTime.setVisibility(View.VISIBLE);
                binding.tvLoading.setVisibility(View.VISIBLE);
                binding.btnLayMa.setVisibility(View.GONE);
                getTime();
            }
        });

        binding.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCode = binding.etXacMinh.getText().toString().trim();
                authenticLoginPresenter.checkCode(strCode);
            }
        });

        return binding.getRoot();
    }

    private void getTime() {
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvTimeCode.setText(" "+millisUntilFinished/1000 +"s");
            }

            @Override
            public void onFinish() {
                binding.tvTimeCode.setText(" 0s");
                binding.llTime.setVisibility(View.GONE);
                binding.tvLoading.setVisibility(View.GONE);
                binding.btnLayMa.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    @Override
    public void setMa(String code) {
        binding.etXacMinh.setText(code);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void getSdt() {
        authenticLoginPresenter.saveNewToken(mUser, getActivity());
    }
}