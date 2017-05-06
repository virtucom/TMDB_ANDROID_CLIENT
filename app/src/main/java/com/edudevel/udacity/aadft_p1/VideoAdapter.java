package com.edudevel.udacity.aadft_p1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.edudevel.udacity.aadft_p1.model.Movie;
import com.edudevel.udacity.aadft_p1.model.Video;
import com.edudevel.udacity.aadft_p1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by albertoruiz on 7/2/17.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder>{

    private ArrayList<Video> mVideoData;

    private Context mContext;

    private int screenWidth;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final VideoAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface VideoAdapterOnClickHandler {
        void onClick(Video selectedVideo);
    }

    public VideoAdapter(Activity activity, VideoAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mVideoImageView;
        public final TextView mVideoTextView;

        public VideoAdapterViewHolder(View view) {
            super(view);
            mVideoImageView = (ImageView) view.findViewById(R.id.iv_video);
            mVideoTextView = (TextView) view.findViewById(R.id.tv_video_name);
            mVideoImageView.setOnClickListener(this);
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
            Video thisVideo = mVideoData.get(adapterPosition);
            mClickHandler.onClick(thisVideo);
        }

    }

    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapterViewHolder videoAdapterViewHolder, int position) {
        Video thisVideo = mVideoData.get(position);

        videoAdapterViewHolder.mVideoTextView.setText(thisVideo.getName());

    }

    @Override
    public int getItemCount() {
        if (null == mVideoData) return 0;
        return mVideoData.size();
    }

    public void setVideosData(ArrayList<Video> videosData) {
        mVideoData = videosData;
        notifyDataSetChanged();
    }

}
