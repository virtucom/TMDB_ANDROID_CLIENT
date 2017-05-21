package com.edudevel.udacity.aadft_p1.utilities;

import android.content.Context;
import android.util.Log;

import com.edudevel.udacity.aadft_p1.model.Movie;
import com.edudevel.udacity.aadft_p1.model.Review;
import com.edudevel.udacity.aadft_p1.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by albertoruiz on 7/2/17.
 */

public final class MoviesJsonUtils {

    private static final String DEBUG_TAG_MOVIES = "MoviesJsonUtils";
    private static final String DEBUG_TAG_VIDEOS = "VideosJsonUtils";
    private static final String DEBUG_TAG_REVIEWS = "ReviewsJsonUtils";

    public static ArrayList<Movie> getMoviesFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        ArrayList<Movie> results = new ArrayList<Movie>();
        try {
            JSONObject jsonObject = new JSONObject(moviesJsonStr);
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonMovieObject = array.getJSONObject(i);
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(jsonMovieObject.getString("id")));
                movie.setTitle(jsonMovieObject.getString("title"));
                movie.setPopularity(jsonMovieObject.getString("popularity"));
                movie.setPosterPath(jsonMovieObject.getString("poster_path"));
                movie.setReleaseDate(jsonMovieObject.getString("release_date"));
                movie.setOverview(jsonMovieObject.getString("overview"));
                movie.setVote_average(jsonMovieObject.getString("vote_average"));
                results.add(movie);
            }
        } catch (JSONException e) {
            System.err.println(e);
            Log.d(DEBUG_TAG_MOVIES, "Error parsing JSON. String was: " + moviesJsonStr);
        }
        return results;

    }

    public static Movie getMovieFromJson(Context context, String movieJsonStr)
            throws JSONException {

        Movie movie = new Movie();

        try {
            JSONObject jsonMovieObject = new JSONObject(movieJsonStr);
            movie.setId(Integer.parseInt(jsonMovieObject.getString("id")));
            movie.setTitle(jsonMovieObject.getString("title"));
            movie.setPopularity(jsonMovieObject.getString("popularity"));
            movie.setPosterPath(jsonMovieObject.getString("poster_path"));
            movie.setReleaseDate(jsonMovieObject.getString("release_date"));
            movie.setOverview(jsonMovieObject.getString("overview"));
            movie.setVote_average(jsonMovieObject.getString("vote_average"));
        } catch (JSONException e) {
            System.err.println(e);
            Log.d(DEBUG_TAG_MOVIES, "Error parsing JSON. String was: " + movieJsonStr);
        }
        return movie;

    }

    public static ArrayList<Video> getVideosFromJson(Context context, String videosJsonStr)
            throws JSONException {

        ArrayList<Video> results = new ArrayList<Video>();
        try {
            JSONObject jsonObject = new JSONObject(videosJsonStr);
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonVideoObject = array.getJSONObject(i);
                Video video = new Video();
                video.setId(jsonVideoObject.getString("id"));
                video.setSize(Integer.parseInt(jsonVideoObject.getString("size")));
                video.setIso_639_1(jsonVideoObject.getString("iso_639_1"));
                video.setIso_3166_1(jsonVideoObject.getString("iso_3166_1"));
                video.setKey(jsonVideoObject.getString("key"));
                video.setName(jsonVideoObject.getString("name"));
                video.setSite(jsonVideoObject.getString("site"));
                video.setType(jsonVideoObject.getString("type"));
                results.add(video);
            }
        } catch (JSONException e) {
            System.err.println(e);
            Log.d(DEBUG_TAG_VIDEOS, "Error parsing JSON. String was: " + videosJsonStr);
        }
        return results;

    }

    public static ArrayList<Review> getReviewsFromJson(Context context, String reviewsJsonStr)
            throws JSONException {

        ArrayList<Review> results = new ArrayList<Review>();
        try {
            JSONObject jsonObject = new JSONObject(reviewsJsonStr);
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonReviewObject = array.getJSONObject(i);
                Review review = new Review();
                review.setId(jsonReviewObject.getString("id"));
                review.setAuthor(jsonReviewObject.getString("author"));
                review.setContent(jsonReviewObject.getString("content"));
                review.setUrl(jsonReviewObject.getString("url"));
                results.add(review);
            }
        } catch (JSONException e) {
            System.err.println(e);
            Log.d(DEBUG_TAG_REVIEWS, "Error parsing JSON. String was: " + reviewsJsonStr);
        }
        return results;

    }
}
