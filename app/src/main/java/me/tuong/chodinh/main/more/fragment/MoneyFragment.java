package me.tuong.chodinh.main.more.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentMoneyBinding;
import me.tuong.chodinh.login.LoginActivity;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.model.UserInfo;
import me.tuong.chodinh.main.more.adapter.NapTienAdapter;
import me.tuong.chodinh.main.more.minterface.MMoneyInterface;
import me.tuong.chodinh.main.more.presenter.MoneyPresenter;

public class MoneyFragment extends Fragment implements MMoneyInterface {
    public static final String TAG = MoneyFragment.class.getName();
    FragmentMoneyBinding binding;
    private MoneyPresenter moneyPresenter;
    private UserInfo userInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_money, container, false);
        moneyPresenter = new MoneyPresenter(this);
        //ẩn thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(false, "Tài khoản Đồng Tốt", getResources().getDrawable(R.drawable.ic_baseline_keyboard_backspace_24));
        setHasOptionsMenu(true);

        //lấy thông tin
        moneyPresenter.getInfo(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rcvNaptien.setLayoutManager(linearLayoutManager);

        NapTienAdapter napTienAdapter = new NapTienAdapter(getListGia(), new NapTienAdapter.IClickItemListener() {
            @Override
            public void onCLickItemUser(String tien) {
                getDiaLog(tien, userInfo);
            }
        });
        binding.rcvNaptien.setAdapter(napTienAdapter);

        //chuyển sang trang đăng nhập
        binding.tvLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_action_search);
        itemSearch.setVisible(false);
    }

    private void getDiaLog(String tien, UserInfo userInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận nạp Đồng Tốt");
        builder.setMessage("Bạn có đồngg ý nạp "+tien+" đồng tốt vào tài khoản?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String totaldong = binding.tvMoney.getText().toString().trim();
                moneyPresenter.napTien(tien,totaldong,userInfo);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private List<String> getListGia() {
        List<String> list = new ArrayList<>();
        list.add("1.500.000");
        list.add("1.000.000");
        list.add("500.000");
        list.add("100.000");
        list.add("50.000");
        list.add("20.000");

        return list;
    }

    @Override
    public void setUserInfo(UserInfo user) {
        userInfo = user;
        if (user.getDong().equals("")){
            binding.tvMoney.setText("0");
        }else {
            binding.tvMoney.setText(user.getDong());
        }
    }

    @Override
    public void setLogin(String name) {
        binding.svMoney.setVisibility(View.GONE);
        binding.llLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void setMoney(String strtongtien) {
        binding.tvMoney.setText(strtongtien);
    }

    @Override
    public void setNotifi(String mess) {
        Toast.makeText(getContext(),mess, Toast.LENGTH_LONG).show();
    }
}