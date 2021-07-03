package me.tuong.chodinh.main.home.presenter;

import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.main.home.minterface.DetailUserInterface;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.model.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserPresenter {
    private DetailUserInterface detailUserInterface;
    private UserInfo userInfo;

    public DetailUserPresenter(DetailUserInterface detailUserInterface) {
        this.detailUserInterface = detailUserInterface;
    }

    public void getUserInfo(String user_id) {
        ApiService.apiService.getListUserInfo().enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                List<UserInfo> list = new ArrayList<>();
                list = response.body();
                for (int $i = 0; $i < list.size(); $i++){
                    if (list.get($i).getId().equals(user_id)){

                        userInfo = list.get($i);

                        detailUserInterface.setUserInfo(userInfo);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                detailUserInterface.error();
            }
        });
    }

    public void getPostUser(String user_id) {
        ApiService.apiService.getPost().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> list = new ArrayList<>();
                List<Post> listPost = new ArrayList<>();
                list = response.body();
                for (int $i = 0; $i < list.size(); $i++){
                    if (list.get($i).getUser_id().equals(user_id)){
                        listPost.add(list.get($i));
                    }
                }
                detailUserInterface.getPost(listPost);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }
}
