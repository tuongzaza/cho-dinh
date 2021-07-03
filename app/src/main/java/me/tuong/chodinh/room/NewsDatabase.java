package me.tuong.chodinh.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {News.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "news.db";
    private static NewsDatabase instance;

    public static synchronized NewsDatabase getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),NewsDatabase.class,DATABASE_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract NewsDAO newsDAO();
}
