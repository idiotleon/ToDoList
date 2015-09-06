package com.example.tek.first.servant.todolist.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.tek.first.servant.todolist.model.SimpleToDoItem;
import com.example.tek.first.servant.todolist.helper.GeneralHelper.CompletionStatus;
import com.example.tek.first.servant.todolist.model.ToDoItem;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = DatabaseHelper.class.getSimpleName();

    private Context context;

    public static String TODOLIST_DATABASE_NAME = "todolist_database";
    public static String TODOLIST_TABLE_NAME = "todolist_table";

    public static String TODOLIST_ITEM_COLUMN_ID = "todolist_item_id";
    public static String TODOLIST_ITEM_COLUMN_TITLE = "todolist_item_title";
    public static String TODOLIST_ITEM_COLUMN_DESCRIPTION = "todolist_tem_description";
    public static String TODOLIST_ITEM_COLUMN_PRIORITY = "todolist_item_priority";
    public static String TODOLIST_ITEM_COLUMN_DEADLINE = "todolist_item_time_date_set";
    public static String TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED = "todolist_item_time_date_created";
    public static String TODOLIST_ITEM_COLUMN_CATEGORY = "todolist_item_category";
    public static String TODOLIST_ITEM_COLUMN_PRICE = "todolist_item_price";
    public static String TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE = "todolist_item_completion_status_code";

    public static int VERSION = 1;

    public static final int SORTING_STANDARD_ASC = 0;
    public static final int SORTING_STANDARD_DESC = 1;
    public static final String SORTING_WAY_BY_PRIORITY = "priority";
    public static final String SORTING_WAY_BY_DEADLINE = "deadline";
    public static final String SORTING_WAY_BY_TIME_ADDED = "timeAdded";
    public static final String SORTING_WAY_BY_TITLE = "title";

    public static final String SORTING_ASC = " ASC";
    public static final String SORTING_DESC = " DESC";

    public DatabaseHelper(Context context) {
        super(context, TODOLIST_DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TODOLIST_TABLE_NAME + " (" +
                TODOLIST_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TODOLIST_ITEM_COLUMN_TITLE + " TEXT NOT NULL, " +
                TODOLIST_ITEM_COLUMN_DESCRIPTION + " TEXT, " +
                TODOLIST_ITEM_COLUMN_PRIORITY + " INTEGER DEFAULT 1, " +
                TODOLIST_ITEM_COLUMN_DEADLINE + " TEXT, " +
                TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED + " TEXT, " +
                TODOLIST_ITEM_COLUMN_CATEGORY + " TEXT, " +
                TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE + " INTEGER DEFAULT 1, " +
                TODOLIST_ITEM_COLUMN_PRICE + " REAL DEFAULT 0.0)";
        Log.v(LOG_TAG, "createTableQuery: " + createTableQuery);

        Log.v(LOG_TAG, "onCreate(), DatabaseHelper");

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgradeTableQuery = "DROP TABLE IF EXISTS " + TODOLIST_TABLE_NAME;
        Log.v(LOG_TAG, "Upgrade database from " + oldVersion + " to " + newVersion);
        Log.v(LOG_TAG, "upgradeTableQuery: " + upgradeTableQuery);
        db.execSQL(upgradeTableQuery);
    }

    public boolean insertToDoListItem(ToDoItem toDoListItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_COLUMN_TITLE, toDoListItem.getTitle());
        contentValues.put(TODOLIST_ITEM_COLUMN_DESCRIPTION, toDoListItem.getDetailDescription());
        contentValues.put(TODOLIST_ITEM_COLUMN_PRIORITY, toDoListItem.getPriority());
        Long toDoItemDateAndTimeCreatedLongType = toDoListItem.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "itemCreatedDateAndTimeLongType, inserted by DatabaseHelper: " + toDoItemDateAndTimeCreatedLongType);
        String toDoItemDateAndTimeCreated = Long.toString(toDoItemDateAndTimeCreatedLongType);
        contentValues.put(TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED, toDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "itemCreatedDateAndTime, inserted by DatabaseHelper: " + toDoItemDateAndTimeCreated);
        String deadline = Long.toString(toDoListItem.getToDoItemDeadline());
        contentValues.put(TODOLIST_ITEM_COLUMN_DEADLINE, deadline);
        Log.v(LOG_TAG, "deadline, inserted by DatabaseHelper: " + deadline);
        contentValues.put(TODOLIST_ITEM_COLUMN_CATEGORY, toDoListItem.getCategory());
        contentValues.put(TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE, toDoListItem.getCompletionStatus().getStatusCode());
        db.insert(TODOLIST_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertSimpleToDoItem(SimpleToDoItem simpleToDoItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_COLUMN_TITLE, simpleToDoItem.getTitle());
        contentValues.put(TODOLIST_ITEM_COLUMN_DESCRIPTION, "");
        contentValues.put(TODOLIST_ITEM_COLUMN_PRIORITY, 1);
        Long simpleToDoItemDateAndTimeCreatedLongType = simpleToDoItem.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "simpleToDoItemDateAndTimeCreatedLongType, inserted by DatabaseHelper: " + simpleToDoItemDateAndTimeCreatedLongType);
        String simpleToDoItemDateAndTimeCreated = Long.toString(simpleToDoItemDateAndTimeCreatedLongType);
        contentValues.put(TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED, simpleToDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "simpleToDoItemDateAndTimeCreated, inserted by DatabaseHelper: " + simpleToDoItemDateAndTimeCreated);
        contentValues.put(TODOLIST_ITEM_COLUMN_DEADLINE, 0L);
        contentValues.put(TODOLIST_ITEM_COLUMN_CATEGORY, 0);
        contentValues.put(TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE, simpleToDoItem.getCompletionStatus().getStatusCode());
        db.insert(TODOLIST_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateToDoListItem(ToDoItem toDoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_COLUMN_TITLE, toDoItem.getTitle());
        contentValues.put(TODOLIST_ITEM_COLUMN_DESCRIPTION, toDoItem.getDetailDescription());
        contentValues.put(TODOLIST_ITEM_COLUMN_PRIORITY, toDoItem.getPriority());
        Long toDoItemDateAndTimeCreatedLongType = toDoItem.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "itemCreatedDateAndTimeLongType, updated by DatabaseHelper: " + toDoItemDateAndTimeCreatedLongType);
        String toDoItemDateAndTimeCreated = Long.toString(toDoItemDateAndTimeCreatedLongType);
        contentValues.put(TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED, toDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "itemCreatedDateAndTime, updated by DatabaseHelper: " + toDoItemDateAndTimeCreated);
        String deadline = Long.toString(toDoItem.getToDoItemDeadline());
        contentValues.put(TODOLIST_ITEM_COLUMN_DEADLINE, deadline);
        Log.v(LOG_TAG, "deadline, updated by DatabaseHelper: " + deadline);
        contentValues.put(TODOLIST_ITEM_COLUMN_CATEGORY, toDoItem.getCategory());
        contentValues.put(TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE, toDoItem.getCompletionStatus().getStatusCode());
        db.update(TODOLIST_TABLE_NAME, contentValues,
                TODOLIST_ITEM_COLUMN_TITLE + " = ? AND " + TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED + " = ? ",
                new String[]{toDoItem.getTitle(), GeneralHelper.formatToString(toDoItem.getItemCreatedDateAndTime())});
        return true;
    }

    public boolean updateToDoListItem(SimpleToDoItem simpleToDoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_COLUMN_TITLE, simpleToDoItem.getTitle());
        contentValues.put(TODOLIST_ITEM_COLUMN_DESCRIPTION, "");
        contentValues.put(TODOLIST_ITEM_COLUMN_PRIORITY, 1);
        Long simpleToDoItemDateAndTimeCreatedLongType = simpleToDoItem.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "itemCreatedDateAndTimeLongType, updated by DatabaseHelper: " + simpleToDoItemDateAndTimeCreatedLongType);
        String simpleToDoItemDateAndTimeCreated = Long.toString(simpleToDoItemDateAndTimeCreatedLongType);
        contentValues.put(TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED, simpleToDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "itemCreatedDateAndTime, updated by DatabaseHelper: " + simpleToDoItemDateAndTimeCreated);
        contentValues.put(TODOLIST_ITEM_COLUMN_DEADLINE, "");
        contentValues.put(TODOLIST_ITEM_COLUMN_CATEGORY, 0);
        contentValues.put(TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE, simpleToDoItem.getCompletionStatus().getStatusCode());
        db.update(TODOLIST_TABLE_NAME, contentValues,
                TODOLIST_ITEM_COLUMN_TITLE + " = ? AND " + TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED + " = ? ",
                new String[]{simpleToDoItem.getTitle(), GeneralHelper.formatToString(simpleToDoItem.getItemCreatedDateAndTime())});
        return true;
    }

    public boolean deleteToDoItem(final ToDoItem toDoItem) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODOLIST_TABLE_NAME,
                TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED + " = ?",
                new String[]{GeneralHelper.formatToString(toDoItem.getItemCreatedDateAndTime())});
        Toast.makeText(context, "ToDoItem: " + toDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean deleteToDoItem(final SimpleToDoItem simpleToDoItem) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODOLIST_TABLE_NAME,
                TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED + " = ?",
                new String[]{GeneralHelper.formatToString(simpleToDoItem.getItemCreatedDateAndTime())});
        Toast.makeText(context, "ToDoItem: " + simpleToDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
        return true;
    }

    public ArrayList<ToDoItem> getToDoItemsSortedAsArrayList() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingWay
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER,
                DatabaseHelper.TODOLIST_ITEM_COLUMN_PRIORITY);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<ToDoItem> toDoItemsArrayListSorted = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sortByColumnName = null;
        switch (sortingWay) {
            case SORTING_WAY_BY_PRIORITY:
                sortByColumnName = TODOLIST_ITEM_COLUMN_PRIORITY;
                break;
            case SORTING_WAY_BY_TIME_ADDED:
                sortByColumnName = TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED;
                break;
            case SORTING_WAY_BY_TITLE:
                sortByColumnName = TODOLIST_ITEM_COLUMN_TITLE;
                break;
            default:
                sortByColumnName = TODOLIST_ITEM_COLUMN_DEADLINE;
                break;
        }

        String sortingASECorDESC = null;
        switch (sortingAscOrDesc) {
            case SORTING_STANDARD_ASC:
                sortingASECorDESC = SORTING_ASC;
                break;
            default:
                sortingASECorDESC = SORTING_DESC;
                break;
        }

        String getAllToDoItemsSortedQuery = "SELECT * FROM " + TODOLIST_TABLE_NAME +
                " ORDER BY " + sortByColumnName + sortingASECorDESC;
        Log.v(LOG_TAG, "getAllToDoItemsSortedQuery: " + getAllToDoItemsSortedQuery);
        Cursor cursor = db.rawQuery(getAllToDoItemsSortedQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE));
            ToDoItem toDoListItem = new ToDoItem(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            toDoItemsArrayListSorted.add(toDoListItem);
            cursor.moveToNext();
        }

        String hint = "ArrayList is sorted by " + sortingWay + ", " + sortingAscOrDesc + ": ";
        GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(toDoItemsArrayListSorted, hint);

        return toDoItemsArrayListSorted;
    }

    public ArrayList<ToDoItem> getSortedIncompleteToDoItemsAsArrayList() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingWay
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER,
                DatabaseHelper.TODOLIST_ITEM_COLUMN_PRIORITY);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<ToDoItem> toDoItemsArrayList = new ArrayList<>();

        String sortByColumnName = null;
        switch (sortingWay) {
            case SORTING_WAY_BY_DEADLINE:
                sortByColumnName = TODOLIST_ITEM_COLUMN_DEADLINE;
                break;
            case SORTING_WAY_BY_TIME_ADDED:
                sortByColumnName = TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED;
                break;
            case SORTING_WAY_BY_TITLE:
                sortByColumnName = TODOLIST_ITEM_COLUMN_TITLE;
                break;
            default:
                sortByColumnName = TODOLIST_ITEM_COLUMN_PRIORITY;
                break;
        }

        String sortingASCorDESC = null;
        switch (sortingAscOrDesc) {
            case SORTING_STANDARD_ASC:
                sortingASCorDESC = SORTING_ASC;
                break;
            default:
                sortingASCorDESC = SORTING_DESC;
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery
                = "SELECT * FROM " + TODOLIST_TABLE_NAME +
                " WHERE " + TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE + " = " + CompletionStatus.INCOMPLETE.getStatusCode() +
                " AND " + TODOLIST_ITEM_COLUMN_DEADLINE + " > 0" +
                " ORDER BY " + sortByColumnName + sortingASCorDESC;
        Log.v(LOG_TAG, "getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery: " +
                getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery);
        Cursor cursor = db.rawQuery(getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery, null);
        if (cursor.getCount() == 0)
            Log.v(LOG_TAG, "getSortedIncompleteToDoItemsAsArrayList(), cursor is empty.");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE));
            ToDoItem toDoListItem = new ToDoItem(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            toDoItemsArrayList.add(toDoListItem);
            cursor.moveToNext();
        }

        GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(toDoItemsArrayList, "getSortedIncompleteToDoItemsAsArrayList(), DatabaseHelper");

        return toDoItemsArrayList;
    }

    public ArrayList<ToDoItem> getSortedCompletedToDoItemsAsArrayList() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingWay
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER,
                DatabaseHelper.TODOLIST_ITEM_COLUMN_PRIORITY);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<ToDoItem> toDoItemsArrayList = new ArrayList<>();

        String sortByColumnName = null;
        switch (sortingWay) {
            case SORTING_WAY_BY_DEADLINE:
                sortByColumnName = TODOLIST_ITEM_COLUMN_DEADLINE;
                break;
            case SORTING_WAY_BY_TIME_ADDED:
                sortByColumnName = TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED;
                break;
            case SORTING_WAY_BY_TITLE:
                sortByColumnName = TODOLIST_ITEM_COLUMN_TITLE;
                break;
            default:
                sortByColumnName = TODOLIST_ITEM_COLUMN_PRIORITY;
                break;
        }

        String sortingASCorDESC = null;
        switch (sortingAscOrDesc) {
            case SORTING_STANDARD_ASC:
                sortingASCorDESC = SORTING_ASC;
                break;
            default:
                sortingASCorDESC = SORTING_DESC;
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery
                = "SELECT * FROM " + TODOLIST_TABLE_NAME +
                " WHERE " + TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE + " = " + CompletionStatus.COMPLETED.getStatusCode() +
                " ORDER BY " + sortByColumnName + sortingASCorDESC;
        Log.v(LOG_TAG, "getSortedCompletedToDoItemsAsArrayList: " +
                getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery);
        Cursor cursor = db.rawQuery(getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery, null);
        if (cursor.getCount() == 0)
            Log.v(LOG_TAG, "getSortedCompletedToDoItemsAsArrayList(), cursor is empty.");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_COMPLETION_STATUS_CODE));
            ToDoItem toDoListItem = new ToDoItem(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            toDoItemsArrayList.add(toDoListItem);
            cursor.moveToNext();
        }

        GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(toDoItemsArrayList, "getSortedIncompleteToDoItemsAsArrayList(), DatabaseHelper");

        return toDoItemsArrayList;
    }

    public ArrayList<SimpleToDoItem> getSortedSimpleToDoItemsAsArrayList() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingWay
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER,
                DatabaseHelper.TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<SimpleToDoItem> simpleToDoItemsArrayList = new ArrayList<>();

        String sortByColumnName = null;
        switch (sortingWay) {
            case SORTING_WAY_BY_PRIORITY:
                sortByColumnName = TODOLIST_ITEM_COLUMN_PRIORITY;
                break;
            case SORTING_WAY_BY_DEADLINE:
                sortByColumnName = TODOLIST_ITEM_COLUMN_DEADLINE;
                break;
            case SORTING_WAY_BY_TITLE:
                sortByColumnName = TODOLIST_ITEM_COLUMN_TITLE;
                break;
            default:
                sortByColumnName = TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED;
                break;
        }

        String sortingASCorDESC = null;
        switch (sortingAscOrDesc) {
            case SORTING_STANDARD_ASC:
                sortingASCorDESC = SORTING_ASC;
                break;
            default:
                sortingASCorDESC = SORTING_DESC;
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery
                = "SELECT * FROM " + TODOLIST_TABLE_NAME +
                " WHERE " + TODOLIST_ITEM_COLUMN_DEADLINE + " = " + "0" +
                " ORDER BY " + sortByColumnName + sortingASCorDESC;
        Log.v(LOG_TAG, "getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery: " +
                getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery);
        Cursor cursor = db.rawQuery(getSortedToDoItemsInDifferentCompletionStatusAsArrayListQuery, null);
        if (cursor.getCount() == 0)
            Log.v(LOG_TAG, "getSortedIncompleteToDoItemsAsArrayList(), cursor is empty.");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_TITLE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_COLUMN_ITEM_TIME_DATE_CREATED));
            SimpleToDoItem simpleToDoListItem = new SimpleToDoItem(title, itemDateAndTimeCreated);
            simpleToDoItemsArrayList.add(simpleToDoListItem);
            cursor.moveToNext();
        }

        return simpleToDoItemsArrayList;
    }
}
