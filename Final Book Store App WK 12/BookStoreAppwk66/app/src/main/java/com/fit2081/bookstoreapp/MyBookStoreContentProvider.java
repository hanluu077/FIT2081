package com.fit2081.bookstoreapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyBookStoreContentProvider extends ContentProvider {
    public static final String CONTENT_AUTHORITY = "fit2081.app.HAN";
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    BookDatabase db;
    private static final int BOOKS = 1;

    UriMatcher uriMatcher=createUriMatcher();
    public MyBookStoreContentProvider() {
    }
    private static UriMatcher createUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = "monash.fit.units";
        uriMatcher.addURI(authority, "books", BOOKS);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        db = BookDatabase.getDatabase(getContext());
        return true;
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("week8tasks","query");
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Books.TABLE_NAME);
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);
        Log.d("week8tasks",query);
        final Cursor cursor = db
                .getOpenHelper()
                .getReadableDatabase()
                .query(query);
        Log.d("week8tasks", String.valueOf(cursor!=null));
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long rowId = db
                .getOpenHelper()
                .getWritableDatabase()
                .insert(Books.TABLE_NAME, 0, contentValues);

        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletionCount;

        deletionCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .delete(Books.TABLE_NAME, selection, selectionArgs);

        return deletionCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount;
        updateCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .update(Books.TABLE_NAME, 0, values, selection, selectionArgs);

        return updateCount;
    }
}
