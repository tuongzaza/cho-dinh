package me.tuong.chodinh.main.post.minterface;

import android.net.Uri;

import java.util.List;

import me.tuong.chodinh.main.home.model.Province;

public interface PostInterface {

    void getError(String mess);


    void getPhoto(List<Uri> uriList);

    void getCate(List<String> list);

    void setUser(int info);

    void getUploadPhoto(List<String> imageData);

    void setAddress(String address, String province, String district);

    void getDataAddress(List<Province> list);

    void getSuccess(String mess);

    void requestLogin();
}
