package com.example.tek.first.servant.todolist.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tek.first.servant.todolist.model.ToDoItemModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = DatabaseHelper.class.getSimpleName();

    public static String TODOLIST_DATABASE_NAME = "todolist_database";
    public static String TODOLIST_TABLE_NAME = "todolist_table";

    public static String TODOLIST_ITEM_ID = "todolist_id";
    public static String TODOLIST_ITEM_TITLE = "todolist_title";
    public static String TODOLIST_ITEM_DESCRIPTION = "todolist_description";
    public static String TODOLIST_ITEM_PRIORITY = "todolist_priority";
    public static String TODOLIST_ITEM_DEADLINE = "todolist_time_date_set";
    public static String TODOLIST_ITEM_TIME_DATE_CREATED = "todolist_time_date_created";
    public static String TODOLIST_ITEM_CATEGORY = "todolist_category";
    // todo: "todolist_complete_status_code"
    public static String TODOLIST_ITEM_COMPLETE_STATUS = "todolist_complete_status";

    public static int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, TODOLIST_DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TODOLIST_TABLE_NAME + " (" +
                TODOLIST_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TODOLIST_ITEM_TITLE + " TEXT NOT NULL, " +
                TODOLIST_ITEM_DESCRIPTION + " TEXT, " +
                TODOLIST_ITEM_PRIORITY + " INTEGER DEFAULT 1, " +
                TODOLIST_ITEM_DEADLINE + " TEXT, " +
                TODOLIST_ITEM_TIME_DATE_CREATED + " TEXT, " +
                TODOLIST_ITEM_CATEGORY + " INTEGER DEFAULT 0, " +
                TODOLIST_ITEM_COMPLETE_STATUS + " INTEGER DEFAULT 1)";
        Log.v(LOG_TAG, "createQuery: " + createTableQuery);

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgradeTableQuery = "DROP TABLE IF EXISTS " + TODOLIST_TABLE_NAME;
        Log.v(LOG_TAG, "Upgrade database from " + oldVersion + " to " + newVersion);
        Log.v(LOG_TAG, "upgradeTableQuery: " + upgradeTableQuery);
        db.execSQL(upgradeTableQuery);
    }

    public boolean insertToDoListItem(ToDoItemModel toDoListItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_TITLE, toDoListItem.getTitle());
        contentValues.put(TODOLIST_ITEM_DESCRIPTION, toDoListItem.getDetailDescription());
        contentValues.put(TODOLIST_ITEM_PRIORITY, toDoListItem.getPriority());
        Long toDoItemDateAndTimeCreatedLongType = toDoListItem.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "itemCreatedDateAndTimeLongType, inserted by DatabaseHelper: " + toDoItemDateAndTimeCreatedLongType);
        String toDoItemDateAndTimeCreated = Long.toString(toDoItemDateAndTimeCreatedLongType);
        contentValues.put(TODOLIST_ITEM_TIME_DATE_CREATED, toDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "itemCreatedDateAndTime, inserted by DatabaseHelper: " + toDoItemDateAndTimeCreated);
        String deadline = Long.toString(toDoListItem.getToDoItemDeadline());
        contentValues.put(TODOLIST_ITEM_DEADLINE, deadline);
        Log.v(LOG_TAG, "deadline, inserted by DatabaseHelper: " + deadline);
        contentValues.put(TODOLIST_ITEM_CATEGORY, toDoListItem.getCategory());
        contentValues.put(TODOLIST_ITEM_COMPLETE_STATUS, toDoListItem.getCompleteStatusCode());
        db.insert(TODOLIST_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateToDoListItem(ToDoItemModel toDoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_TITLE, toDoItem.getTitle());
        contentValues.put(TODOLIST_ITEM_DESCRIPTION, toDoItem.getDetailDescription());
        contentValues.put(TODOLIST_ITEM_PRIORITY, toDoItem.getPriority());
        Long toDoItemDateAndTimeCreatedLongType = toDoItem.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "itemCreatedDateAndTimeLongType, updated by DatabaseHelper: " + toDoItemDateAndTimeCreatedLongType);
        String toDoItemDateAndTimeCreated = Long.toString(toDoItemDateAndTimeCreatedLongType);
        contentValues.put(TODOLIST_ITEM_TIME_DATE_CREATED, toDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "itemCreatedDateAndTime, updated by DatabaseHelper: " + toDoItemDateAndTimeCreated);
        String deadline = Long.toString(toDoItem.getToDoItemDeadline());
        contentValues.put(TODOLIST_ITEM_DEADLINE, deadline);
        Log.v(LOG_TAG, "deadline, updated by DatabaseHelper: " + deadline);
        contentValues.put(TODOLIST_ITEM_CATEGORY, toDoItem.getCategory());
        contentValues.put(TODOLIST_ITEM_COMPLETE_STATUS, toDoItem.getCompleteStatusCode());
        db.update(TODOLIST_TABLE_NAME, contentValues,
                TODOLIST_ITEM_TITLE + " = ? AND " + TODOLIST_ITEM_TIME_DATE_CREATED + " = ? ",
                new String[]{toDoItem.getTitle(), GeneralHelper.formatToString(toDoItem.getItemCreatedDateAndTime())});
        return true;
    }

//    public boolean insertToDoListItemSetDataAndTime(String title, )

    public ArrayList<ToDoItemModel> getTitlePriorityDeadlineOfAllToDoItemsAsArrayList() {
        ArrayList<ToDoItemModel> toDoListItemsTitlePriorityDeadlineArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getAllQuery = "SELECT * FROM " + TODOLIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(getAllQuery, null);
        // todo: test whether the 1st item was included
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, deadline);
            toDoListItemsTitlePriorityDeadlineArrayList.add(toDoListItem);
            cursor.moveToNext();
        }
        return toDoListItemsTitlePriorityDeadlineArrayList;
    }

    public ArrayList<ToDoItemModel> getAllToDoItemsAsArrayList() {
        Log.v(LOG_TAG, "getAllToDoItemsAsArrayList(), dbHelper executed.");
        ArrayList<ToDoItemModel> toDoListItemsTitlePriorityDeadlineArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getAllQuery = "SELECT * FROM " + TODOLIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(getAllQuery, null);
        // todo: test whether the 1st item was included
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            String description = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DESCRIPTION));
            long itemCreatedDateAndTime = Long.parseLong(cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TIME_DATE_CREATED)));
            Log.v(LOG_TAG, "itemCreatedDateAndTime, fetched the DatabaseHelper: " + itemCreatedDateAndTime);
            long deadline = Long.parseLong(cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE)));
            Log.v(LOG_TAG, "deadline, fetched the DatabaseHelper: " + deadline);
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COMPLETE_STATUS));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, description, itemCreatedDateAndTime, deadline, category, completionStatusCode);
            toDoListItemsTitlePriorityDeadlineArrayList.add(0, toDoListItem);
            cursor.moveToNext();
        }
        return toDoListItemsTitlePriorityDeadlineArrayList;
    }

    /**
     * Returns total rows of the table
     *
     * @return
     */
    public int numberOfTotalToDoItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TODOLIST_TABLE_NAME);
        return numRows;
    }

    /**
     * returns total rows of incomplete todolist items
     *
     * @return
     */
    // todo
//    public int numberOfNotStartedToDoItems(){
//        SQLiteDatabase db = this.getReadableDatabase();
//    }
//
//    public int numberOfIncompleteToDoItems() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//    }
//
//    public int numberOfCompletedToDoItem() {
//        SQLiteDatabase db = this.getReadableDatabase();
//    }
    public boolean deleteToDoItem(ToDoItemModel toDoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODOLIST_TABLE_NAME,
                TODOLIST_ITEM_TIME_DATE_CREATED + " =? AND " +
                        TODOLIST_ITEM_TITLE + " =?",
                new String[]{GeneralHelper.formatToString(toDoItem.getItemCreatedDateAndTime()), toDoItem.getTitle()});
        return true;
    }

    public ArrayList<ToDoItemModel> toDoItemsArrayListSortByDeadline() {
        ArrayList<ToDoItemModel> toDoItemsArrayListSortByDeadline = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getAllToDoItemsOrderByDeadlineQuery = "SELECT * FROM " + TODOLIST_TABLE_NAME + " ORDER BY " + TODOLIST_ITEM_DEADLINE;
        Log.v(LOG_TAG, "getAllToDoItemsOrderByDeadlineQuery: " + getAllToDoItemsOrderByDeadlineQuery);
        Cursor cursor = db.rawQuery(getAllToDoItemsOrderByDeadlineQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COMPLETE_STATUS));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            toDoItemsArrayListSortByDeadline.add(toDoListItem);
            cursor.moveToNext();
        }

        return toDoItemsArrayListSortByDeadline;
    }

    public ArrayList<ToDoItemModel> toDoItemsArrayListSortByTitle() {
        ArrayList<ToDoItemModel> toDoItemsArrayListSortByTitle = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getAllToDoItemsOrderByTitleQuery = "SELECT * FROM " + TODOLIST_TABLE_NAME + " ORDER BY " + TODOLIST_ITEM_TITLE;
        Log.v(LOG_TAG, "getAllToDoItemsOrderByTitleQuery: " + getAllToDoItemsOrderByTitleQuery);
        Cursor cursor = db.rawQuery(getAllToDoItemsOrderByTitleQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COMPLETE_STATUS));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            toDoItemsArrayListSortByTitle.add(toDoListItem);
            cursor.moveToNext();
        }

        for (int i = 0; i < toDoItemsArrayListSortByTitle.size(); i++) {
            Log.v(LOG_TAG, " toDoItemsArrayListSortByTitle(), dbHelper executed: " + toDoItemsArrayListSortByTitle.get(i).getTitle());
        }

        return toDoItemsArrayListSortByTitle;
    }

    public ArrayList<ToDoItemModel> toDoItemsArrayListSortByTimeAdded() {
        ArrayList<ToDoItemModel> toDoItemsArrayListSortByTimeAdded = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getAllToDoItemsOrderByTimeAddedQuery = "SELECT * FROM " + TODOLIST_TABLE_NAME + " ORDER BY " + TODOLIST_ITEM_TIME_DATE_CREATED;
        Log.v(LOG_TAG, "getAllToDoItemsOrderByTimeAddedQuery: " + getAllToDoItemsOrderByTimeAddedQuery);
        Cursor cursor = db.rawQuery(getAllToDoItemsOrderByTimeAddedQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COMPLETE_STATUS));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            toDoItemsArrayListSortByTimeAdded.add(toDoListItem);
            cursor.moveToNext();
        }

        return toDoItemsArrayListSortByTimeAdded;
    }

    public ArrayList<ToDoItemModel> toDoItemsArrayListSortByPriority() {
        ArrayList<ToDoItemModel> toDoItemsArrayListSortByPriority = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getAllToDoItemsOrderByPriorityQuery = "SELECT * FROM " + TODOLIST_TABLE_NAME + " ORDER BY " + TODOLIST_ITEM_PRIORITY;
        Log.v(LOG_TAG, "getAllToDoItemsOrderByPriorityQuery: " + getAllToDoItemsOrderByPriorityQuery);
        Cursor cursor = db.rawQuery(getAllToDoItemsOrderByPriorityQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COMPLETE_STATUS));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            toDoItemsArrayListSortByPriority.add(toDoListItem);
            cursor.moveToNext();
        }

        for (int i = 0; i < toDoItemsArrayListSortByPriority.size(); i++) {
            Log.v(LOG_TAG, "getAllToDoItemsOrderByPriorityQuery: " + toDoItemsArrayListSortByPriority.get(i).getTitle());
        }

        return toDoItemsArrayListSortByPriority;
    }

    public ArrayList<ToDoItemModel> incompleteToDoItemsArrayList() {

        ArrayList<ToDoItemModel> incompleteToDoItemsArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getIncompleteToDoItemsOrderByDeadline = "SELECT * FROM " + TODOLIST_TABLE_NAME +
                " WHERE " + TODOLIST_ITEM_COMPLETE_STATUS + " = " + GeneralConstants.TODOLISTITEM_STATUS_INCOMPLETE +
                " ORDER BY " + TODOLIST_ITEM_DEADLINE;
        Log.v(LOG_TAG, "getIncompleteToDoItemsOrderByDeadline: " + getIncompleteToDoItemsOrderByDeadline);
        Cursor cursor = db.rawQuery(getIncompleteToDoItemsOrderByDeadline, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COMPLETE_STATUS));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            incompleteToDoItemsArrayList.add(toDoListItem);
            cursor.moveToNext();
        }

        return incompleteToDoItemsArrayList;

    }

    public ArrayList<ToDoItemModel> completeToDoItemsArrayList() {

        ArrayList<ToDoItemModel> completeToDoItemsArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getCompleteToDoItemsOrderByDeadline = "SELECT * FROM " + TODOLIST_TABLE_NAME +
                " WHERE " + TODOLIST_ITEM_COMPLETE_STATUS + " = " + GeneralConstants.TODOLISTITEM_STATUS_COMPLETE +
                " ORDER BY " + TODOLIST_ITEM_DEADLINE;
        Log.v(LOG_TAG, "getCompleteToDoItemsOrderByDeadline: " + getCompleteToDoItemsOrderByDeadline);
        Cursor cursor = db.rawQuery(getCompleteToDoItemsOrderByDeadline, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COMPLETE_STATUS));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            completeToDoItemsArrayList.add(toDoListItem);
            cursor.moveToNext();
        }

        return completeToDoItemsArrayList;

    }

    public ArrayList<ToDoItemModel> notStartedToDoItemsArrayList() {

        ArrayList<ToDoItemModel> notStartedToDoItemsArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String getNotStartedToDoItemsOrderByDeadline = "SELECT * FROM " + TODOLIST_TABLE_NAME +
                " WHERE " + TODOLIST_ITEM_COMPLETE_STATUS + " = " + GeneralConstants.TODOLISTITEM_STATUS_NOT_STARTED +
                " ORDER BY " + TODOLIST_ITEM_DEADLINE;
        Log.v(LOG_TAG, "getNotStartedToDoItemsOrderByDeadline: " + getNotStartedToDoItemsOrderByDeadline);
        Cursor cursor = db.rawQuery(getNotStartedToDoItemsOrderByDeadline, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_TITLE));
            int priority = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_PRIORITY));
            long deadline = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_DEADLINE));
            long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(TODOLIST_ITEM_TIME_DATE_CREATED));
            String detailDescription = cursor.getString(cursor.getColumnIndex(TODOLIST_ITEM_DESCRIPTION));
            int category = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_CATEGORY));
            int completionStatusCode = cursor.getInt(cursor.getColumnIndex(TODOLIST_ITEM_COMPLETE_STATUS));
            ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, detailDescription,
                    itemDateAndTimeCreated, deadline, category, completionStatusCode);
            notStartedToDoItemsArrayList.add(toDoListItem);
            cursor.moveToNext();
        }

        /**
         * For test purpose
         */
        for (int i = 0; i < notStartedToDoItemsArrayList.size(); i++) {
            Log.v(LOG_TAG, "getNotStartedToDoItemsOrderByDeadline: " + notStartedToDoItemsArrayList.get(i).getTitle());
        }

        return notStartedToDoItemsArrayList;
    }
}
