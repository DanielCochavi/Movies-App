package com.academy.fundamentals.mymoviesapp.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class MovieDetailsModel implements Parcelable {

    @PrimaryKey
    private Integer movieId;

    private String movieName;
    private String imageResUrl;
    private String backImageResUrl;
    private String overview;
    private String releaseDate;
    private String trailerUrl;
    private double popularity;

    public MovieDetailsModel() {
    }

    protected MovieDetailsModel(Parcel in) {
        movieName = in.readString();
        imageResUrl = in.readString();
        backImageResUrl = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        trailerUrl = in.readString();
    }

    public static final Creator<MovieDetailsModel> CREATOR = new Creator<MovieDetailsModel>() {

        @Override
        public MovieDetailsModel createFromParcel(Parcel in) {
            return new MovieDetailsModel(in);
        }

        @Override
        public MovieDetailsModel[] newArray(int size) {
            return new MovieDetailsModel[size];
        }
    };

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getImageResUrl() {
        return imageResUrl;
    }

    public void setImageResUrl(String imageResUrl) {
        this.imageResUrl = imageResUrl;
    }

    public String getBackImageResUrl() {
        return backImageResUrl;
    }

    public void setBackImageResUrl(String backImageResUrl) {
        this.backImageResUrl = backImageResUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieName);
        dest.writeString(imageResUrl);
        dest.writeString(backImageResUrl);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(trailerUrl);
    }

    public void setMovieId(Integer id) {
        movieId = id;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
}
