package com.leontheprofessional.todolist.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Text;


public class ToDoListProvider extends ContentProvider {

    private static final String LOG_TAG = ToDoListProvider.class.getSimpleName();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private static final int SIMPLE_TODO_LIST = 100;
    private static final int SIMPLE_TODO_ITEM = 101;
    private static final int DETAILED_TODO_LIST = 200;
    private static final int DETAILED_TODO_ITEM = 201;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ToDoListProviderContract.CONTENT_AUTHORITY, ToDoListProviderContract.PATH_SIMPLE_TODOITEM, SIMPLE_TODO_LIST);
        uriMatcher.addURI(ToDoListProviderContract.CONTENT_AUTHORITY, ToDoListProviderContract.PATH_SIMPLE_TODOITEM + "/#", SIMPLE_TODO_ITEM);
        uriMatcher.addURI(ToDoListProviderContract.CONTENT_AUTHORITY, ToDoListProviderContract.PATH_DETAILED_TODOITEM, DETAILED_TODO_LIST);
        uriMatcher.addURI(ToDoListProviderContract.CONTENT_AUTHORITY, ToDoListProviderContract.PATH_DETAILED_TODOITEM + "/#", DETAILED_TODO_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        openDatabase();

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String rowId = "0";

        switch (uriMatcher.match(uri)) {
            case SIMPLE_TODO_LIST:
                queryBuilder.setTables(ToDoListProviderContract.SimpleToDoItemEntry.TABLE_NAME);
                break;
            case SIMPLE_TODO_ITEM:
                rowId = uri.getPathSegments().get(1);
                queryBuilder.setTables(ToDoListProviderContract.SimpleToDoItemEntry.TABLE_NAME);
                queryBuilder.appendWhere(ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_ID + " = " + rowId);
                break;
            case DETAILED_TODO_LIST:
                queryBuilder.setTables(ToDoListProviderContract.DetailedToDoItemEntry.TABLE_NAME);
                break;
            case DETAILED_TODO_ITEM:
                rowId = uri.getPathSegments().get(1);
                queryBuilder.setTables(ToDoListProviderContract.DetailedToDoItemEntry.TABLE_NAME);
                queryBuilder.appendWhere(ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_ID + " = " + rowId);
                break;
        }

        Log.v(LOG_TAG, "queryBuilder.toString(), ToDoListProvider: " + queryBuilder.toString());

        // Apply the query to the underlying database
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, groupBy, having, sortOrder);

        // Register the contexts ContentResolver to be notified if the cursor result set changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return a cursor as the queried result
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.v(LOG_TAG, "getType(Uri uri), ToDoListProvider executed");

        switch (uriMatcher.match(uri)) {
            case SIMPLE_TODO_LIST:
                return ToDoListProviderContract.SimpleToDoItemEntry.CONTENT_TYPE;
            case SIMPLE_TODO_ITEM:
                return ToDoListProviderContract.SimpleToDoItemEntry.CONTNET_ITEM_TYPE;
            case DETAILED_TODO_LIST:
                return ToDoListProviderContract.DetailedToDoItemEntry.CONTENT_TYPE;
            case DETAILED_TODO_ITEM:
                return ToDoListProviderContract.DetailedToDoItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(LOG_TAG, "insert(Uri uri, ContentValues values), ToDoListProvider executed");
        openDatabase();

        String nullColumnHack = null;

        long id;
        Uri insertedId;
        switch (uriMatcher.match(uri)) {
            case SIMPLE_TODO_ITEM:
                id = database.insert(ToDoListProviderContract.SimpleToDoItemEntry.TABLE_NAME, nullColumnHack, values);
                if (id > -1) {
                    insertedId = ContentUris.withAppendedId(ToDoListProviderContract.SimpleToDoItemEntry.CONTENT_URI, id);
                    getContext().getContentResolver().notifyChange(insertedId, null);
                    return insertedId;
                }
            case DETAILED_TODO_ITEM:
                id = database.insert(ToDoListProviderContract.DetailedToDoItemEntry.TABLE_NAME, nullColumnHack, values);
                if (id > -1) {
                    insertedId = ContentUris.withAppendedId(ToDoListProviderContract.DetailedToDoItemEntry.CONTENT_URI, id);
                    getContext().getContentResolver().notifyChange(insertedId, null);
                    return insertedId;
                }
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        openDatabase();

        String rowId;
        int deleteCount = 0;
        switch (uriMatcher.match(uri)) {

            case SIMPLE_TODO_ITEM:
                rowId = uri.getPathSegments().get(1);
                selection = ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_ID = " = " + rowId +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                if (selection == null) selection = "1";
                deleteCount = database.delete(ToDoListProviderContract.SimpleToDoItemEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case DETAILED_TODO_ITEM:
                rowId = uri.getPathSegments().get(1);
                selection = ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_ID = " = " + rowId +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                if (selection == null) selection = "1";
                deleteCount = database.delete(ToDoListProviderContract.DetailedToDoItemEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
        }
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private void openDatabase() throws SQLiteException {
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            database = dbHelper.getReadableDatabase();
        }
    }
}
