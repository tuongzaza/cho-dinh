package me.tuong.chodinh.main.notification.minterface;

import java.util.List;

import me.tuong.chodinh.main.home.model.Post;

public interface ActiveInterface {
    void requestLogin();

    void setPost(List<Post> listPost);
}
