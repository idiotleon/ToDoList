package com.example.tek.first.servant.todolist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.helper.GeneralHelper.CompletionStatus;

public class ToDoItemModel implements Parcelable {

    private static final String LOG_TAG = ToDoItemModel.class.getSimpleName();

    private String title;
    private int priority;
    private String detailDescription;
    private Long itemCreatedDateAndTime;
    private Long toDoItemDeadline;
    private int category;
    private CompletionStatus completionStatus;

    public ToDoItemModel() {
        this.title = null;
        this.priority = 1;
        this.detailDescription = null;
        this.itemCreatedDateAndTime = 0L;
        this.toDoItemDeadline = 0L;
        this.category = 0;
        this.completionStatus = CompletionStatus.NOTSTARTED;
    }

    public ToDoItemModel(String title) {
        this.title = title;
        this.priority = 1;
        this.detailDescription = null;
        this.itemCreatedDateAndTime = 0L;
        this.toDoItemDeadline = 0L;
        this.category = 0;
        this.completionStatus = CompletionStatus.NOTSTARTED;
    }

    public ToDoItemModel(String title, Long currentTime) {
        this.title = title;
        this.priority = 1;
        this.detailDescription = null;
        this.itemCreatedDateAndTime = currentTime;
        this.toDoItemDeadline = 0L;
        this.category = 0;
        this.completionStatus = CompletionStatus.NOTSTARTED;
    }

    public ToDoItemModel(String title, int priority, long toDoItemDeadline) {
        this.title = title;
        this.priority = priority;
        this.toDoItemDeadline = toDoItemDeadline;
    }

    public ToDoItemModel(String title, int priority, String detailDescription, Long itemCreatedDateAndTime, Long toDoItemDeadline, int category) {
        this.title = title;
        this.priority = priority;
        this.detailDescription = detailDescription;
        this.itemCreatedDateAndTime = itemCreatedDateAndTime;
        this.toDoItemDeadline = toDoItemDeadline;
        this.category = category;
        this.completionStatus = completionStatus.NOTSTARTED;

    }

    public ToDoItemModel(String title, int priority, String detailDescription, Long itemCreatedDateAndTime, Long toDoItemDeadline, int category, int statusCode) {
        this.title = title;
        this.priority = priority;
        this.detailDescription = detailDescription;
        this.itemCreatedDateAndTime = itemCreatedDateAndTime;
        this.toDoItemDeadline = toDoItemDeadline;
        this.category = category;
        switch (statusCode) {
            case 0:
                this.completionStatus = completionStatus.NOTSTARTED;
                break;
            case 1:
                this.completionStatus = completionStatus.INCOMPLETED;
                break;
            case 2:
                this.completionStatus = completionStatus.COMPLETED;
                break;
            default:
                Log.v(LOG_TAG, "completeStatusCode is OutOfBound. lol");
                break;
        }
    }

    public ToDoItemModel(String title, int priority, String detailDescription, Long itemCreatedDateAndTime, Long toDoItemDeadline, int category, CompletionStatus completionStatus) {
        this.title = title;
        this.priority = priority;
        this.detailDescription = detailDescription;
        this.itemCreatedDateAndTime = itemCreatedDateAndTime;
        this.toDoItemDeadline = toDoItemDeadline;
        this.category = category;
        this.completionStatus = completionStatus;
    }

    public CompletionStatus getCompletionStatus() {
        return this.completionStatus;
    }

    public void setCompletionStatus(CompletionStatus completionStatus) {
        this.completionStatus = completionStatus;
    }

//    public void setCompleteStatusCode(int statusCode) {
//        completionStatus.setStatusCode(statusCode);
//    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCategory() {
        return category;
    }

    /**
     * Getters only used for interaction with the database
     *
     * @return
     */
    public int getCompleteStatusCode() {
        return completionStatus.getStatusCode();
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItemCreatedDateAndTime(Long itemCreatedDateAndTime) {
        this.itemCreatedDateAndTime = itemCreatedDateAndTime;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
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
        Log.v(LOG_TAG, "itemCreatedDateAndTime, writeToParcel(Parcel dest, int flags), ToDoItemModel: " + GeneralHelper.formatToString(itemCreatedDateAndTime));
        dest.writeLong(toDoItemDeadline);
        dest.writeValue(completionStatus);
    }

    protected ToDoItemModel(Parcel in) {
        title = in.readString();
        priority = in.readInt();
        detailDescription = in.readString();
        category = in.readInt();
        itemCreatedDateAndTime = in.readLong();
        Log.v(LOG_TAG, "itemCreatedDateAndTime, in.readLong(), ToDoItemModel(Parcel in): " + GeneralHelper.formatToString(itemCreatedDateAndTime));
        toDoItemDeadline = in.readLong();
        completionStatus = (CompletionStatus) in.readValue(CompletionStatus.class.getClassLoader());
    }

    public static final Creator<ToDoItemModel> CREATOR = new Creator<ToDoItemModel>() {
        @Override
        public ToDoItemModel createFromParcel(Parcel in) {
            return new ToDoItemModel(in);
        }

        @Override
        public ToDoItemModel[] newArray(int size) {
            return new ToDoItemModel[size];
        }
    };
}
