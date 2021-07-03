package me.tuong.chodinh.main.notification.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import me.tuong.chodinh.main.notification.fragment.ActiveFragment;
import me.tuong.chodinh.main.notification.fragment.NewsFragment;
import me.tuong.chodinh.main.sell.fragment.OnSaleFragment;
import me.tuong.chodinh.main.sell.fragment.SoldFragment;

public class NewsViewPagerAdapter extends FragmentStatePagerAdapter {

    public NewsViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ActiveFragment();
            case 1:
                return new NewsFragment();
            default:
                return new ActiveFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position){
            case 0:
                title = "Hoạt động";
                break;
            case 1:
                title = "Tin mới";
                break;
        }
        return title;
    }
}
