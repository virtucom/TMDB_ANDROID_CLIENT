package com.edudevel.udacity.aadft_p1.utilities;

import android.content.Context;
import android.util.Log;

import com.edudevel.udacity.aadft_p1.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by albertoruiz on 7/2/17.
 */

public final class MoviesJsonUtils {

    private static final String DEBUG_TAG = "MoviesJsonUtils";

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
            Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + moviesJsonStr);
        }
        return results;


    }
}
