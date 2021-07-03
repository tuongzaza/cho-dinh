package me.tuong.chodinh.main.notification.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentNotifiBinding;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.notification.adapter.NewsViewPagerAdapter;
import me.tuong.chodinh.main.notification.minterface.NotifiInterface;
import me.tuong.chodinh.main.notification.presenter.NotifiPresenter;


public class NotifiFragment extends Fragment implements NotifiInterface {
    FragmentNotifiBinding binding;
    private NotifiPresenter notifiPresenter;


    public NotifiFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notifi, container, false);
        notifiPresenter = new NotifiPresenter(this);

        setHasOptionsMenu(true);

        //hiện thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(true, "Thông báo", null);

        NewsViewPagerAdapter newsViewPagerAdapter = new NewsViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPagerLayoutNotification.setAdapter(newsViewPagerAdapter);
        binding.tabLayoutNotification.setupWithViewPager(binding.viewPagerLayoutNotification);

        return binding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_action_search);
        itemSearch.setVisible(false);
    }
}