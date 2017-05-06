package com.edudevel.udacity.aadft_p1.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by albertoruiz on 1/5/17.
 */

public class FavoritesProvider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavoritesDbHelper mFavoritesDbHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;
        final String path = FavoritesContract.PATH_FAVORITES;

        matcher.addURI(authority, path, FAVORITES);
        matcher.addURI(authority, path + "/#", FAVORITES_WITH_ID);

        return matcher;

    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mFavoritesDbHelper = new FavoritesDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case FAVORITES: {
                cursor = mFavoritesDbHelper.getReadableDatabase().query(
                        FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Uri returnUri;

        switch (sUriMatcher.match(uri)) {

            case FAVORITES:
                long id = mFavoritesDbHelper.getWritableDatabase().insert(
                        FavoritesContract.FavoritesEntry.TABLE_NAME, null, values);

                if (id > 0) {

                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoritesEntry.CONTENT_URI, id);

                } else {
                    throw new android.database.SQLException("Failed to inser row into " + uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int numRowsDeleted = 0;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case FAVORITES_WITH_ID:
                numRowsDeleted = mFavoritesDbHelper.getWritableDatabase().delete(
                        FavoritesContract.FavoritesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mFavoritesDbHelper.close();
        super.shutdown();
    }
}
