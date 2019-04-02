package com.academy.fundamentals.mymoviesapp.Networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesService {

    String BASE_URL = "https://api.themoviedb.org/3/";
    String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    /* api key */
    String apiKey = "2bd8b1f8f29d23972a1499b54d8ef38d";

    String keyQuery = "?api_key=" + apiKey;

    String VIDEOS = "movie/{movie_id}/videos";
    String POPULAR = "movie/popular";

    String POPULAR_QUERY_PATH = POPULAR + keyQuery;
    String VIDEOS_QUERY_PATH = VIDEOS + keyQuery;

    @GET(VIDEOS_QUERY_PATH)
    Call<VideosListResult> getVideos(@Path("movie_id") int movieID);

    @GET(POPULAR_QUERY_PATH)
    Call<MoviesListResult> searchImage();


}
