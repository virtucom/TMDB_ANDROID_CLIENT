package com.edudevel.udacity.aadft_p1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.edudevel.udacity.aadft_p1.model.Movie;
import com.edudevel.udacity.aadft_p1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private int screenWidth;

    private Movie mMovie;
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mRelease;
    private TextView mAverage;
    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        mRelease = (TextView) findViewById(R.id.tv_movie_release);
        mAverage = (TextView) findViewById(R.id.tv_movie_average);
        mOverview = (TextView) findViewById(R.id.tv_movie_overview);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("selected_movie")) {
                mMovie = (Movie) intentThatStartedThisActivity.getParcelableExtra("selected_movie");
                String mTitle = mMovie.getTitle();
                this.mTitle.setText(mTitle);

                WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                screenWidth = size.x;

                Uri uri = NetworkUtils.buildImageUrl(mMovie.getPosterPath());
                Picasso.with(this)
                        .load(uri)
                        .resize(screenWidth / 3, (int) Math.floor(screenWidth * 1.5 / 3))
                        .centerCrop()
                        .into(mPoster);

                this.mRelease.setText(mMovie.getReleaseDate());
                this.mAverage.setText(mMovie.getVote_average());
                this.mOverview.setText(mMovie.getOverview());

            }
        }
    }
}