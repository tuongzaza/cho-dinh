package me.tuong.chodinh.main.home.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentHomeBinding;
import me.tuong.chodinh.login.model.User;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.PaginationScollListener;
import me.tuong.chodinh.main.home.adapter.CategoryAdapter;
import me.tuong.chodinh.main.home.adapter.PhotoSlideAdapter;
import me.tuong.chodinh.main.home.adapter.PostAdapter;
import me.tuong.chodinh.main.home.minterface.HomeInterface;
import me.tuong.chodinh.main.home.model.Category;
import me.tuong.chodinh.main.home.model.PhotoSlide;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.presenter.HomePresenter;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class HomeFragment extends Fragment implements HomeInterface, SwipeRefreshLayout.OnRefreshListener {
    FragmentHomeBinding binding;
    private HomePresenter homePresenter;
    private Context context;
    private MainActivity mainActivity;
    private PhotoSlideAdapter photoSlideAdapter;
    private CategoryAdapter categoryAdapter;
    private PostAdapter postAdapter;
    private List<PhotoSlide> photoSlides;
    private List<Category> categoryList;
    private List<Post> postList;
    private Timer timer;
    private boolean isLoading;
    private boolean isLastPage;
    private int curentPage = 1;
    private int totalPage;
    private ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        homePresenter = new HomePresenter(this);
        binding.srlLayout.setOnRefreshListener(this);
        mainActivity = (MainActivity) getActivity();

        //hiển thị progressDialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.show();

        //isLoading = true;
        isLastPage = false;
        curentPage = 1;

        categoryList = new ArrayList<>();
        postList = new ArrayList<>();

        //hiện thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(true, "Chợ tốt", null);

        //nhận dữ liệu
//        Bundle bundleReceive = getArguments();
//        if (bundleReceive != null){
//            categoryList = (List<Category>) bundleReceive.get("listCate");
//            postList = (List<Post>) bundleReceive.get("listPost");
//        }

        photoSlides = getListPhoto();
        photoSlideAdapter = new PhotoSlideAdapter(getContext(),photoSlides);

        binding.vpSlide.setAdapter(photoSlideAdapter);
        binding.circleIndi.setViewPager(binding.vpSlide);
        photoSlideAdapter.registerDataSetObserver(binding.circleIndi.getDataSetObserver());

        autoSlideImages();

        homePresenter.getCategory();
        homePresenter.getPost();

        binding.llTinDaLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToFavorite();
            }
        });

        binding.llNapDongTot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToNapDong();
            }
        });


        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);

        return binding.getRoot();
    }

    private List<PhotoSlide> getListPhoto() {
        List<PhotoSlide> list = new ArrayList<>();
        list.add(new PhotoSlide("https://cdn.chotot.com/admincentre/xsVeA1AMTk_306ZaRUe6U435XKVGBzHYpwpRTcbLDF8/preset:raw/plain/4c67f502c8a984b1dca266e567d9e3fd-2717105712225075650.jpg"));
        list.add(new PhotoSlide("http://static.chotot.com/storage/banner/1-NKBan-938x260_v1.jpg"));
        list.add(new PhotoSlide("https://cdn.chotot.com/admincentre/Gj8Tdw4jgKNg1wqtdhDJlTuUd9rC9W1ZLtGPFy95bf0/preset:raw/plain/b33d00ac71b5d4af2878be6f5b77d5a2-2717494009474485008.jpg"));
        list.add(new PhotoSlide("https://cdn.chotot.com/admincentre/AqWmGXsne8XNwuQ3Rk2GIgfVLJHo2npafpd2F1vyVpE/preset:raw/plain/2913932b316b1de0ac0ee5335875044d-2717351472651426730.jpg"));
        return list;
    }

    private void  autoSlideImages(){
        if (photoSlides == null || photoSlides.isEmpty() || binding.vpSlide ==null){
            return;
        }

        if (timer == null){
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = binding.vpSlide.getCurrentItem();
                        int totalItem = photoSlides.size() -1;
                        if(currentItem <totalItem){
                            currentItem ++;
                            binding.vpSlide.setCurrentItem(currentItem);
                        }else {
                            binding.vpSlide.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void getSuccess(List<Category> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        gridLayoutManager.setOrientation(HORIZONTAL);
        binding.rcvCate.setLayoutManager(gridLayoutManager);
        categoryAdapter = new CategoryAdapter(list, new CategoryAdapter.IClickItemListener() {
            @Override
            public void onCLickItemUser(Category category) {
                mainActivity.goToDetailCategory(category);
            }
        });
        binding.rcvCate.setAdapter(categoryAdapter);
    }

    @Override
    public void getError() {

    }

    @Override
    public void getPost(List<Post> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        gridLayoutManager.setOrientation(VERTICAL);
        binding.rcvPost.setNestedScrollingEnabled(false);
        binding.rcvPost.setLayoutManager(gridLayoutManager);

        totalPage = (list.size()/10)+1;

        setFirstData(list);

        binding.rcvPost.addOnScrollListener(new PaginationScollListener(gridLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                curentPage+=1;
                loadNextPage(list);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }

    private void setFirstData(List<Post> list) {
        postList = getListPost(list);
        postAdapter = new PostAdapter(postList, new PostAdapter.IClickItemListener(){
            @Override
            public void onCLickItemUser(Post post) {
                mainActivity.goToDetailPost(post);
            }
        });
        binding.rcvPost.setAdapter(postAdapter);
        if (curentPage < totalPage){
            postAdapter.addFooterLoading();
        }else {
            isLastPage = true;
        }
        //tắt progressDialog
        progressDialog.dismiss();
    }

    private void loadNextPage(List<Post> list) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Post> listPost = getListPost(list);
                postAdapter.removeFooterLoading();
                postList.addAll(listPost);
                postAdapter = new PostAdapter(postList, new PostAdapter.IClickItemListener(){
                    @Override
                    public void onCLickItemUser(Post post) {
                        mainActivity.goToDetailPost(post);
                    }
                });
                postAdapter.notifyDataSetChanged();
                binding.rcvPost.setAdapter(postAdapter);

                isLoading=false;

                if (curentPage < totalPage){
                    postAdapter.addFooterLoading();
                }else {
                    isLastPage = true;
                }
            }
        },1000);
    }

    private List<Post> getListPost(List<Post> list) {
        List<Post> postList = new ArrayList<>();
        for (int i=0;i<10;i++){
            try {
                postList.add(list.get((curentPage-1)*10+i));
            }catch (Exception e){

            }
        }
        return postList;
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.srlLayout.setRefreshing(false);
            }
        },3000);
    }
}