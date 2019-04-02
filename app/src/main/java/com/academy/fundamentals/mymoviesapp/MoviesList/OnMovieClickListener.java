package com.academy.fundamentals.mymoviesapp.MoviesList;

/* When we want a fragment to communicate with an Activity we use a interface.*/

public interface OnMovieClickListener {

    void onMovieClicked(int itemPosition);

    void onAsyncTaskActivityClicked();

    void onThreadHandlerActivityClicked();

    void onBackgroundServiceActivityClicked();
}
