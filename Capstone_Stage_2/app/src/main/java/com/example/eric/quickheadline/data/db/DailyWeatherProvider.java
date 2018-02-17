/*
 * Copyright (C) 2018 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.example.eric.quickheadline.data.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.eric.quickheadline.data.db.QHContracts.DailyWeatherEntry;


/**
 * Created by eric on 08/02/2018.
 * <p>
 * This class serves as the ContentProvider for our daily weather forecast. It allows us to
 * bulkInsert data, query all data, and delete all data.Additional methods to perform single
 * inserts and single deletes are not implemented because they are not needed.
 */

public class DailyWeatherProvider extends ContentProvider {

    private static final int CODE_DAILY_WEATHER = 400;

    /*The URI Matcher used by this content provider.*/
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private WeatherDbHelper mOpenHelper;


    /**
     * @return A UriMatcher that correctly matches the constant for CODE_LATEST_ARTICLE
     */
    private static UriMatcher buildUriMatcher() {

        /*
         * 1. UriMatcher matcher
         * 2. CONTENT_AUTHORITY :String (package name)
         */

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = QHContracts.DAILY_WEATHER_CONTENT_AUTHORITY;

        /* This URI is content://com.example.eric.quickheadline/daily */
        matcher.addURI(authority, QHContracts.PATH_DAILY_WEATHER, CODE_DAILY_WEATHER);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        /*constructs a new WeatherDbHelper*/
        mOpenHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_DAILY_WEATHER:
                cursor = db.query(DailyWeatherEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        /*number of rows deleted*/
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_DAILY_WEATHER:
                numRowsDeleted = db.delete(DailyWeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
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
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_DAILY_WEATHER:
                db.beginTransaction();
                int rowsInserted = 0;

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DailyWeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                } finally {
                    db.endTransaction();
                }
                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
