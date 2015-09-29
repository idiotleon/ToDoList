package com.leontheprofessional.todolist.helper;

import android.os.Bundle;

import com.leontheprofessional.todolist.R;

public abstract class GeneralConstants {

    public static final String COMPLETED_TODOLISTITEM_IDENTIFIER = "completedToDoListItemIdentifier";
    public static final String INCOMPLETED_TODOLISTITEM_IDENTIFIER = "incompleteToDoListItemIdentifier";
    public static final String DETAILED_TO_DO_ITEM_IDENTIFIER = "toDoItemIdentifier";
    public static final String SIMPLE_TO_DO_ITEM_IDENTIFIER = "simpleToDoItemIdentifier";

    public static final String HOUR_IDENTIFIER = "hourIdentifier";
    public static final String MINUTE_IDENTIFIER = "minuteIdentifier";
    public static final String DAY_IDENTIFIER = "dayIdentifier";
    public static final String YEAR_IDENTIFIER = "yearIdentifier";
    public static final String MONTH_IDENTIFIER = "monthIdentifier";

    public static final String SAVEINSTANCESTATE_INCOMPLETE_TODOITEMS_ARRAYLIST_IDENTIFIER = "incompleteToDoItemIdentifier";
    public static final String SAVEINSTANCESTATE_COMPLETED_TODOITEMS_ARRAYLIST_IDENTIFIER = "completeToDoItemIdentifier";
    public static final String SAVEINSTANCESTATE_SIMPLE_TODOITEM_IDENTIFIER = "simpleToDoItemIdentifier";
    public static final String SAVEINSTANCESTATE_TODOITEM_IDENTIFIER = "toDoItemIdentifier";

    public static final Bundle NULL_SAVED_INSTANCE_STATE = null;

    public static final int PRIORITY_LEVEL_OPTIONS = 10;

    // todo: how to getResources().getStringArray..... instead such self-created int Array
    public static final int[] PRIORITY_LEVEL_COLOR
            = {R.color.priority_level1, R.color.priority_level2, R.color.priority_level3,
            R.color.priority_level4, R.color.priority_level5, R.color.priority_level6,
            R.color.priority_level7, R.color.priority_level8, R.color.priority_level9,
            R.color.priority_level10};

    public static final String[] PRIORITY_LEVEL_COLOR_HEX_CODE
            = {"#FBE9E7", "#FFCCBC", "#FFAB91", "#FF7043", "#FF5722",
            "#F4511E", "#E64A19", "#D84315", "#BF360C", "#DD2C00"};


    public static final String TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER = "sortingTrendASCOrDESC";
    public static final String TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER = "sortingWayPriorityDeadlineTimeAddedOrTitle";

    public static final int YEAR_OPTION = 1;
    public static final int MONTH_OF_YEAR_OPTION = 2;
    public static final int DAY_OF_MONTH_OPTION = 3;
    public static final int HOUR_OF_DAY_OPTION = 4;
    public static final int MINUTE_OF_HOUR_OPTION = 5;
    public static final int SECOND_OF_MINUTE_OPTION = 6;

    public static final int SHOW_PREFERENCES = 0;
    public static final String UPDATE_TIME_PREFERENCE = "minUpdateFrequncy";

}


