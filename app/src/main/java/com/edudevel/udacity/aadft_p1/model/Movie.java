package com.edudevel.udacity.aadft_p1.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by albertoruiz on 7/2/17.
 */

public class Movie implements Parcelable {

    private int id;
    private String popularity;
    private String posterPath;
    private String releaseDate;
    private String title;
    private String overview;
    private  String vote_average;

    public int getId() {
        return id;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(popularity);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(vote_average);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Movie(Parcel in) {
        id = in.readInt();
        popularity = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        overview = in.readString();
        vote_average = in.readString();
    }

    public Movie() {

    }
}
