package com.academy.fundamentals.mymoviesapp.Model;

import java.util.ArrayList;

public class MovieContent {

    public static final ArrayList<MovieDetailsModel> MOVIES = new ArrayList<>();

    public static void addMovie(MovieDetailsModel movie) {
        MOVIES.add(movie);
    }

}
