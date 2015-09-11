package com.leontheprofessional.todolist.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;


public class ToDoListProvider extends ContentProvider {

    private DatabaseHelper dbHelper;

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
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
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
