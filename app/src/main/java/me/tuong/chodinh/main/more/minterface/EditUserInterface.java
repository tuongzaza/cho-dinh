package me.tuong.chodinh.main.more.minterface;

import android.graphics.Bitmap;

import me.tuong.chodinh.main.home.model.UserInfo;

public interface EditUserInterface {

    void setUserInfo(UserInfo user);

    void wrongPassword();

    void messenger(String mess);
}
