package me.tuong.chodinh.main.more.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentFavoriteBinding;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.adapter.Post1Adapter;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.more.minterface.FavoriteInterface;
import me.tuong.chodinh.main.more.presenter.FavoritePresenter;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class FavoriteFragment extends Fragment implements FavoriteInterface {
    public static final String TAG = FavoriteFragment.class.getName();
    FragmentFavoriteBinding binding;
    private FavoritePresenter favoritePresenter;
    private Post1Adapter post1Adapter;
    private MainActivity mainActivity;


    public FavoriteFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorite, container, false);
        favoritePresenter = new FavoritePresenter(this);
        mainActivity = (MainActivity) getActivity();
        //ẩn thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(false, "Tin đăng đã lưu", getResources().getDrawable(R.drawable.ic_baseline_keyboard_backspace_24));
        setHasOptionsMenu(true);

        favoritePresenter.getInfo(mainActivity);

        return binding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_action_search);
        itemSearch.setVisible(false);
    }

    @Override
    public void requestLogin() {
        binding.rcvPostSave.setVisibility(View.GONE);
        binding.llLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPost(List<Post> listPost) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        gridLayoutManager.setOrientation(VERTICAL);
        binding.rcvPostSave.setNestedScrollingEnabled(false);
        binding.rcvPostSave.setLayoutManager(gridLayoutManager);
        post1Adapter = new Post1Adapter(listPost, new Post1Adapter.IClickItemListener(){
            @Override
            public void onCLickItemUser(Post post) {
                mainActivity.goToDetailPost(post);
            }
        });
        binding.rcvPostSave.setAdapter(post1Adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL);
        binding.rcvPostSave.addItemDecoration(itemDecoration);
    }
}