package me.tuong.chodinh.main.home.minterface;

import me.tuong.chodinh.main.home.model.UserInfo;

public interface DetailPostInterface {
    void setTime(String times);

    void setEditPost(String userid, UserInfo user);

    void getSuccess(String mess);

    void getError(String mess);

    void getToast(String mess);

    void setFavoriteOK();

    void showTimeExp(String mess, long timeExp);

    void setUserPostInfo(UserInfo userInfo);
}
