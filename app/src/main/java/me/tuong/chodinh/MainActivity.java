package me.tuong.chodinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.List;

import me.tuong.chodinh.databinding.ActivityMainBinding;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.fragment.DetailCategoryFragment;
import me.tuong.chodinh.main.home.fragment.DetailPostFragment;
import me.tuong.chodinh.main.home.fragment.DetailUserFragment;
import me.tuong.chodinh.main.home.fragment.HomeFragment;
import me.tuong.chodinh.main.home.fragment.MapsFragment;
import me.tuong.chodinh.main.home.model.Category;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.more.fragment.EditUserFragment;
import me.tuong.chodinh.main.more.fragment.FavoriteFragment;
import me.tuong.chodinh.main.more.fragment.MoneyFragment;
import me.tuong.chodinh.main.more.fragment.MoreFragment;
import me.tuong.chodinh.main.notification.fragment.NotifiFragment;
import me.tuong.chodinh.main.post.fragment.PostFragment;
import me.tuong.chodinh.main.sell.fragment.SellFragment;


public class MainActivity extends AppCompatActivity implements NavHideShow {
    ActivityMainBinding binding;
    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final CharSequence CHANNEL_NAME = "Chợ Đình";
    public static final String CHANNEL_DESC = "Đình chợ";
//    private List<Category> list;
//    private List<Post> listPost;


    @Override
    public void setNavHideShow(boolean enabled, String title, Drawable drawable) {
        if (enabled){
            binding.bottomNav.setVisibility(View.VISIBLE);
            binding.bottomAppBar.setVisibility(View.VISIBLE);
            binding.btnPost.setVisibility(View.VISIBLE);
        }else {
            binding.bottomNav.setVisibility(View.INVISIBLE);
            binding.bottomAppBar.setVisibility(View.INVISIBLE);
            binding.btnPost.setVisibility(View.INVISIBLE);
        }
        binding.topAppBar.setTitle(title);
        binding.topAppBar.setNavigationIcon(drawable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        setSupportActionBar(binding.topAppBar);

        //nhận dữ liệu
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            list = (List<Category>) bundle.get("listCate");
//            listPost = (List<Post>) bundle.get("listPost");
//        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment homeFragment = new HomeFragment();
//        Bundle bundlehome = new Bundle();
//        bundlehome.putSerializable("listCate", (Serializable) list);
//        bundlehome.putSerializable("listPost", (Serializable) listPost);
//        homeFragment.setArguments(bundlehome);
        fragmentTransaction.replace(R.id.frameContainer, homeFragment);
        fragmentTransaction.commit();


        //loadFragment(new HomeFragment());

        //Creating notification channel for devices on and above Android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment postFragmment = new PostFragment();
                transaction.replace(R.id.frameContainer, postFragmment);
                transaction.addToBackStack(PostFragment.TAG);
                transaction.commit();
            }
        });

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.action_home:
                        loadFragment(new HomeFragment());
                        return true;
                    case R.id.action_sell:
                        loadFragment(new SellFragment());
                        return true;
                    case R.id.action_notifi:
                        loadFragment(new NotifiFragment());
                        return true;
                    case R.id.action_more:
                        loadFragment(new MoreFragment());
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        MenuItem item = menu.findItem(R.id.menu_action_search);
        SearchManager searchManager = (SearchManager) getSystemService(getApplicationContext().SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Tìm kiếm chợ Tốt");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQueryHint(query);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                DetailCategoryFragment detailCategoryFragment = new DetailCategoryFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("key", query);
                detailCategoryFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frameContainer, detailCategoryFragment);
                fragmentTransaction.addToBackStack(DetailCategoryFragment.TAG);
                fragmentTransaction.commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                DetailCategoryFragment detailCategoryFragment = new DetailCategoryFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("key", newText);
//                detailCategoryFragment.setArguments(bundle);
//                fragmentTransaction.replace(R.id.frameContainer, detailCategoryFragment);
//                fragmentTransaction.addToBackStack(DetailCategoryFragment.TAG);
//                fragmentTransaction.commit();
                return false;
            }
        });
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                DetailCategoryFragment detailCategoryFragment = new DetailCategoryFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("key", key);
//                detailCategoryFragment.setArguments(bundle);
//                fragmentTransaction.replace(R.id.frameContainer, detailCategoryFragment);
//                fragmentTransaction.addToBackStack(DetailCategoryFragment.TAG);
//                fragmentTransaction.commit();
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (getSupportFragmentManager() != null){
                    getSupportFragmentManager().popBackStack();
                }
                return true;

            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goToDetailPost(Post post) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailPostFragment detailPostFragment = new DetailPostFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_post", post);
        detailPostFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameContainer, detailPostFragment);
        fragmentTransaction.addToBackStack(DetailPostFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToDetailProfile(String user_id) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailUserFragment detailUserFragment = new DetailUserFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("user_id", user_id);
        detailUserFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameContainer, detailUserFragment);
        fragmentTransaction.addToBackStack(DetailUserFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToEditPost(Post post) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PostFragment postFragment = new PostFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_post", post);
        postFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameContainer, postFragment);
        fragmentTransaction.addToBackStack(PostFragment.TAG);
        fragmentTransaction.commit();

    }

    public void goToSellFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SellFragment sellFragment = new SellFragment();
        fragmentTransaction.replace(R.id.frameContainer, sellFragment);
        fragmentTransaction.addToBackStack(SellFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToDetailCategory(Category category) {
        String categoryName = category.getCategoryName();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailCategoryFragment detailCategoryFragment = new DetailCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("category_name", categoryName);
        detailCategoryFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameContainer, detailCategoryFragment);
        fragmentTransaction.addToBackStack(DetailCategoryFragment.TAG);
        fragmentTransaction.commit();

    }

    public void goToFavorite() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        fragmentTransaction.replace(R.id.frameContainer, favoriteFragment);
        fragmentTransaction.addToBackStack(FavoriteFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToEditUser() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        EditUserFragment editUserFragment = new EditUserFragment();
        fragmentTransaction.replace(R.id.frameContainer, editUserFragment);
        fragmentTransaction.addToBackStack(EditUserFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToNapDong() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MoneyFragment moneyFragment = new MoneyFragment();
        fragmentTransaction.replace(R.id.frameContainer, moneyFragment);
        fragmentTransaction.addToBackStack(MoneyFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToMap(String getAddress) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MapsFragment mapsFragment = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("address", getAddress);
        mapsFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameContainer, mapsFragment);
        fragmentTransaction.addToBackStack(MapsFragment.TAG);
        fragmentTransaction.commit();
    }
}