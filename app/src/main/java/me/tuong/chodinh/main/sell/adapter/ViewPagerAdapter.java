package me.tuong.chodinh.main.sell.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import me.tuong.chodinh.main.sell.fragment.OnSaleFragment;
import me.tuong.chodinh.main.sell.fragment.SoldFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OnSaleFragment();
            case 1:
                return new SoldFragment();
            default:
                return new OnSaleFragment();
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
                title = "Đang bán";
                break;
            case 1:
                title = "Đã bán";
                break;
        }
        return title;
    }
}
