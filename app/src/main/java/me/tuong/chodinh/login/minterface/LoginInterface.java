package me.tuong.chodinh.login.minterface;

import java.util.List;

import me.tuong.chodinh.login.model.User;

public interface LoginInterface {
    void loginSuccess(List<User> mUser);
    void loginError(String mess);

    void gotoAuthenticLoginFragment(User mUser);
}
