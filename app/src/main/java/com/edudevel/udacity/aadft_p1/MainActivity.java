package com.edudevel.udacity.aadft_p1;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edudevel.udacity.aadft_p1.data.FavoritesContract;
import com.edudevel.udacity.aadft_p1.model.Movie;
import com.edudevel.udacity.aadft_p1.utilities.MoviesJsonUtils;
import com.edudevel.udacity.aadft_p1.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private PosterAdapter mPosterAdapter;

    private TextView mErrorMessageDisplay;
    private TextView mConnectionErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private boolean show_favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_posters);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mConnectionErrorMessageDisplay = (TextView) findViewById(R.id.tv_connection_error_message_display);

        if(isOnline()) {
            Context context = this;

            GridLayoutManager layoutManager
                    = new GridLayoutManager(context,calculateNoOfColumns(getBaseContext()));

            mRecyclerView.setHasFixedSize(true);

            mRecyclerView.setLayoutManager(layoutManager);

            Activity activity = this;

            PosterAdapter.PosterAdapterOnClickHandler posterAdapterOnClickHandler = this;

            mPosterAdapter = new PosterAdapter(activity, posterAdapterOnClickHandler);

            mRecyclerView.setAdapter(mPosterAdapter);

            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this);

            show_favorites = sharedPreferences.getBoolean("show_favorites", false);

            if(show_favorites) {
                loadMovieData("favorites");
            }else {
                loadMovieData("popular");
            }

        } else {
            showConnectionErrorMessage();
        }

    }

    // From StackOverflow http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_order_popular) {
            loadMovieData("popular");
            return true;
        }else if(id == R.id.action_order_top_rated) {
            loadMovieData("top_rated");
            return true;
        }else if(id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPosterDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showConnectionErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mConnectionErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie selectedMovie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("selected_movie", selectedMovie);
        startActivity(intentToStartDetailActivity);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String order = params[0];

            if(order.equalsIgnoreCase("favorites")) {

                Uri uri = FavoritesContract.FavoritesEntry.CONTENT_URI;
                Cursor data = null;

                try {
                    data = getContentResolver().query(uri, null, null, null, null);
                }catch (Exception e) {
                    e.printStackTrace();
                }

                if (data != null && data.moveToFirst()) {

                    ArrayList<String> favorites = new ArrayList<String>();

                    while(!data.isAfterLast()) {
                        favorites.add(data.getString(1));
                        data.moveToNext();
                    }

                    ArrayList<Movie> moviesData = new ArrayList<Movie>();

                    for (String favorite:favorites) {
                        URL movieRequestUrl = NetworkUtils.buildSingleMovieUrl(favorite);

                        try {
                            String jsonMovieResponse = NetworkUtils
                                    .getResponseFromHttpUrl(movieRequestUrl);

                            Movie movieData = MoviesJsonUtils.getMovieFromJson(MainActivity.this, jsonMovieResponse);

                            moviesData.add(movieData);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    return moviesData;

                }

            }else {
                URL movieRequestUrl = NetworkUtils.buildUrl(order);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    ArrayList<Movie> moviesData = MoviesJsonUtils
                            .getMoviesFromJson(MainActivity.this, jsonMovieResponse);

                    return moviesData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return null;


        }

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showPosterDataView();
                mPosterAdapter.setMoviesData(moviesData);
            } else {
                showConnectionErrorMessage();
            }
        }
    }

    private void loadMovieData(String order) {

        if(isOnline()) {
            showPosterDataView();

            new FetchMoviesTask().execute(order);
        } else {
            showErrorMessage();
        }

    }

    // From StackOverflow http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        boolean new_show_favorites = sharedPreferences.getBoolean("show_favorites", false);

        if(show_favorites != new_show_favorites) {

            this.recreate();
        }

    }

}
