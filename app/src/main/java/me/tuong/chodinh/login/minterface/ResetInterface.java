package me.tuong.chodinh.login.minterface;

public interface ResetInterface {
    void onError(String mess);

    void setMa(String code);

    void getPassword();

    void getSuccess(String mess);
}
