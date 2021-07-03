package me.tuong.chodinh.main.home.minterface;

import java.util.List;

import me.tuong.chodinh.main.home.model.Category;
import me.tuong.chodinh.main.home.model.Post;

public interface HomeInterface {
    void getSuccess(List<Category> list);
    void getError();

    void getPost(List<Post> list);
}
