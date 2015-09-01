package com.example.tek.first.servant.todolist.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Time implements Parcelable {

    private int hour;
    private int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    protected Time(Parcel in) {
        hour = in.readInt();
        minute = in.readInt();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(hour);
        dest.writeInt(minute);
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }

    public String formatTimeToString() {
        return String.format("%02d", hour) + String.format("%02d", minute);
    }

    public Long formatToLong() {
        return Long.parseLong(formatTimeToString());
    }
}
