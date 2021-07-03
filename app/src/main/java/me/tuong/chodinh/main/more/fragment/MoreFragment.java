package me.tuong.chodinh.main.more.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentMoreBinding;
import me.tuong.chodinh.login.LoginActivity;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.model.UserInfo;
import me.tuong.chodinh.main.more.minterface.MoreInterface;
import me.tuong.chodinh.main.more.presenter.MorePresenter;

public class MoreFragment extends Fragment implements MoreInterface {
    FragmentMoreBinding binding;
    MorePresenter morePresenter;
    private MainActivity mainActivity;


    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_more, container, false);
        morePresenter = new MorePresenter(this);
        mainActivity = (MainActivity) getActivity();

        setHasOptionsMenu(true);

        //hiện thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(true, "Thêm", null);

        morePresenter.getInfo(getActivity());
        morePresenter.getLogout(getActivity());

        binding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morePresenter.logout(getActivity());
            }
        });

        binding.tvPostFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToFavorite();
            }
        });

        binding.tvPageEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.goToEditUser();
            }
        });

        binding.tvDongTot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.goToNapDong();
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

    @Override
    public void setName(UserInfo user) {
        binding.tvNameShow.setText(user.getName());
        if (user.getAvatar().equals("")==false){
            Glide.with(getContext()).load(user.getAvatar()).into(binding.imvAvatar);
        }
    }

    @Override
    public void setLogin(String name) {
        binding.tvNameShow.setText(name);
        binding.tvPageEditUser.setVisibility(View.GONE);
        binding.tvNameShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
    }

    @Override
    public void setLogout() {
        binding.llLogout.setVisibility(View.VISIBLE);
    }
}