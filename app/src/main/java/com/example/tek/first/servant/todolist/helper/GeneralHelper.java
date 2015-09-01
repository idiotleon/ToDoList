package com.example.tek.first.servant.todolist.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.tek.first.servant.todolist.model.DateModel;
import com.example.tek.first.servant.todolist.model.TimeModel;
import com.example.tek.first.servant.todolist.model.ToDoItemModel;

import java.util.ArrayList;

public class GeneralHelper {
    private static String LOG_TAG = GeneralHelper.class.getSimpleName();

    public static Long dateAndTimeFormattedToLong(DateModel date, TimeModel time) {
        if (date != null && time != null)
            return Long.parseLong(date.formatDateToString() + time.formatTimeToString());
        else {
            Log.e(LOG_TAG, "Date or time might be null");
            return 0L;
        }
    }

    /**
     * 0: not started
     * 1: incompleted
     * 2: completed
     */
    public enum CompletionStatus implements Parcelable {
        NOTSTARTED(0),
        INCOMPLETED(1),
        COMPLETED(2);

        private int statusCode;

        CompletionStatus(int statusCode) {
            this.statusCode = statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return this.statusCode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ordinal());
            dest.writeInt(statusCode);
        }

        // todo: what is this method used for? Possible explanation might be found at Parcelable class
        public static final Parcelable.Creator<CompletionStatus> CREATOR = new Parcelable.Creator<CompletionStatus>() {

            public CompletionStatus createFromParcel(Parcel in) {
                CompletionStatus completionStatus = CompletionStatus.values()[in.readInt()];
                completionStatus.setStatusCode(in.readInt());
                return completionStatus;
            }

            public CompletionStatus[] newArray(int size) {
                return new CompletionStatus[size];
            }
        };
    }

    public static CompletionStatus completionStatusCodeToCompletationStatus(int completionStatusCode) {
        switch (completionStatusCode) {
            case 1:
                return CompletionStatus.INCOMPLETED;
            case 2:
                return CompletionStatus.COMPLETED;
        }
        return CompletionStatus.NOTSTARTED;
    }

    public static String formatToString(String text) {
        if (text == null || text.length() == 0) {
            return "";
        } else
            return text;
    }

    public static String formatToString(Long text) {
        return Long.toString(text);
    }

    public static String formatToString(int text) {
        return Integer.toString(text);
    }

    public static String parseDateAndTimeToString(Long dateAndTime) {
        String dateAndTimeString = formatToString(dateAndTime);
        String year = dateAndTimeString.substring(0, 4);
        String monthInString = dateAndTimeString.substring(4, 6);
        String day = dateAndTimeString.substring(6, 8);
        String hourOfDay = dateAndTimeString.substring(8, 10);
        String minuteOfHour = dateAndTimeString.substring(10, 12);
        int monthInInt = Integer.parseInt(monthInString);
        String[] monthOptions = {
                "Jan ", "Feb ", "Mar ", "Apr ",
                "May ", "Jun ", "Jul ", "Aug ",
                "Sep ", "Oct ", "Nov ", "Dec "};
        String dateAndTimeFormatted = hourOfDay + ":" + minuteOfHour + ", "
                + monthOptions[monthInInt - 1] + day + ", " + year;
        Log.v(LOG_TAG, "dateAndTimeFormatted, GeneralHelper: " + dateAndTimeFormatted);
        return dateAndTimeFormatted;
    }

    public static String parseDateAndTimeToString(String dateAndTimeString) {
        String year = dateAndTimeString.substring(0, 4);
        String monthInString = dateAndTimeString.substring(4, 6);
        String day = dateAndTimeString.substring(6, 8);
        String hourOfDay = dateAndTimeString.substring(8, 10);
        String minuteOfHour = dateAndTimeString.substring(10, 12);
        int monthInInt = Integer.parseInt(monthInString);
        String[] monthOptions = {
                "Jan ", "Feb ", "Mar ", "Apr ",
                "May ", "Jun ", "Jul ", "Aug ",
                "Sep ", "Oct ", "Nov ", "Dec "};
        String dateAndTimeFormatted = hourOfDay + ":" + minuteOfHour + ", "
                + monthOptions[monthInInt - 1] + day + ", " + year;
        Log.v(LOG_TAG, "dateAndTimeFormatted, GeneralHelper: " + dateAndTimeFormatted);
        return dateAndTimeFormatted;
    }

    public static void displayTitleOfAllToDoItemsInAnArrayList(ArrayList<ToDoItemModel> toDoItemsArrayList, String hint) {
        if (toDoItemsArrayList.isEmpty()) {
            Log.v(LOG_TAG, hint + ": is an empty ArrayList.");
        } else {
            for (int i = 0; i < toDoItemsArrayList.size(); i++) {
                Log.v(LOG_TAG, hint + ": " + toDoItemsArrayList.get(i).getTitle());
            }
        }
    }

    /**
     * Helper method to decide whether on which the app is runned is a tablet.
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration()
                .screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void sortStandard(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(key, 1).commit();
    }
}
