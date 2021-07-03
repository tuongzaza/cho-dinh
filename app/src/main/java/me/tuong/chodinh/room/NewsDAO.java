package me.tuong.chodinh.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDAO {
    @Insert
    void insertNews(News news);

    @Query("SELECT * FROM news")
    List<News> getListNews();

    @Delete
    void deleteNews(News news);

    @Query("DELETE FROM news")
    void deleteAllNews();
}
