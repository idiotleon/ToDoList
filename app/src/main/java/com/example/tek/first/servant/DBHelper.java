package com.example.tek.first.servant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stan on 8/18/15.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "servant.db";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create a table to hold user info.
//        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
//                + UserContract.UserEntry.COLUMN_USER_ID + " INTEGER PRIMARY KEY,"
//                + UserContract.UserEntry.COLUMN_USER_USERNAME + " TEXT UNIQUE NOT NULL, "
//                + UserContract.UserEntry.COLUMN_USER_PASSWORD + " TEXT NOT NULL, "
//                + UserContract.UserEntry.COLUMN_USER_AGE + " INTEGER, "
//                + UserContract.UserEntry.COLUMN_USER_COUNTRY + " TEXT, "
//                + UserContract.UserEntry.COLUMN_USER_STREET + " TEXT);";

//        db.execSQL( SQL_CREATE_USER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL( "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }
}
