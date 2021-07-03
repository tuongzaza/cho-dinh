package me.tuong.chodinh.login.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.databinding.FragmentRegisterBinding;
import me.tuong.chodinh.login.minterface.RegisterInterface;
import me.tuong.chodinh.login.model.User;
import me.tuong.chodinh.login.presenter.RegisterPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment implements RegisterInterface {

    public static final String TAG = RegisterFragment.class.getName();

    FragmentRegisterBinding binding;
    private RegisterPresenter registerPresenter;
    private List<User> userList;
    String strEmail1;
    private Context context;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register, container, false);
        registerPresenter = new RegisterPresenter(this);

        receiveData(strEmail1);
        binding.etEmail.setText(strEmail1);

        registerPresenter.getListUser();

        binding.btnLayMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConfirm();
                binding.llTime.setVisibility(View.VISIBLE);
                binding.tvLoading.setVisibility(View.VISIBLE);
                binding.btnLayMa.setVisibility(View.GONE);
                getTime();
            }
        });

        binding.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister();
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null){
                    getFragmentManager().popBackStack();
                }
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

    private void getConfirm() {
        String mobile = binding.etEmail.getText().toString().trim();
        if(mobile.isEmpty() || mobile.length() < 10){
            binding.etEmail.setError("Nhập đúng định dạng SDT");
            binding.etEmail.requestFocus();
            return;
        }
        registerPresenter.onConfirm(mobile, getActivity());
    }


    private void onRegister() {
        String code = binding.etXacMinh.getText().toString().trim();
        String strEmailR = binding.etEmail.getText().toString().trim();
        String strNameR = binding.etName.getText().toString().trim();
        String strPassR = binding.etPass.getText().toString().trim();

        if (code.isEmpty()){
            binding.etXacMinh.setError("Nhập mã xác minh!");
            binding.etXacMinh.requestFocus();
            return;
        }
        if (strEmailR.isEmpty()){
            binding.etEmail.setError("Nhập đúng định dạng SDT!");
            binding.etEmail.requestFocus();
            return;
        }
        if (strNameR.isEmpty()){
            binding.etName.setError("Nhập họ tên của bạn!");
            binding.etName.requestFocus();
            return;
        }
        if (strPassR.isEmpty()){
            binding.etPass.setError("Nhập mật khẩu!");
            binding.etPass.requestFocus();
            return;
        }

        registerPresenter.register(strEmailR,strNameR,strPassR,code,userList,getActivity());
    }

    public void receiveData (String strEmail) {
        strEmail1 = strEmail;
    }


    @Override
    public void loginError(String mess) {
        Toast.makeText(getContext(),mess, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMa(String code) {
        binding.etXacMinh.setText(code);
    }

    @Override
    public void nextFragment() {
        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvNotifi.setVisibility(View.VISIBLE);
                binding.tvNotifi.setText("Đăng ký tài khoản thành công! Trở lại trang Login sau " + millisUntilFinished/1000 +"s");
            }

            @Override
            public void onFinish() {
                binding.tvNotifi.setText("Đăng ký tài khoản thành công! Trở lại trang Login sau 0s");
                getFragmentManager().popBackStack();
            }

        }.start();
    }

    @Override
    public void setListUser(List<User> listU) {
        userList = listU;
    }
}