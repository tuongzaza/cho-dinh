package me.tuong.chodinh.main.home.minterface;

import java.util.List;

import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.model.UserInfo;

public interface DetailUserInterface {
    void setUserInfo(UserInfo userInfo);

    void error();

    void getPost(List<Post> listPost);
}
