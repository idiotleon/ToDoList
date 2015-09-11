package com.leontheprofessional.todolist.helper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.leontheprofessional.todolist.model.Date;
import com.leontheprofessional.todolist.model.Time;
import com.leontheprofessional.todolist.model.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GeneralHelper {
    private static String LOG_TAG = GeneralHelper.class.getSimpleName();

    public static Long dateAndTimeFormattedToLong(Date date, Time time) {
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
        INCOMPLETE(1),
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

    public static int getCurrentTimeAndDate(int component) {

        String currentTimeAndDate = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

        int year = Integer.parseInt(currentTimeAndDate.substring(0, 4));
        int monthOfYear = Integer.parseInt(currentTimeAndDate.substring(4, 6));
        int dayOfMonth = Integer.parseInt(currentTimeAndDate.substring(6, 8));
        int hourOfDay = Integer.parseInt(currentTimeAndDate.substring(8, 10));
        int minute = Integer.parseInt(currentTimeAndDate.substring(10, 12));
        int second = Integer.parseInt(currentTimeAndDate.substring(12, 14));

        switch (component) {
            case 1:
                return year;
            case 2:
                return monthOfYear;
            case 3:
                return dayOfMonth;
            case 4:
                return hourOfDay;
            case 5:
                return minute;
            case 6:
                return second;
            default:
                return 0;
        }
    }

    public static CompletionStatus completionStatusCodeToCompletationStatus(int completionStatusCode) {
        switch (completionStatusCode) {
            case 2:
                return CompletionStatus.COMPLETED;
        }
        return CompletionStatus.INCOMPLETE;
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
        if (dateAndTime != 0L && dateAndTime != 1L) {
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
        } else if (dateAndTime == 1L) {
            return "";
        } else {
            return "0";
        }
    }

    public static Long parseDateAndTimeModelToLong(Date date, Time time) {
        if (time == null) {
            /**
             * For purpose of differentiate
             * a simple new todoItem (deadline = 0L)
             * and a detailed todoItem without a deadline (deadline = 1L)
             */
            return 1L;
        } else {
            String dateInStringType;
            String timeInStringType;
            String dateAndTimeInStringType;
            if (date == null) {
                int year = getCurrentTimeAndDate(GeneralConstants.YEAR_OPTION);
                int monthOfYear = getCurrentTimeAndDate(GeneralConstants.MONTH_OF_YEAR_OPTION);
                int dayOfMonth = getCurrentTimeAndDate(GeneralConstants.DAY_OF_MONTH_OPTION);
                dateInStringType = Integer.toString(year) + String.format("%02d", monthOfYear) + String.format("%02d", dayOfMonth);
            } else {
                dateInStringType = date.formatDateToString();
            }
            timeInStringType = time.formatTimeToString();
            dateAndTimeInStringType = dateInStringType + timeInStringType;
            return Long.parseLong(dateAndTimeInStringType);
        }
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

    public static void displayTitleOfAllToDoItemsInAnArrayList(ArrayList<ToDoItem> toDoItemsArrayList, String hint) {
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

    public static void hideFragment(Activity activity, Fragment fragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(fragment)
                .commit();
    }

    public static void showFragment(Activity activity, Fragment fragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(fragment)
                .commit();
    }

    public interface ToDoItemStatusChangeListener {
        void onStatusChanged();
    }

    public static void hideSoftKeyBoard(Context context, View view){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
