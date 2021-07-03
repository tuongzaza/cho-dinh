package me.tuong.chodinh.main.home.model;

public class UserInfo {

    private String id;
    private String name;
    private String timejoin;
    private String address;
    private float rating;
    private String emailreal;
    private String avatar;
    private String password;
    private String email;
    private String dong;

    public UserInfo(String id, String name, String timejoin, String address, float rating, String emailreal, String avatar, String password, String email, String dong) {
        this.id = id;
        this.name = name;
        this.timejoin = timejoin;
        this.address = address;
        this.rating = rating;
        this.emailreal = emailreal;
        this.avatar = avatar;
        this.password = password;
        this.email = email;
        this.dong = dong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimejoin() {
        return timejoin;
    }

    public void setTimejoin(String timejoin) {
        this.timejoin = timejoin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getEmailreal() {
        return emailreal;
    }

    public void setEmailreal(String emailreal) {
        this.emailreal = emailreal;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }
}
