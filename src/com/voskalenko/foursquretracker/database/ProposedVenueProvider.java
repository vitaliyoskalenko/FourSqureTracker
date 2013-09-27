/*
 * @(#)ProposedVenueProvider.java  1.0 2013/09/21
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EProvider;
import com.voskalenko.foursquretracker.model.Venue;

@EProvider
public class ProposedVenueProvider extends ContentProvider {

    private static final String AUTHORITY = "com.voskalenko.provider.foursqretracker";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final int URI_PROPOSED_VENUE = 1;

    private static final String CONTENT_TYPE_VENUE = "vnd.android.cursor.dir/venues";

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, Venue.TABLE_NAME, URI_PROPOSED_VENUE);
    }

    @Bean
    DatabaseHelper dbHelper;

    private DatabaseHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] pProjection, String pSelection, String[] pSelectionArgs, String pSortorder) {
        SQLiteDatabase db = getDbHelper().getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case URI_PROPOSED_VENUE:
                queryBuilder.setTables(Venue.TABLE_NAME);
                queryBuilder.appendWhere(Venue.FIELD_MUTED + " = 0 and " + Venue.FIELD_PROPOSED + " = 1");
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return queryBuilder.query(db, pProjection, pSelection, pSelectionArgs, null, null, pSortorder, null);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_PROPOSED_VENUE:
                return CONTENT_TYPE_VENUE;
            default:
                throw new IllegalArgumentException("unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    public static Uri getContentUri(String path) {
        return Uri.withAppendedPath(CONTENT_URI, path);

    }
}