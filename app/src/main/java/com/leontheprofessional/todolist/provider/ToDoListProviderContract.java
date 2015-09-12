package com.leontheprofessional.todolist.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Leon on 9/11/2015.
 */
public class ToDoListProviderContract {

    public static final String CONTENT_AUTHORITY
            = "com.leontheprofessional.todolist.ToDoListProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SIMPLE_TODOITEM = "simple_todo_item";
    public static final String PATH_DETAILED_TODOITEM = "detail_todo_item";

    public static final class SimpleToDoItemEntry implements BaseColumns {
        public static String TABLE_NAME = "simple_todolist_table";
        public static String SIMPLE_TODO_ITEM_COLUMN_ID = "simple_todolist_id";
        public static String SIMPLE_TODO_ITEM_COLUMN_TITLE = "simple_todolist_title";
        public static String SIMPLE_TODO_ITEM_COLUMN_PRIORITY = "simple_todo_item_priority";
        public static String SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE = "simple_todo_item_created_time_and_date";
        public static String SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE = "simple_todo_item_completion_status_code";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SIMPLE_TODOITEM).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_SIMPLE_TODOITEM;
        public static final String CONTNET_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_SIMPLE_TODOITEM;
    }

    public static final class DetailedToDoItemEntry implements BaseColumns {
        public static String TABLE_NAME = "detailed_todolist_table";
        public static String DETAILED_TODO_ITEM_COLUMN_ID = "detailed_todo_item_id";
        public static String DETAILED_TODO_COLUMN_TITLE = "detailed_todo_item_title";
        public static String DETAILED_TODO_COLUMN_DESCRIPTION = "detailed_todo_tem_description";
        public static String DETAILED_TODO_COLUMN_PRIORITY = "detailed_todo_item_priority";
        public static String DETAILED_TODO_COLUMN_DEADLINE = "detailed_todo_item_deadline";
        public static String DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE = "detailed_todo_item_created_time_and_date";
        public static String DETAILED_TODO_COLUMN_CATEGORY = "detailed_todo_item_category";
        public static String DETAILED_TODO_COLUMN_PRICE = "detailed_todo_item_price";
        public static String DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE = "detailed_todo_item_completion_status_code";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DETAILED_TODOITEM).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_DETAILED_TODOITEM;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_DETAILED_TODOITEM;
    }

}
