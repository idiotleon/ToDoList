package com.leontheprofessional.todolist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.leontheprofessional.todolist.helper.GeneralHelper;
import com.leontheprofessional.todolist.helper.GeneralHelper.CompletionStatus;

public class ToDoItem implements Parcelable {

    private static final String LOG_TAG = ToDoItem.class.getSimpleName();

    private String title;
    private int priority;
    private String detailDescription;
    private Long itemCreatedDateAndTime;
    private Long toDoItemDeadline;
    private int category;
    private CompletionStatus completionStatus;
    private double price;

    public ToDoItem() {
        this.title = null;
        this.priority = 1;
        this.detailDescription = "";
        this.itemCreatedDateAndTime = 0L;
        this.toDoItemDeadline = 0L;
        this.category = 0;
        this.completionStatus = CompletionStatus.INCOMPLETE;
        this.price = 0;
    }

    public ToDoItem(SimpleToDoItem simpleToDoItem) {
        this.title = simpleToDoItem.getTitle();
        this.completionStatus = simpleToDoItem.getCompletionStatus();
        this.itemCreatedDateAndTime = simpleToDoItem.getItemCreatedDateAndTime();
        this.priority = 1;
        this.toDoItemDeadline = 0L;
        this.detailDescription = "";
        this.category = 0;
        this.price = 0.0;
    }

    public ToDoItem(String title) {
        this.title = title;
        this.priority = 1;
        this.detailDescription = "";
        this.itemCreatedDateAndTime = 0L;
        this.toDoItemDeadline = 0L;
        this.category = 0;
        this.completionStatus = CompletionStatus.INCOMPLETE;
        this.price = 0;
    }

    public ToDoItem(String title, Long currentTime) {
        this.title = title;
        this.priority = 1;
        this.detailDescription = "";
        this.itemCreatedDateAndTime = currentTime;
        this.toDoItemDeadline = 0L;
        this.category = 0;
        this.completionStatus = CompletionStatus.INCOMPLETE;
        this.price = 0;
    }

    public ToDoItem(String title, int priority, long toDoItemDeadline) {
        this.title = title;
        this.priority = priority;
        this.toDoItemDeadline = toDoItemDeadline;
        this.price = 0;
    }

    public ToDoItem(String title, int priority, String detailDescription, Long itemCreatedDateAndTime, Long toDoItemDeadline, int category) {
        this.title = title;
        this.priority = priority;
        this.detailDescription = detailDescription;
        this.itemCreatedDateAndTime = itemCreatedDateAndTime;
        this.toDoItemDeadline = toDoItemDeadline;
        this.category = category;
        this.completionStatus = completionStatus.INCOMPLETE;
        this.price = 0;
    }

    public ToDoItem(String title, int priority, String detailDescription, Long itemCreatedDateAndTime, Long toDoItemDeadline, int category, int statusCode) {
        this.title = title;
        this.priority = priority;
        this.detailDescription = detailDescription;
        this.itemCreatedDateAndTime = itemCreatedDateAndTime;
        this.toDoItemDeadline = toDoItemDeadline;
        this.category = category;
        switch (statusCode) {
            case 2:
                this.completionStatus = completionStatus.COMPLETED;
                break;
            default:
                this.completionStatus = completionStatus.INCOMPLETE;
                break;
        }
        this.price = 0;
    }

    public ToDoItem(String title, int priority, String detailDescription, Long itemCreatedDateAndTime, Long toDoItemDeadline, int category, CompletionStatus completionStatus) {
        this.title = title;
        this.priority = priority;
        this.detailDescription = detailDescription;
        this.itemCreatedDateAndTime = itemCreatedDateAndTime;
        this.toDoItemDeadline = toDoItemDeadline;
        this.category = category;
        this.completionStatus = completionStatus;
        this.price = 0;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public CompletionStatus getCompletionStatus() {
        return this.completionStatus;
    }

    public void setCompletionStatus(CompletionStatus completionStatus) {
        this.completionStatus = completionStatus;
    }

    public int getCategory() {
        return category;
    }

    public int getPriority() {
        return priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setToDoItemDeadline(Long toDoItemDeadline) {
        this.toDoItemDeadline = toDoItemDeadline;
    }

    public String getTitle() {
        return title;
    }

    public Long getItemCreatedDateAndTime() {
        return itemCreatedDateAndTime;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public Long getToDoItemDeadline() {
        return toDoItemDeadline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(priority);
        dest.writeString(detailDescription);
        dest.writeInt(category);
        dest.writeLong(itemCreatedDateAndTime);
        Log.v(LOG_TAG, "itemCreatedDateAndTime, writeToParcel(Parcel dest, int flags), ToDoItem: " + GeneralHelper.formatToString(itemCreatedDateAndTime));
        dest.writeLong(toDoItemDeadline);
        dest.writeValue(completionStatus);
        dest.writeDouble(price);
    }

    protected ToDoItem(Parcel in) {
        title = in.readString();
        priority = in.readInt();
        detailDescription = in.readString();
        category = in.readInt();
        itemCreatedDateAndTime = in.readLong();
        Log.v(LOG_TAG, "itemCreatedDateAndTime, in.readLong(), ToDoItem(Parcel in): " + GeneralHelper.formatToString(itemCreatedDateAndTime));
        toDoItemDeadline = in.readLong();
        completionStatus = (CompletionStatus) in.readValue(CompletionStatus.class.getClassLoader());
        price = in.readDouble();
    }

    public static final Creator<ToDoItem> CREATOR = new Creator<ToDoItem>() {
        @Override
        public ToDoItem createFromParcel(Parcel in) {
            return new ToDoItem(in);
        }

        @Override
        public ToDoItem[] newArray(int size) {
            return new ToDoItem[size];
        }
    };
}
