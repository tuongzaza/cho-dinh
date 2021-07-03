package me.tuong.chodinh.login.minterface;

import java.util.List;

import me.tuong.chodinh.login.model.User;

public interface RegisterInterface {
    void loginError(String mess);

    void setMa(String code);

    void nextFragment();

    void setListUser(List<User> listU);
}
