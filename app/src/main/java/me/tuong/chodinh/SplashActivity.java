package me.tuong.chodinh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.databinding.ActivitySplashBinding;
import me.tuong.chodinh.main.home.model.Category;
import me.tuong.chodinh.main.home.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    private List<Category> list;
    private List<Post> listPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        list = new ArrayList<>();
        listPost = new ArrayList<>();

        loadData();
    }

    private void loadData() {
        if (AppUtil.isNetworkAvailable(this)){
            //Network connected
            //Load data
//            getCategory();
//            getPost();

            //thời gian chờ
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);

//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("listCate", (Serializable) list);
//                    bundle.putSerializable("listPost", (Serializable) listPost);
//                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }else {
            Toast.makeText(this, "Không có kết nối internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getPost() {
        ApiService.apiService.getPost().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                listPost = response.body();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }

    private void getCategory() {
        ApiService.apiService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                list = response.body();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }
}