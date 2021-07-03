package me.tuong.chodinh.main.home.minterface;

import java.util.List;

import me.tuong.chodinh.main.home.model.Post;

public interface DetailCategoryInterface {
    void setAddress(String province);

    void getDataAddress(List<String> list);

    void setCate(List<String> listCate);

    void setPost(List<Post> listNew, List<Post> listTopNew);
}
