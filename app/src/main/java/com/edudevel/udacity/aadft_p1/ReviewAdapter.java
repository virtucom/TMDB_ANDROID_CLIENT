package com.edudevel.udacity.aadft_p1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.edudevel.udacity.aadft_p1.model.Review;
import com.edudevel.udacity.aadft_p1.model.Video;

import java.util.ArrayList;

/**
 * Created by albertoruiz on 7/2/17.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private ArrayList<Review> mReviewData;

    private Context mContext;

    private int screenWidth;

    public ReviewAdapter(Activity activity) {

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mReviewImageView;
        public final TextView mReviewTextView;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewImageView = (ImageView) view.findViewById(R.id.iv_review);
            mReviewTextView = (TextView) view.findViewById(R.id.tv_review);
            mContext = view.getContext();
        }

    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        Review thisReview = mReviewData.get(position);

        reviewAdapterViewHolder.mReviewTextView.setText(thisReview.getAuthor() + " : " + thisReview.getContent());

    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.size();
    }

    public void setReviewsData(ArrayList<Review> reviewsData) {
        mReviewData = reviewsData;
        notifyDataSetChanged();
    }

}
