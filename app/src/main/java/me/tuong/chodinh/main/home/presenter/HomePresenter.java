package me.tuong.chodinh.main.home.presenter;

import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.main.home.minterface.HomeInterface;
import me.tuong.chodinh.main.home.model.Category;
import me.tuong.chodinh.main.home.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter {
    private HomeInterface homeInterface;

    public HomePresenter(HomeInterface homeInterface) {
        this.homeInterface = homeInterface;
    }



    public void getCategory() {

        ApiService.apiService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> list = new ArrayList<>();
                list = response.body();
                homeInterface.getSuccess(list);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    public void getPost() {

        ApiService.apiService.getPost().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> list = new ArrayList<>();
                list = response.body();
                homeInterface.getPost(list);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });

    }
}
