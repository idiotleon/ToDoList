package com.example.tek.first.servant.user;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.example.tek.first.servant.DBHelper;

/**
 * Created by stan on 8/18/15.
 */
public class UserProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buidlUriMatcher();

    //private static final SQLiteQueryBuilder sUserQueryBuidler;
    private static final SQLiteQueryBuilder mSQLiteQueryBuilder;


    static final int USERS = 100;

    static {
        mSQLiteQueryBuilder = new SQLiteQueryBuilder();
        mSQLiteQueryBuilder.setTables(UserContract.UserEntry.TABLE_NAME);
    }

    private DBHelper mOpenHelper;

    //user.username = ? AND password = ?
    public static final String sUsernameSelection =
            UserContract.UserEntry.TABLE_NAME + "." + UserContract.UserEntry.COLUMN_USER_USERNAME
                    + " = ? AND " + UserContract.UserEntry.COLUMN_USER_PASSWORD + " = ? ";

    private static UriMatcher buidlUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = UserContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, UserContract.PATH_USER, USERS);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        if (sUriMatcher.match(uri) == USERS) {
            retCursor = mOpenHelper.getReadableDatabase().query(UserContract.UserEntry.TABLE_NAME,
                    projection, selection, selectionArgs, null, null, sortOrder);

        } else {
            throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case USERS:
                return UserContract.UserEntry.CONTENT_TYPE;
//            case VALIDATE_PASSWORD:
//                Log.i("Here", "Here: " + match);
//                return UserContract.UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @SuppressLint("LongLogTag")
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        Uri returnUri;

        if (match == USERS) {

            try {
                long _id = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
                returnUri = UserContract.UserEntry.buildUserUri(_id);
            } catch (SQLiteConstraintException e) {
                Log.e("Username unique exception: ", e.toString());
                returnUri = UserContract.UserEntry.buildUserUri(-1);
            }


        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (db != null)
            db.close();

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
