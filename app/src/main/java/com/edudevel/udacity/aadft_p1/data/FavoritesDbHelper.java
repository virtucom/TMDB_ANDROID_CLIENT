package com.edudevel.udacity.aadft_p1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.edudevel.udacity.aadft_p1.data.FavoritesContract.FavoritesEntry;

/**
 * Created by albertoruiz on 1/5/17.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 1;

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_TABLE =

                "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " (" +
                        FavoritesEntry._ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoritesEntry.MOVIE_ID     + " TEXT NOT NULL, "                 +
                        " UNIQUE (" + FavoritesEntry.MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(db);

    }
}
