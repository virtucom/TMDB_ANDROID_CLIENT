package com.edudevel.udacity.aadft_p1.utilities;

import android.net.Uri;
import android.util.Log;

import com.edudevel.udacity.aadft_p1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by albertoruiz on 7/2/17.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TMDB_BASE = "https://api.themoviedb.org/3/movie";
    private static final String QUERY_PARAM = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;

    private static final String IMAGE_BASE = "http://image.tmdb.org/t/p/w185/";

    public static URL buildUrl(String orderPreference) {
        Uri builtUri = Uri.parse(TMDB_BASE + "/" + orderPreference).buildUpon()
                .appendQueryParameter(QUERY_PARAM, API_KEY).build();

        return createUrl(builtUri);
    }

    public static Uri buildImageUrl(String posterPath) {
        Uri builtUri = Uri.parse(IMAGE_BASE + "/" + posterPath).buildUpon().build();

        return builtUri;
    }

    public static URL buildVideosUrl(String id) {
        Uri builtUri = Uri.parse(TMDB_BASE + "/" + id + "/videos").buildUpon()
                .appendQueryParameter(QUERY_PARAM, API_KEY).build();

        return createUrl(builtUri);
    }

    public static URL buildReviewsUrl(String id) {
        Uri builtUri = Uri.parse(TMDB_BASE + "/" + id + "/reviews").buildUpon()
                .appendQueryParameter(QUERY_PARAM, API_KEY).build();

        return createUrl(builtUri);
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static URL createUrl(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
