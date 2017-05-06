package com.edudevel.udacity.aadft_p1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edudevel.udacity.aadft_p1.model.Movie;
import com.edudevel.udacity.aadft_p1.model.Video;
import com.edudevel.udacity.aadft_p1.utilities.MoviesJsonUtils;
import com.edudevel.udacity.aadft_p1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler{

    private int screenWidth;

    private Movie mMovie;
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mRelease;
    private TextView mAverage;
    private TextView mOverview;
    private RecyclerView mRecyclerView;

    private VideoAdapter mVideoAdapter;

    private TextView mErrorMessageDisplay;
    private TextView mConnectionErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        mRelease = (TextView) findViewById(R.id.tv_movie_release);
        mAverage = (TextView) findViewById(R.id.tv_movie_average);
        mOverview = (TextView) findViewById(R.id.tv_movie_overview);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display_detail);
        mConnectionErrorMessageDisplay = (TextView) findViewById(R.id.tv_connection_error_message_display_detail);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("selected_movie")) {
                if(isOnline()) {
                    mMovie = (Movie) intentThatStartedThisActivity.getParcelableExtra("selected_movie");
                    String mTitle = mMovie.getTitle();
                    this.mTitle.setText(mTitle);

                    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    screenWidth = size.x;

                    Uri imageUri = NetworkUtils.buildImageUrl(mMovie.getPosterPath());
                    Picasso.with(this)
                            .load(imageUri)
                            .resize(screenWidth / 3, (int) Math.floor(screenWidth * 1.5 / 3))
                            .centerCrop()
                            .into(mPoster);

                    URL videosUri = NetworkUtils.buildVideosUrl(String.valueOf(mMovie.getId()));
                    URL reviewsUri = NetworkUtils.buildReviewsUrl(String.valueOf(mMovie.getId()));

                    this.mRelease.setText(mMovie.getReleaseDate());
                    this.mAverage.setText(mMovie.getVote_average());
                    this.mOverview.setText(mMovie.getOverview());

                    Activity activity = this;

                    Context context = this;

                    GridLayoutManager layoutManager
                            = new GridLayoutManager(context, 1);

                    mRecyclerView.setHasFixedSize(true);

                    mRecyclerView.setLayoutManager(layoutManager);

                    VideoAdapter.VideoAdapterOnClickHandler videoAdapterOnClickHandler = this;

                    mVideoAdapter = new VideoAdapter(activity, videoAdapterOnClickHandler);

                    mRecyclerView.setAdapter(mVideoAdapter);

                    mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator_detail);

                    showVideosDataView();

                    new MovieQueryTask(this).execute(videosUri);
                    //new MovieQueryTask(this).execute(reviewsUri);


                } else {

                    showConnectionErrorMessage();
                }

            }
        }
    }

    // From StackOverflow http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showConnectionErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mConnectionErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showVideosDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, ArrayList<Video>> {

        private Context mContext;

        public MovieQueryTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Video> doInBackground(URL... params) {
            ArrayList<Video> mVideos = null;
            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(params[0]);

                mVideos = MoviesJsonUtils.getVideosFromJson(mContext, jsonResponse);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mVideos;
        }

        @Override
        protected void onPostExecute(ArrayList<Video> videosData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (videosData != null) {
                showVideosDataView();
                mVideoAdapter.setVideosData(videosData);
            } else {
                showConnectionErrorMessage();
            }
        }
    }

        @Override
    public void onClick(Video selectedVideo) {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + selectedVideo.getKey()));
            startActivity(intent);

    }
}