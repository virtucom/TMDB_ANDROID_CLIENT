package com.edudevel.udacity.aadft_p1;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.edudevel.udacity.aadft_p1.model.Movie;
import com.edudevel.udacity.aadft_p1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by albertoruiz on 7/2/17.
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder>{

    private ArrayList<Movie> mMovieData;

    private Context mContext;

    private int screenWidth;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final PosterAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface PosterAdapterOnClickHandler {
        void onClick(Movie selectedMovie);
    }

    public PosterAdapter(Activity activity, PosterAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mPosterImageView;

        public PosterAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = (ImageView) view.findViewById(R.id.iv_poster);
            mPosterImageView.setOnClickListener(this);
            mContext = view.getContext();
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie thisMovie = mMovieData.get(adapterPosition);
            mClickHandler.onClick(thisMovie);
        }

    }

    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder posterAdapterViewHolder, int position) {
        Movie thisMovie = mMovieData.get(position);

        Uri uri = NetworkUtils.buildImageUrl(thisMovie.getPosterPath());

        int columnNumber = MainActivity.calculateNoOfColumns(mContext);

        Picasso.with(mContext)
                .load(uri)
                .placeholder(R.drawable.ic_action_image_error)
                .error(R.drawable.ic_action_image_error)
                .resize((int) Math.floor(screenWidth / columnNumber), (int) Math.floor(screenWidth * 1.5 / columnNumber))
                .centerCrop()
                .into(posterAdapterViewHolder.mPosterImageView);

    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public void setMoviesData(ArrayList<Movie> moviesData) {
        mMovieData = moviesData;
        notifyDataSetChanged();
    }

}
