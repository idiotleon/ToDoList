package com.example.tek.first.servant.todolist.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tek.first.servant.todolist.helper.GeneralHelper;

public class SimpleToDoItem implements Parcelable {

    private static final String LOG_TAG = SimpleToDoItem.class.getSimpleName();

    private String title;
    private int priority;
    private Long itemCreatedDateAndTime;
    private GeneralHelper.CompletionStatus completionStatus;

    public SimpleToDoItem(String title, Long itemCreatedDateAndTime) {
        this.title = title;
        priority = 1;
        this.itemCreatedDateAndTime = itemCreatedDateAndTime;
        completionStatus = GeneralHelper.CompletionStatus.INCOMPLETE;
    }

    public void setCompletionStatus(GeneralHelper.CompletionStatus completionStatus) {
        this.completionStatus = completionStatus;
    }

    public String getTitle() {
        return title;
    }

    public Long getItemCreatedDateAndTime() {
        return itemCreatedDateAndTime;
    }

    public GeneralHelper.CompletionStatus getCompletionStatus() {
        return completionStatus;
    }

    protected SimpleToDoItem(Parcel in) {
        title = in.readString();
        priority = in.readInt();
        itemCreatedDateAndTime = in.readLong();
        completionStatus = (GeneralHelper.CompletionStatus) in.readValue(GeneralHelper.CompletionStatus.class.getClassLoader());
    }

    public static final Creator<SimpleToDoItem> CREATOR = new Creator<SimpleToDoItem>() {
        @Override
        public SimpleToDoItem createFromParcel(Parcel in) {
            return new SimpleToDoItem(in);
        }

        @Override
        public SimpleToDoItem[] newArray(int size) {
            return new SimpleToDoItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(priority);
        dest.writeLong(itemCreatedDateAndTime);
        dest.writeValue(completionStatus);
    }
}
