package com.edudevel.udacity.aadft_p1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by albertoruiz on 1/5/17.
 */

public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "com.edudevel.udacity.aadft_p1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorite";

    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String TABLE_NAME = "favorites";

        public static final String MOVIE_ID = "movie_id";

    }
}
