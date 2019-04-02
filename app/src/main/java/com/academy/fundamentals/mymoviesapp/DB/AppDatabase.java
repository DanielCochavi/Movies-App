package com.academy.fundamentals.mymoviesapp.DB;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.academy.fundamentals.mymoviesapp.Model.MovieDetailsModel;

@Database(entities = {MovieDetailsModel.class, VideoModel.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "movies.db";
    private static AppDatabase INSTANCE;

    public abstract MovieDao movieDao();

    public abstract VideoDao videoDao();


    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries() // for tests propose. Not recommended in real project
                    .fallbackToDestructiveMigration()
                    .build();                 // to work on UIThread
        }
        return INSTANCE;
    }

}
