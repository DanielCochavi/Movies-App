package com.academy.fundamentals.mymoviesapp.MoviesList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.academy.fundamentals.mymoviesapp.DB.AppDatabase;
import com.academy.fundamentals.mymoviesapp.Details.DetailsActivity;
import com.academy.fundamentals.mymoviesapp.Model.MovieContent;
import com.academy.fundamentals.mymoviesapp.Model.MovieDetailsModel;
import com.academy.fundamentals.mymoviesapp.Networking.MovieResult;
import com.academy.fundamentals.mymoviesapp.Networking.MoviesListResult;
import com.academy.fundamentals.mymoviesapp.Networking.MoviesRetrofitInstance;
import com.academy.fundamentals.mymoviesapp.Networking.MoviesService;
import com.academy.fundamentals.mymoviesapp.R;
import com.academy.fundamentals.mymoviesapp.Services.BGServiceActivity;
import com.academy.fundamentals.mymoviesapp.Threads.AsyncTaskActivity;
import com.academy.fundamentals.mymoviesapp.Threads.ThreadHandlerActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesActivity extends AppCompatActivity implements OnMovieClickListener {

    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    MoviesService moviesService = MoviesRetrofitInstance.getMoviesServiceInstance();

    List<MovieDetailsModel> cachedMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        initRecyclerView();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.title1:
                onAsyncTaskActivityClicked();
                return true;
            case R.id.title2:
                onThreadHandlerActivityClicked();
                return true;
            case R.id.title3:
                onBackgroundServiceActivityClicked();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MoviesAdapter(this, MovieContent.MOVIES, this);
        mRecyclerView.setAdapter(mAdapter);
        loadMovies();
    }

    @Override
    public void onMovieClicked(int itemPosition) {

        if (itemPosition < 0 || itemPosition >= MovieContent.MOVIES.size()) return;

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_ITEM_POSITION, itemPosition);
        startActivity(intent);
    }

    @Override
    public void onAsyncTaskActivityClicked() {
        Intent i = new Intent(MoviesActivity.this, AsyncTaskActivity.class);
        this.startActivity(i);
    }

    @Override
    public void onThreadHandlerActivityClicked() {
        Intent i = new Intent(MoviesActivity.this, ThreadHandlerActivity.class);
        this.startActivity(i);
    }

    @Override
    public void onBackgroundServiceActivityClicked() {
        Intent i = new Intent(MoviesActivity.this, BGServiceActivity.class);
        this.startActivity(i);
    }


    private void loadMovies() {
        MovieContent.MOVIES.clear();
        /*
        MovieDetailsModel movie1 = new MovieDetailsModel();
        MovieDetailsModel movie2 = new MovieDetailsModel();
        MovieDetailsModel movie3 = new MovieDetailsModel();
        MovieDetailsModel movie4 = new MovieDetailsModel();
        MovieDetailsModel movie5 = new MovieDetailsModel();
        MovieDetailsModel movie6 = new MovieDetailsModel();
        MovieDetailsModel movie7 = new MovieDetailsModel();
        MovieDetailsModel movie8 = new MovieDetailsModel();
        MovieDetailsModel movie9 = new MovieDetailsModel();
        MovieDetailsModel movie10 = new MovieDetailsModel();

        movie1.setMovieName("Jurassic World - Fallen Kingdom");
        movie2.setMovieName("Venom");
        movie3.setMovieName("The First Purge");
        movie4.setMovieName("Deadpool 2");
        movie5.setMovieName("Black Panther");
        movie6.setMovieName("Ocean's Eight");
        movie7.setMovieName("Interstellar");
        movie8.setMovieName("Infinity war");
        movie9.setMovieName("Guardians of the Galaxy");
        movie10.setMovieName("The Nutcracker and the Four Realms");

        movie1.setTrailerUrl("https://www.youtube.com/watch?v=vn9mMeWcgoM");
        movie2.setTrailerUrl("https://www.youtube.com/watch?v=bsLk0NPRFAc");
        movie3.setTrailerUrl("https://www.youtube.com/watch?v=UL29y0ah92w");
        movie4.setTrailerUrl("https://www.youtube.com/watch?v=D86RtevtfrA&t=1s");
        movie5.setTrailerUrl("https://www.youtube.com/watch?v=xjDjIWPwcPU");
        movie6.setTrailerUrl("https://www.youtube.com/watch?v=n5LoVcVsiSQ&t=1s");
        movie7.setTrailerUrl("https://www.youtube.com/watch?v=zSWdZVtXT7E");
        movie8.setTrailerUrl("https://www.youtube.com/watch?v=hVSpac8wx3I");
        movie9.setTrailerUrl("https://www.youtube.com/watch?v=2cv2ueYnKjg");
        movie10.setTrailerUrl("https://www.youtube.com/watch?v=BXfxLIuNJvw");


        movie1.setReleaseDate("2018-06-06");
        movie2.setReleaseDate("2018-08-09");
        movie3.setReleaseDate("2018-07-04");
        movie4.setReleaseDate("2018-05-15");
        movie5.setReleaseDate("2018-02-13");
        movie6.setReleaseDate("2018-06-07");
        movie7.setReleaseDate("2014-11-05");
        movie8.setReleaseDate("2017-10-25");
        movie9.setReleaseDate("2014-07-30");
        movie10.setReleaseDate("2019-07-30");


        movie1.setImageResUrl(R.drawable.jurassic2);
        movie2.setImageResUrl(R.drawable.venom2);
        movie3.setImageResUrl(R.drawable.the_first_purge2);
        movie4.setImageResUrl(R.drawable.deadpool_2);
        movie5.setImageResUrl(R.drawable.black_panther2);
        movie6.setImageResUrl(R.drawable.ocean_eight2);
        movie7.setImageResUrl(R.drawable.interstellar2);
        movie8.setImageResUrl(R.drawable.infinity_war_image2);
        movie9.setImageResUrl(R.drawable.guardians_of_the_galaxy2);
        movie10.setImageResUrl(R.drawable.nutcracker2);


        movie1.setBackImageResUrl(R.drawable.jurassic);
        movie2.setBackImageResUrl(R.drawable.venom);
        movie3.setBackImageResUrl(R.drawable.the_first_purge);
        movie4.setBackImageResUrl(R.drawable.deadpool_2_2);
        movie5.setBackImageResUrl(R.drawable.black_panther);
        movie6.setBackImageResUrl(R.drawable.ocean_eight);
        movie7.setBackImageResUrl(R.drawable.interstellar);
        movie8.setBackImageResUrl(R.drawable.infinity_war_image);
        movie9.setBackImageResUrl(R.drawable.guardians_of_the_galaxy);
        movie10.setBackImageResUrl(R.drawable.nutcracker);

        movie1.setOverview("Three years after the demise of Jurassic World, a volcanic eruption threatens the remaining dinosaurs on the isla Nublar, so Claire Dearing, the former park manager, recruits Owen Grady to help prevent the extinction of the dinosaurs once again");
        movie2.setOverview("When Eddie Brock acquires the powers of a symbiote, he will have to release his alter-ego \"Venom\" to save his life.");
        movie3.setOverview("To push the crime rate below one percent for the rest of the year, the New Founding Fathers of America test a sociological theory that vents aggression for one night in one isolated community. But when the violence of oppressors meets the rage of the others, the contagion will explode from the trial-city borders and spread across the nation");
        movie4.setOverview("Wisecracking mercenary Deadpool battles the evil and powerful Cable and other bad guys to save a boy's life");
        movie5.setOverview("King T'Challa returns home from America to the reclusive, technologically advanced African nation of Wakanda to serve as his country's new leader. However, T'Challa soon finds that he is challenged for the throne by factions within his own country as well as without. Using powers reserved to Wakandan kings, T'Challa assumes the Black Panther mantel to join with girlfriend Nakia, the queen-mother, his princess-kid sister, members of the Dora Milaje (the Wakandan 'special forces') and an American secret agent, to prevent Wakanda from being dragged into a world war");
        movie6.setOverview("Debbie Ocean, a criminal mastermind, gathers a crew of female thieves to pull off the heist of the century at New York's annual Met Gala");
        movie7.setOverview("Interstellar chronicles the adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage");
        movie8.setOverview("Three years after the demise of Jurassic World, a volcanic eruption threatens the remaining dinosaurs on the isla Nublar, so Claire Dearing, the former park manager, recruits Owen Grady to help prevent the extinction of the dinosaurs once again");
        movie9.setOverview("Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser");
        movie10.setOverview("A young girl is transported into a magical world of gingerbread soldiers and an army of mice.");

        MovieContent.addMovie(movie1);
        MovieContent.addMovie(movie2);
        MovieContent.addMovie(movie3);
        MovieContent.addMovie(movie4);
        MovieContent.addMovie(movie5);
        MovieContent.addMovie(movie6);
        MovieContent.addMovie(movie7);
        MovieContent.addMovie(movie8);
        MovieContent.addMovie(movie9);
        MovieContent.addMovie(movie10);
*/
        fetchMoviesFromCache();
        asyncGetMoviesListFromTMDB();
    }

    private void fetchMoviesFromCache() {
        cachedMovies = AppDatabase.getInstance(this).movieDao().getAll();

        if (cachedMovies != null && cachedMovies.size() > 0) {
            mAdapter.setData(cachedMovies);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void asyncGetMoviesListFromTMDB() {

        //---> API HTTP call <---//
        /** Call the method with parameter in the interface to get the movies data */
        Call<MoviesListResult> call = moviesService.searchImage();

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<MoviesListResult>() {
            @Override
            public void onResponse(Call<MoviesListResult> call, Response<MoviesListResult> response) {
                int returnCode = response.code();
                if (200 == returnCode) {
                    generateMovieList(response.body().getResults());
                } else {
                    Log.wtf("URL Called", "Return code from " +
                            call.request().url() + " is " + returnCode);
                }
            }

            @Override
            public void onFailure(Call<MoviesListResult> call, Throwable t) {
                Toast.makeText(MoviesActivity.this,
                        "Something went wrong...Error message: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateMovieList(List<MovieResult> results) {
        converter(results);

        fetchMoviesFromTMDB();
        mAdapter.notifyDataSetChanged();
    }

    private void fetchMoviesFromTMDB() {

        if(!MovieContent.MOVIES.equals(cachedMovies)) {
            AppDatabase.getInstance(this).movieDao().deleteAll();

            // save the WEB movies to the adapter
            mAdapter.setData(MovieContent.MOVIES);
            // save updated movies to the cached DB
            AppDatabase.getInstance(this).movieDao().insertAll(MovieContent.MOVIES);
        }
    }

    private void converter(List<MovieResult> movieResultList) {
        if (movieResultList != null) {
            for (MovieResult tmpMovieResult : movieResultList) {
                MovieDetailsModel tmpMovieDetailsModel = new MovieDetailsModel();

                tmpMovieDetailsModel.setMovieId(tmpMovieResult.getId());
                tmpMovieDetailsModel.setMovieName(tmpMovieResult.getTitle());
                tmpMovieDetailsModel.setImageResUrl(tmpMovieResult.getPosterPath());
                tmpMovieDetailsModel.setBackImageResUrl(tmpMovieResult.getBackdropPath());
                tmpMovieDetailsModel.setOverview(tmpMovieResult.getOverview());
                tmpMovieDetailsModel.setReleaseDate(tmpMovieResult.getReleaseDate());
                tmpMovieDetailsModel.setTrailerUrl("https://www.youtube.com/watch?v=y9eWoBKsVJ4");
                tmpMovieDetailsModel.setPopularity(tmpMovieResult.getPopularity());

                MovieContent.addMovie(tmpMovieDetailsModel);
            }
        }
    }
}


