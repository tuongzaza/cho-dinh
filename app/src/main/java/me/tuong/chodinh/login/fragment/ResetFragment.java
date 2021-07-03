package me.tuong.chodinh.login.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentResetBinding;
import me.tuong.chodinh.login.minterface.ResetInterface;
import me.tuong.chodinh.login.model.User;
import me.tuong.chodinh.login.presenter.ResetPresenter;

public class ResetFragment extends Fragment implements ResetInterface {
    public static final String TAG = ResetFragment.class.getName();
    FragmentResetBinding binding;
    private ResetPresenter resetPresenter;
    private String strSdt, strCode;
    private List<User> userList;
    private ProgressDialog progressDialog;




    public ResetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_reset, container, false);
        resetPresenter = new ResetPresenter(this);
        userList = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Vui lòng đợi...");

        //lấy dữ liệu
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null){
            String strEmail = (String) bundleReceive.get("strEmail");
            userList = (List<User>) bundleReceive.get("object_user");
            if (strEmail != null){
                binding.etSDTRe.setText(strEmail);
            }
        }

        binding.btnLayMaRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressDialog.show();
                strSdt = binding.etSDTRe.getText().toString().trim();
                if (strSdt.isEmpty()){
                    binding.etSDTRe.setError("Nhập đúng định dạng SDT!");
                    binding.etSDTRe.requestFocus();
                    return;
                }
                resetPresenter.checkSDT(strSdt,userList, getActivity());
                binding.llTime.setVisibility(View.VISIBLE);
                binding.tvLoading.setVisibility(View.VISIBLE);
                binding.btnLayMaRe.setVisibility(View.GONE);
                getTime();
            }
        });

        binding.btnReSetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCode = binding.etXacMinhRe.getText().toString().trim();
                resetPresenter.checkCode(strCode);
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

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
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
                binding.btnLayMaRe.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    @Override
    public void onError(String mess) {
        Toast.makeText(getContext(),mess, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMa(String code) {
        binding.etXacMinhRe.setText(code);
    }

    @Override
    public void getPassword() {
        String pass1 = binding.etPass1Re.getText().toString().trim();
        String pass2 = binding.etPass1Re.getText().toString().trim();
        if (pass1.isEmpty()){
            binding.etPass1Re.setError("Nhập mật khẩu mới!");
            binding.etPass1Re.requestFocus();
            return;
        }
        if (pass1.equals(pass2)){
            resetPresenter.updatePassword(pass1, strSdt,getActivity());
        }else {
            Toast.makeText(getContext(),"Cần nhập 2 mật khẩu trùng nhau", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getSuccess(String mess) {
        CountDownTimer timerr = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvNotifi.setVisibility(View.VISIBLE);
                binding.tvNotifi.setText("Đổi mật khẩu thành công! Trở lại trang Login sau " + millisUntilFinished/1000 +"s");
            }

            @Override
            public void onFinish() {
                binding.tvNotifi.setText("Đổi mật khẩu thành công! Trở lại trang Login sau 0s");
                getFragmentManager().popBackStack();
            }

        }.start();
    }
}