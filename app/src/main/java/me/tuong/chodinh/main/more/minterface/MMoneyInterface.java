package me.tuong.chodinh.main.more.minterface;

import me.tuong.chodinh.main.home.model.UserInfo;

public interface MMoneyInterface {
    void setUserInfo(UserInfo user);

    void setLogin(String name);

    void setMoney(String strtongtien);

    void setNotifi(String mess);
}
