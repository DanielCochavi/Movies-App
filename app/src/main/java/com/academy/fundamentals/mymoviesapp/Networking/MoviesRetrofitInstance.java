package com.academy.fundamentals.mymoviesapp.Networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRetrofitInstance {

    private static Retrofit retrofit;
    private static MoviesService moviesService;

    /**
     * Create an instance of Retrofit object
     * */
    public static MoviesService getMoviesServiceInstance() {
        if (moviesService == null) {
             retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(MoviesService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            /** Create handle for the MoviesRetrofitInstance interface */
            moviesService = retrofit.create(MoviesService.class);
        }
        return moviesService;
    }
}
