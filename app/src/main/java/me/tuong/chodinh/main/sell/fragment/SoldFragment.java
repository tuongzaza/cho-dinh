package me.tuong.chodinh.main.sell.fragment;

import android.content.Intent;
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
import me.tuong.chodinh.databinding.FragmentSoldBinding;
import me.tuong.chodinh.login.LoginActivity;
import me.tuong.chodinh.main.home.adapter.Post1Adapter;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.sell.minterface.SoldInterface;
import me.tuong.chodinh.main.sell.presenter.SoldPresenter;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class SoldFragment extends Fragment implements SoldInterface {
    FragmentSoldBinding binding;
    private SoldPresenter soldPresenter;
    private Post1Adapter post1Adapter;
    private MainActivity mainActivity;


    public SoldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sold, container, false);
        soldPresenter = new SoldPresenter(this);
        mainActivity = (MainActivity) getActivity();

        soldPresenter.getInfo(getActivity());

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
    public void setPost(List<Post> listPost) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        gridLayoutManager.setOrientation(VERTICAL);
        binding.rcvPostSold.setNestedScrollingEnabled(false);
        binding.rcvPostSold.setLayoutManager(gridLayoutManager);
        post1Adapter = new Post1Adapter(listPost, new Post1Adapter.IClickItemListener(){
            @Override
            public void onCLickItemUser(Post post) {
                mainActivity.goToDetailPost(post);
            }
        });
        binding.rcvPostSold.setAdapter(post1Adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL);
        binding.rcvPostSold.addItemDecoration(itemDecoration);
    }

    @Override
    public void requestLogin() {
        binding.rcvPostSold.setVisibility(View.GONE);
        binding.llLogin.setVisibility(View.VISIBLE);
    }
}