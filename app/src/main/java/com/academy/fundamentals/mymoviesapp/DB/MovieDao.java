package com.academy.fundamentals.mymoviesapp.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.academy.fundamentals.mymoviesapp.Model.MovieDetailsModel;

import java.util.Collection;
import java.util.List;

/**
 * Data Access Object - an abstract class or interface that includes methods to define
 * database queries.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM MovieDetailsModel ORDER BY popularity DESC")
    List<MovieDetailsModel> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Collection<MovieDetailsModel> movies);

    @Query("DELETE FROM MovieDetailsModel")
    void deleteAll();
}
