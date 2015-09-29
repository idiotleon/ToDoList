package com.example.tek.first.servant.user;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by stan on 8/18/15.
 */
public class UserContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.Assignment5.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";


    public static final class UserEntry implements BaseColumns {

        public static final String TABLE_NAME = "user";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_USERNAME = "username";
        public static final String COLUMN_USER_PASSWORD = "password";
        public static final String COLUMN_USER_AGE = "age";
        public static final String COLUMN_USER_STREET = "street";
        public static final String COLUMN_USER_COUNTRY = "country";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static String getUsernameFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
