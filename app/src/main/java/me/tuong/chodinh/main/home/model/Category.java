package me.tuong.chodinh.main.home.model;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;
    private String categoryName;
    private String categoryThumb;

    public Category(int id, String categoryName, String categoryThumb) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryThumb = categoryThumb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryThumb() {
        return categoryThumb;
    }

    public void setCategoryThumb(String categoryThumb) {
        this.categoryThumb = categoryThumb;
    }
}
