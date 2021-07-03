package me.tuong.chodinh.main.home.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentDetailUserBinding;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.adapter.Post1Adapter;
import me.tuong.chodinh.main.home.minterface.DetailUserInterface;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.model.UserInfo;
import me.tuong.chodinh.main.home.presenter.DetailUserPresenter;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class DetailUserFragment extends Fragment implements DetailUserInterface {
    public static final String TAG = DetailUserFragment.class.getName();
    FragmentDetailUserBinding binding;
    private DetailUserPresenter detailUserPresenter;
    private UserInfo userInfo;
    private String user_id;
    private Post1Adapter post1Adapter;
    private MainActivity mainActivity;


    public DetailUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail_user, container, false);
        detailUserPresenter = new DetailUserPresenter(this);
        mainActivity = (MainActivity) getActivity();

        //hiện thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(true, "Thông tin người dùng", getResources().getDrawable(R.drawable.ic_baseline_keyboard_backspace_24));

        //lấy dữ liệu
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null){
            user_id = (String) bundleReceive.get("user_id");
            if (user_id != null){
                detailUserPresenter.getUserInfo(user_id);
                detailUserPresenter.getPostUser(user_id);
            }
        }

        return binding.getRoot();
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {


        binding.tvNameShow1.setText(userInfo.getName());
        binding.tvTimeJoin.setText(userInfo.getTimejoin());
        binding.tvAddressUser.setText(userInfo.getAddress());
        if (userInfo.getAvatar().equals("")==false){
            Glide.with(getContext()).load(userInfo.getAvatar()).into(binding.imgAvatarr);
        }
        if (userInfo.getRating()!=0){
            binding.tvRatingUser.setVisibility(View.GONE);
            binding.ratingUser.setVisibility(View.VISIBLE);
            binding.ratingUser.setRating(userInfo.getRating());
        }
    }

    @Override
    public void error() {
        Toast.makeText(getActivity(),"Lỗi call api", Toast.LENGTH_LONG).show();
    }

    @Override
    public void getPost(List<Post> listPost) {
        binding.tvNumPost.setText(" "+listPost.size()+" tin");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        gridLayoutManager.setOrientation(VERTICAL);
        binding.rcvPostUser.setNestedScrollingEnabled(false);
        binding.rcvPostUser.setLayoutManager(gridLayoutManager);
        post1Adapter = new Post1Adapter(listPost, new Post1Adapter.IClickItemListener(){
            @Override
            public void onCLickItemUser(Post post) {
                mainActivity.goToDetailPost(post);
            }
        });
        binding.rcvPostUser.setAdapter(post1Adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL);
        binding.rcvPostUser.addItemDecoration(itemDecoration);
    }
}