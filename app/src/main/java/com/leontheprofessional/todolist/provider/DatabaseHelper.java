package com.leontheprofessional.todolist.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = DatabaseHelper.class.getSimpleName();

    private Context context;

    public static String TODOLIST_DATABASE_NAME = "todolist_database";

    public static int VERSION = 1;

    public static final int SORTING_STANDARD_ASC = 0;
    public static final int SORTING_STANDARD_DESC = 1;
    public static final String SORT_BY_PRIORITY = "priority";
    public static final String SORT_BY_DEADLINE = "deadline";
    public static final String SORT_BY_TIME_ADDED = "timeAdded";
    public static final String SORT_BY_TITLE = "title";

    public static final String SORTING_ASC = " ASC";
    public static final String SORTING_DESC = " DESC";

    public DatabaseHelper(Context context) {
        super(context, TODOLIST_DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(LOG_TAG, "onCreate(), DatabaseHelper");
        String createDetailedToDoItemListTableQuery = "CREATE TABLE " + ToDoListProviderContract.DetailedToDoItemEntry.TABLE_NAME + " (" +
                ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE + " TEXT NOT NULL, " +
                ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DESCRIPTION + " TEXT, " +
                ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY + " INTEGER DEFAULT 1, " +
                ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE + " TEXT, " +
                ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE + " TEXT NOT NULL, " +
                ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_CATEGORY + " TEXT, " +
                ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE + " INTEGER DEFAULT 1, " +
                ToDoListProviderContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRICE + " REAL DEFAULT 0.0)";
        Log.v(LOG_TAG, "createDetailedToDoItemListTableQuery: " + createDetailedToDoItemListTableQuery);
        db.execSQL(createDetailedToDoItemListTableQuery);

        String createSimpleToDoItemListTableQuery = "CREATE TABLE " + ToDoListProviderContract.SimpleToDoItemEntry.TABLE_NAME + " (" +
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE + " TEXT NOT NULL, " +
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE + " TEXT NOT NULL, " +
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_DEADLINE + " INTEGER DEFAULT 1, " +
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE + " INTEGER DEFAULT 1)";
        Log.v(LOG_TAG, "createSimpleToDoItemListTableQuery: " + createSimpleToDoItemListTableQuery);
        db.execSQL(createSimpleToDoItemListTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgradeDetailedToDoItemListTableQuery = "DROP TABLE IF EXISTS " + ToDoListProviderContract.DetailedToDoItemEntry.TABLE_NAME;
        Log.v(LOG_TAG, "Upgrade " + ToDoListProviderContract.DetailedToDoItemEntry.TABLE_NAME + " from " + oldVersion + " to " + newVersion);
        Log.v(LOG_TAG, "upgradeDetailedToDoItemListTableQuery: " + upgradeDetailedToDoItemListTableQuery);
        db.execSQL(upgradeDetailedToDoItemListTableQuery);

        String upgradeSimpleToDoItemListTableQuery = "DROP TABLE IF EXISTS " + ToDoListProviderContract.SimpleToDoItemEntry.TABLE_NAME;
        Log.v(LOG_TAG, "Upgrade " + ToDoListProviderContract.SimpleToDoItemEntry.TABLE_NAME + " from " + oldVersion + " to " + newVersion);
        Log.v(LOG_TAG, "upgradeSimpleToDoItemListTableQuery: " + upgradeSimpleToDoItemListTableQuery);
        db.execSQL(upgradeSimpleToDoItemListTableQuery);
    }
}
