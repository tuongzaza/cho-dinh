package me.tuong.chodinh.main.sell.fragment;

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
import me.tuong.chodinh.databinding.FragmentSellBinding;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.sell.adapter.ViewPagerAdapter;

public class SellFragment extends Fragment {
    public static final String TAG = SellFragment.class.getName();
    FragmentSellBinding binding;



    public SellFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sell, container, false);
        setHasOptionsMenu(true);

        //hiện thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(true, "Tôi bán", null);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPagerLayout.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPagerLayout);
        

        return binding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_action_search);
        itemSearch.setVisible(false);
    }
}