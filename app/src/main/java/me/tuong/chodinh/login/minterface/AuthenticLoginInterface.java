package me.tuong.chodinh.login.minterface;

public interface AuthenticLoginInterface {
    void setMa(String code);

    void onError(String message);

    void getSdt();
}
