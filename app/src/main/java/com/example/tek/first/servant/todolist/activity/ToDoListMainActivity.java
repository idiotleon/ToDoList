package com.example.tek.first.servant.todolist.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tek.first.servant.R;
//import com.example.tek.first.servant.todolist.fragment.display.GeneralToDoItemsDisplayFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.DatePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import com.example.tek.first.servant.todolist.fragment.NewItemAddedFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.TimePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.display.CompletedItemsDisplayFragment;
import com.example.tek.first.servant.todolist.fragment.display.IncompleteItemsDisplayFragment;
import com.example.tek.first.servant.todolist.fragment.display.NotStartedItemsDisplayFragment;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.DateModel;
import com.example.tek.first.servant.todolist.model.TimeModel;
import com.example.tek.first.servant.todolist.model.ToDoItemModel;

import com.example.tek.first.servant.todolist.helper.GeneralHelper.CompletionStatus;

import java.util.ArrayList;


public class ToDoListMainActivity extends Activity
        implements DatePickerDialogFragment.DatePickerDialogListener,
        TimePickerDialogFragment.TimePickerDialogListener,
        DetailedNewToDoItemDialogFragment.OnNewItemAddedListener,
        NewItemAddedFragment.OnNewSimpleItemAddedListener {

    public static String LOG_TAG = ToDoListMainActivity.class.getSimpleName();

    private DateModel dateSelected = null;
    private TimeModel timeSelected = null;

    private DatabaseHelper dbHelper;

    private ArrayList<ToDoItemModel> toDoItemsArrayList;
    private ArrayList<ToDoItemModel> incompleteToDoItemsArrayList;
    private ArrayList<ToDoItemModel> completedToDoItemsArrayList;
    private ArrayList<ToDoItemModel> notStartedToDoItemsArrayList;

    private ToDoItemsListViewCustomAdapter generalToDoItemsCustomAdapter;
    private ToDoItemsListViewCustomAdapter incompleteToDoItemsCustomAdapter;
    private ToDoItemsListViewCustomAdapter completedToDoItemsCustomAdapter;
    private ToDoItemsListViewCustomAdapter notStartedToDoItemsCustomAdapter;

    //    private GeneralToDoItemsDisplayFragment generalToDoItemDisplayFragment;
    private IncompleteItemsDisplayFragment incompleteToDoItemDisplayListFragment;
    private CompletedItemsDisplayFragment completedToDoItemDisplayListFragment;
    private NotStartedItemsDisplayFragment notStartedToDoItemDisplayListFragment;



    private int counterOfSortByPrioritySelectedTimes = 0;
    private int counterOfSortByDeadlineSelectedTimes = 0;
    private int counterOfSortByTimeAddedSelectedTimes = 0;
    private int counterOfSortByTitleSelectedTimes = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_main_activity);

        toDoItemsArrayList = new ArrayList<>();
        incompleteToDoItemsArrayList = new ArrayList<>();
        completedToDoItemsArrayList = new ArrayList<>();
        notStartedToDoItemsArrayList = new ArrayList<>();

        if (savedInstanceState != null) {
            toDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_ALL_TODOITEMS_ARRAYLIST_IDENTIFIER);
            incompleteToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_TODOITEMS_ARRAYLIST_IDENTIFIER);
            completedToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_COMPLETED_TODOITEMS_ARRAYLIST_IDENTIFIER);
            notStartedToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_NOTSTARTED_TODOITEMS_ARRAYLIST_IDENTIFIER);
        } else {
            dbHelper = new DatabaseHelper(ToDoListMainActivity.this);

            toDoItemsArrayList = dbHelper.getToDoItemsSortedAsArrayList();

            incompleteToDoItemsArrayList = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.INCOMPLETED);
            GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(incompleteToDoItemsArrayList, "displayIncompleteToDoItemsAsArrayList");
            notStartedToDoItemsArrayList = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.NOTSTARTED);
            GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(notStartedToDoItemsArrayList, "displayNotStartedToDoItemsAsArrayList");
            completedToDoItemsArrayList = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.COMPLETED);
            GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(completedToDoItemsArrayList, "completedToDoItemsArrayList");
        }

        FragmentManager fragmentManager = getFragmentManager();
        NewItemAddedFragment newItemAddedFragment =
                (NewItemAddedFragment) fragmentManager.findFragmentById(R.id.todolist_newitem);

        displayAllThreeFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todolist_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.display_menu_activitytodolist:
                Log.v(LOG_TAG, "Displaying-Menu Selected");
                AlertDialog.Builder sortBuilder = new AlertDialog.Builder(ToDoListMainActivity.this);
                sortBuilder.setTitle("Please select what to be displayed: ")
                        .setItems(R.array.menu_display_standard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:     // Display only incomplete items, sorted in SharedPreference mode
                                        ArrayList<ToDoItemModel> incompleteToDoItemsArrayList
                                                = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.INCOMPLETED);
                                        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsArrayList);
                                        incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
                                        hideFragment(completedToDoItemDisplayListFragment);
                                        hideFragment(notStartedToDoItemDisplayListFragment);
                                        showFragment(incompleteToDoItemDisplayListFragment);
                                        break;
                                    case 1:     // Display only completed items
                                        ArrayList<ToDoItemModel> completeToDoItemsArrayList
                                                = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.COMPLETED);
                                        completedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, completeToDoItemsArrayList);
                                        completedToDoItemDisplayListFragment.setListAdapter(completedToDoItemsCustomAdapter);
                                        hideFragment(incompleteToDoItemDisplayListFragment);
                                        hideFragment(notStartedToDoItemDisplayListFragment);
                                        showFragment(completedToDoItemDisplayListFragment);
                                        break;
                                    case 2:     // Display only not started items
                                        ArrayList<ToDoItemModel> notStartedToDoItemsArrayList
                                                = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.NOTSTARTED);
                                        notStartedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, notStartedToDoItemsArrayList);
                                        notStartedToDoItemDisplayListFragment.setListAdapter(notStartedToDoItemsCustomAdapter);
                                        hideFragment(incompleteToDoItemDisplayListFragment);
                                        hideFragment(completedToDoItemDisplayListFragment);
                                        showFragment(notStartedToDoItemDisplayListFragment);
                                        break;
                                    case 3:     // Display all items
                                        displayAllThreeFragments();
                                        break;
                                }
                            }
                        });
                (sortBuilder.create()).show();
                break;
            case R.id.sortby_menu_activitytodolist:
                Log.v(LOG_TAG, "Sorting-Menu Selected");
                AlertDialog.Builder displayBuilder = new AlertDialog.Builder(ToDoListMainActivity.this);
                displayBuilder.setTitle("Please select a sorting standard: ")
                        .setItems(R.array.menu_sort_standard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:     // Sort by priority
/*                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByPriority = null;
                                        sharedPreferences.edit().putString(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER, DatabaseHelper.SORTING_WAY_BY_PRIORITY).commit();
                                        if (counterOfSortByPrioritySelectedTimes % 2 == 0) {
                                            toDoItemModelArrayListSortByPriority = dbHelper.getToDoItemsSortedAsArrayList(DatabaseHelper.TODOLIST_ITEM_PRIORITY, DatabaseHelper.SORTING_STANDARD_DESC);
                                            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_DESC).commit();
                                        } else {
                                            toDoItemModelArrayListSortByPriority = dbHelper.getToDoItemsSortedAsArrayList(DatabaseHelper.TODOLIST_ITEM_PRIORITY, DatabaseHelper.SORTING_STANDARD_ASC);
                                            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_ASC).commit();
                                        }
                                        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByPriority);
                                        incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
                                        counterOfSortByPrioritySelectedTimes++;*/
                                        sortToDoItemsArrayList(DatabaseHelper.SORTING_WAY_BY_PRIORITY, counterOfSortByPrioritySelectedTimes);
                                        break;
                                    case 1:     // Sort by deadline
/*                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByDeadline = dbHelper.toDoItemsArrayListSortByDeadline();
                                        sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER, DatabaseHelper.SORTING_WAY_BY_DEADLINE).commit();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByDeadline);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);*/
                                        sortToDoItemsArrayList(DatabaseHelper.SORTING_WAY_BY_DEADLINE, counterOfSortByDeadlineSelectedTimes);
                                        break;
                                    case 2:     // Sort by time added
 /*                                       ArrayList<ToDoItemModel> toDoItemModelArrayListSortByTimeAdded = dbHelper.toDoItemsArrayListSortByTimeAdded();
                                        sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER, DatabaseHelper.SORTING_WAY_BY_TIME_ADDED).commit();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByTimeAdded);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);*/
                                        sortToDoItemsArrayList(DatabaseHelper.SORTING_WAY_BY_TIME_ADDED, counterOfSortByTimeAddedSelectedTimes);
                                        break;
                                    case 3:     // Sort by title
/*                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByTitle = dbHelper.toDoItemsArrayListSortByTitle();
                                        sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER, DatabaseHelper.SORTING_WAY_BY_TITLE).commit();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByTitle);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);*/
                                        sortToDoItemsArrayList(DatabaseHelper.SORTING_WAY_BY_TITLE, counterOfSortByTitleSelectedTimes);
                                        break;
                                }
                            }
                        });
                (displayBuilder.create()).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(fragment)
                .commit();
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(fragment)
                .commit();
    }

    private void displayAllThreeFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        incompleteToDoItemDisplayListFragment
                = (IncompleteItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_incomplete_items);
        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsArrayList);
        if (!incompleteToDoItemsArrayList.isEmpty()) {
            incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
            showFragment(incompleteToDoItemDisplayListFragment);
        } else {
            incompleteToDoItemDisplayListFragment.setListAdapter(null);
        }

        completedToDoItemDisplayListFragment
                = (CompletedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_completed_items);
        completedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, completedToDoItemsArrayList);
        if (!completedToDoItemsArrayList.isEmpty()) {
            completedToDoItemDisplayListFragment.setListAdapter(completedToDoItemsCustomAdapter);
            showFragment(incompleteToDoItemDisplayListFragment);
        } else {
            completedToDoItemDisplayListFragment.setListAdapter(null);
        }

        notStartedToDoItemDisplayListFragment
                = (NotStartedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_not_started_items);
        notStartedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, notStartedToDoItemsArrayList);
        if (!notStartedToDoItemsArrayList.isEmpty()) {
            notStartedToDoItemDisplayListFragment.setListAdapter(notStartedToDoItemsCustomAdapter);
            showFragment(incompleteToDoItemDisplayListFragment);
        } else {
            notStartedToDoItemDisplayListFragment.setListAdapter(null);
        }
    }

    private void sortToDoItemsArrayList(String sortingPreference, int counter) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ToDoListMainActivity.this);

        ArrayList<ToDoItemModel> incompleteToDoItemsAsArrayListSorted = null;
        ArrayList<ToDoItemModel> completedToDoItemsAsArrayListSorted = null;
        sharedPreferences.edit().putString(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER, sortingPreference).commit();

        if (counter % 2 == 0) {
            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_DESC).commit();
        } else {
            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_ASC).commit();
        }
        counter++;

        incompleteToDoItemsAsArrayListSorted = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.INCOMPLETED);
        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsAsArrayListSorted);
        incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);

        completedToDoItemsAsArrayListSorted = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.COMPLETED);
        completedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, completedToDoItemsAsArrayListSorted);
        completedToDoItemDisplayListFragment.setListAdapter(completedToDoItemsCustomAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_ALL_TODOITEMS_ARRAYLIST_IDENTIFIER, toDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_TODOITEMS_ARRAYLIST_IDENTIFIER, incompleteToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_COMPLETED_TODOITEMS_ARRAYLIST_IDENTIFIER, completedToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_NOTSTARTED_TODOITEMS_ARRAYLIST_IDENTIFIER, notStartedToDoItemsArrayList);
    }

    @Override
    public void onDateSelected(DateModel dateSelected) {
        this.dateSelected = dateSelected;
        Toast.makeText(ToDoListMainActivity.this, "Date set: " + dateSelected.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSelected(TimeModel timeSelected) {
        this.timeSelected = timeSelected;
        Toast.makeText(ToDoListMainActivity.this, "Time set: " + timeSelected.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewItemAdded(ToDoItemModel toDoItem) {
        Long deadline = GeneralHelper.dateAndTimeFormattedToLong(dateSelected, timeSelected);
        Log.v(LOG_TAG, "deadline, onNewItemAdded, ToDoListMainActivity: " + deadline);
        toDoItem.setToDoItemDeadline(deadline);
//        this.toDoItem = toDoItem;
        dbHelper.insertToDoListItem(toDoItem);
        toDoItemsArrayList.add(0, toDoItem);
        generalToDoItemsCustomAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNewSimpleItemAdded(ToDoItemModel newSimpleItem) {
        dbHelper.insertToDoListItem(newSimpleItem);
        toDoItemsArrayList.add(0, newSimpleItem);
        generalToDoItemsCustomAdapter.notifyDataSetChanged();
    }
}