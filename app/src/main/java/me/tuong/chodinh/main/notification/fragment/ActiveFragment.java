package me.tuong.chodinh.main.notification.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentActiveBinding;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.notification.adapter.NotifiAdapter;
import me.tuong.chodinh.main.notification.minterface.ActiveInterface;
import me.tuong.chodinh.main.notification.presenter.ActivePresenter;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;


public class ActiveFragment extends Fragment implements ActiveInterface {
    FragmentActiveBinding binding;
    private ActivePresenter activePresenter;
    private MainActivity activity;
    private NotifiAdapter notifiAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_active, container, false);
        activePresenter = new ActivePresenter(this);

        activity = (MainActivity) getActivity();

        activePresenter.getInfo(activity);

        return binding.getRoot();
    }

    @Override
    public void requestLogin() {
        binding.rcvNotifi.setVisibility(View.GONE);
        binding.llLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPost(List<Post> listPost) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        gridLayoutManager.setOrientation(VERTICAL);
        binding.rcvNotifi.setNestedScrollingEnabled(false);
        binding.rcvNotifi.setLayoutManager(gridLayoutManager);
        notifiAdapter = new NotifiAdapter(activity);
        notifiAdapter.setData(listPost);
        binding.rcvNotifi.setAdapter(notifiAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        binding.rcvNotifi.addItemDecoration(itemDecoration);
    }
}