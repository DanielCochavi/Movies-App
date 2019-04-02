package com.academy.fundamentals.mymoviesapp.Details;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.academy.fundamentals.mymoviesapp.DB.AppDatabase;
import com.academy.fundamentals.mymoviesapp.DB.VideoModel;
import com.academy.fundamentals.mymoviesapp.Networking.MoviesRetrofitInstance;
import com.academy.fundamentals.mymoviesapp.Networking.MoviesService;
import com.academy.fundamentals.mymoviesapp.Networking.VideoResult;
import com.academy.fundamentals.mymoviesapp.Networking.VideosListResult;
import com.academy.fundamentals.mymoviesapp.Notifications.DownloadActivity;
import com.academy.fundamentals.mymoviesapp.R;
import com.academy.fundamentals.mymoviesapp.Model.MovieDetailsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesDetailsFragment extends Fragment {

    private static final String ARGS_MOVIE = "MovieDetailsModel-data";
    private static final String TAG = "MovieDetailsFragment";

    private ProgressDialog progressBar;

    private MovieDetailsModel movieDetailsModel;
    private ImageView ivImageId;
    private ImageView ivBackImageId;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvOverview;

    public MoviesDetailsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CounterFragment.
     */

    public static MoviesDetailsFragment newInstance(MovieDetailsModel movieDetailsModel) {

        MoviesDetailsFragment movieFragment = new MoviesDetailsFragment();
        Bundle bundle = new Bundle();

        // putParcelable(String key, Parcelable value)
        // Inserts a Parcelable value into the mapping of this Bundle, replacing any existing value for the given key.
        bundle.putParcelable(ARGS_MOVIE, movieDetailsModel);
        // The way to pass stuff to your Fragment so that they are available after
        // a Fragment is recreated by Android, is to pass a bundle to the setArguments method.
        movieFragment.setArguments(bundle);
        return movieFragment;
    }

    /**
     * If you save the state of the application in a bundle (dynamic data),
     * it can be passed back to onCreate if the activity needs to be recreated (e.g., orientation change).
     * so that you don't lose this prior information. If no data was supplied, savedInstanceState is null.
     * The onCreate() method in a Fragment is called after the Activity's onAttachFragment(),
     * but before that Fragment's onCreateView().
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieDetailsModel = getArguments().getParcelable(ARGS_MOVIE);
        }
        Log.d(TAG, "movieModel: " + movieDetailsModel);
    }

    /**
     * onCreateView is called to inflate the layout of the fragment i.e graphical initialization.
     * After the onCreate() is called (in the Fragment), the Fragment's onCreateView() is called.
     * You can assign your View variables and do any graphical initialisations.
     * You are expected to return a View from this method, and this is the main UI view,
     * but if your Fragment does not use any layouts or graphics, you can return null
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_details, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivImageId = view.findViewById(R.id.details_iv_image);
        ivBackImageId = view.findViewById(R.id.details_iv_back);
        tvTitle = view.findViewById(R.id.details_tv_title);
        tvReleaseDate = view.findViewById(R.id.details_tv_released_date);
        tvOverview = view.findViewById(R.id.details_tv_overview_text);

        Button btnTrailer = view.findViewById(R.id.details_btn_trailer);
        btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingIconWhileWhiting();
                getTrailersListFromCacheOrTMDB();
            }

            private void loadingIconWhileWhiting() {
                progressBar = new ProgressDialog(getActivity());
                progressBar.setMessage("Loading..");
                progressBar.setTitle("Get Data");
                progressBar.setIndeterminate(false);
                progressBar.setCancelable(true);
                progressBar.show();
            }
        });

        ImageButton btnDownload = view.findViewById(R.id.details_btn_download);
        btnDownload.setOnClickListener(v -> DownloadActivity.startActivity(getContext(), movieDetailsModel));
        setMovie();
    }

    private void getTrailersListFromCacheOrTMDB() {
        final MoviesService moviesService = MoviesRetrofitInstance.getMoviesServiceInstance();

        //---> API HTTP call <---//
        /** Call the method with parameter in the interface to get the movies data */
        Call<VideosListResult> call = moviesService.getVideos(movieDetailsModel.getMovieId());

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<VideosListResult>() {
            @Override
            public void onResponse(Call<VideosListResult> call, Response<VideosListResult> response) {
                int returnCode = response.code();
                if (200 == returnCode) {
                    progressBar.dismiss();

                    FragmentActivity activity = getActivity();
                    if (activity == null) {
                        return;
                    }
                    final Context context = activity.getApplicationContext();
                    if (context == null) {
                        return;
                    }

                    VideoModel videoModel =
                            AppDatabase.getInstance(context).videoDao().getVideo(movieDetailsModel.getMovieId());

                    if (videoModel != null) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(moviesService.BASE_YOUTUBE_URL + videoModel.getKey()));
                        startActivity(browserIntent);
                        return;

                    } else {
                        videoModel = convertVideoResult(response.body());
                        AppDatabase.getInstance(context).videoDao().
                                insert(new VideoModel(movieDetailsModel.getMovieId(), videoModel.getId(), videoModel.getKey()));
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(moviesService.BASE_YOUTUBE_URL + videoModel.getKey()));
                        startActivity(browserIntent);

                    }

                } else {
                    Log.wtf("URL Called", "Return code from " +
                            call.request().url() + " is " + returnCode);
                }
            }

            @Override
            public void onFailure(Call<VideosListResult> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Something went wrong...Error message: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static VideoModel convertVideoResult(VideosListResult videosListResult) {
        List<VideoResult> results = videosListResult.getResults();
        if (results != null && !results.isEmpty()) {
            VideoResult videoResult = results.get(0);
            return new VideoModel(videosListResult.getId(), videoResult.getId(), videoResult.getKey());
        }
        return null;
    }


    private void setMovie() {

        if (movieDetailsModel == null) return;

        tvTitle.setText(movieDetailsModel.getMovieName());
        tvReleaseDate.setText(movieDetailsModel.getReleaseDate());
        tvOverview.setText(movieDetailsModel.getOverview());
        if (!TextUtils.isEmpty(movieDetailsModel.getImageResUrl())) {
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w600_and_h900_bestv2" + movieDetailsModel.getImageResUrl())
                    .into(ivImageId);
        }
        if (!TextUtils.isEmpty(movieDetailsModel.getBackImageResUrl())) {
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + movieDetailsModel.getBackImageResUrl())
                    .into(ivBackImageId);
        }
    }
}
