package com.example.tek.first.servant.todolist.helper;

import com.example.tek.first.servant.R;

public abstract class GeneralConstants {

    public static final String DESCRIPTION_IDENTIFIER = "descriptionIdentifier";
    public static final String TODOLISTITEM_IDENTIFIER = "toDoListItemIdentifier";
    public static final String PRIORITY_IDENTIFIER = "priorityIdentifier";
    public static final String TIME_SET_IDENTIFIER = "timeIdentifier";
    public static final String TIME_PICKED_IDENTIFIER = "timePickedIdentifier";
    public static final String HOUR_IDENTIFIER = "hourIdentifier";
    public static final String MINUTE_IDENTIFIER = "minuteIdentifier";
    public static final String DAY_IDENTIFIER = "dayIdentifier";
    public static final String YEAR_IDENTIFIER = "yearIdentifier";
    public static final String MONTH_IDENTIFIER = "monthIdentifier";
    public static final String DATE_PICKED_IDENTIFIER = "datePickedIdentifier";

    public static final int TODOLISTITEM_STATUS_NOT_STARTED = 0;
    public static final int TODOLISTITEM_STATUS_INCOMPLETE = 1;
    public static final int TODOLISTITEM_STATUS_COMPLETE = 2;

    public static final String TO_DO_ITEM_IDENTIFIER = "toDoItemIdentifier";
    public static final String SAVEINSTANCESTATE_TODOITEMS_ARRAYLIST_IDENTIFIER = "toDoItemIdentifier";

    // todo: how to getResources().getStringArray..... instead such self-created int Array
    public static final int[] PRIORITY_LEVEL_COLOR
            = {R.color.priority_level1, R.color.priority_level2, R.color.priority_level3,
            R.color.priority_level4, R.color.priority_level5, R.color.priority_level6,
            R.color.priority_level7, R.color.priority_level8, R.color.priority_level9,
            R.color.priority_level10};

    public static final String[] PRIORITY_LEVEL_COLOR_HEX_CODE
            = {"#FBE9E7", "#FFCCBC", "#FFAB91", "#FF7043", "#FF5722",
            "#F4511E", "#E64A19", "#D84315", "#BF360C", "#DD2C00"};

}


