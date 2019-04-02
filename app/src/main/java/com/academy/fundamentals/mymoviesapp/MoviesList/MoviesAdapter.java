package com.academy.fundamentals.mymoviesapp.MoviesList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.fundamentals.mymoviesapp.DB.AppDatabase;
import com.academy.fundamentals.mymoviesapp.Model.MovieContent;
import com.academy.fundamentals.mymoviesapp.R;
import com.academy.fundamentals.mymoviesapp.Model.MovieDetailsModel;
import com.squareup.picasso.Picasso;

import java.security.PublicKey;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private OnMovieClickListener movieClickListener;
    private List<MovieDetailsModel> data;

    public MoviesAdapter(Context context, List<MovieDetailsModel> data, OnMovieClickListener listener) {
        this.data = data;
        movieClickListener = listener;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<MovieDetailsModel> data) {
        this.data = data;
        MovieContent.MOVIES.addAll(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(this.data.get(position));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final ImageView ivImage;
        public final TextView tvTitle;
        public final TextView tvOverview;


        public ViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.imageView);
            tvTitle = view.findViewById(R.id.textView);
            tvOverview = view.findViewById(R.id.textView2);
            view.setOnClickListener(this);
        }


        public void bind(MovieDetailsModel movieDetailsModel) {

            tvTitle.setText(movieDetailsModel.getMovieName());
            tvOverview.setText(movieDetailsModel.getOverview());
            if (!TextUtils.isEmpty(movieDetailsModel.getImageResUrl())) {
                Picasso.get()
                        .load("https://image.tmdb.org/t/p/w600_and_h900_bestv2" + movieDetailsModel.getImageResUrl())
                        .into(ivImage);
            }
        }

        @Override
        public void onClick(View view) {
            if (movieClickListener == null) return;
            movieClickListener.onMovieClicked(getAdapterPosition());
        }
    }
}
