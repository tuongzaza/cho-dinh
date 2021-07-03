package me.tuong.chodinh.login.model;

public class Info {
    private int userid;
    private String userName;
    private int statut;

    public Info(int userid, String userName, int statut) {
        this.userid = userid;
        this.userName = userName;
        this.statut = statut;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }
}
