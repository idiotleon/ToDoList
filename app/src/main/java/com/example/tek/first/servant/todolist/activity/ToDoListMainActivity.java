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
import com.example.tek.first.servant.todolist.adapter.SimpleToDoItemsListViewCustomAdapter;
import com.example.tek.first.servant.todolist.adapter.ToDoItemsListViewCustomAdapter;
import com.example.tek.first.servant.todolist.fragment.dialog.DatePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import com.example.tek.first.servant.todolist.fragment.NewItemAddedFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.TimePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.display.CompletedItemsDisplayFragment;
import com.example.tek.first.servant.todolist.fragment.display.IncompleteItemsDisplayFragment;
import com.example.tek.first.servant.todolist.fragment.display.SimpleToDoItemsDisplayFragment;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.Date;
import com.example.tek.first.servant.todolist.model.SimpleToDoItem;
import com.example.tek.first.servant.todolist.model.Time;

import com.example.tek.first.servant.todolist.helper.GeneralHelper.CompletionStatus;
import com.example.tek.first.servant.todolist.model.ToDoItem;

import java.util.ArrayList;


public class ToDoListMainActivity extends Activity
        implements DatePickerDialogFragment.DatePickerDialogListener,
        TimePickerDialogFragment.TimePickerDialogListener,
        DetailedNewToDoItemDialogFragment.OnNewItemAddedListener,
        NewItemAddedFragment.OnNewSimpleItemAddedListener {

    public static String LOG_TAG = ToDoListMainActivity.class.getSimpleName();

    private Date dateSelected = null;
    private Time timeSelected = null;

    private DatabaseHelper dbHelper;

    private ArrayList<ToDoItem> incompleteToDoItemsArrayList;
    private ArrayList<ToDoItem> completedToDoItemsArrayList;
    private ArrayList<SimpleToDoItem> simpleToDoItemsArrayList;

    private ToDoItemsListViewCustomAdapter incompleteToDoItemsCustomAdapter;
    private ToDoItemsListViewCustomAdapter completedToDoItemsCustomAdapter;
    private SimpleToDoItemsListViewCustomAdapter simpleToDoItemsListViewCustomAdapter;

    private IncompleteItemsDisplayFragment incompleteToDoItemDisplayListFragment;
    private CompletedItemsDisplayFragment completedToDoItemDisplayListFragment;
    private SimpleToDoItemsDisplayFragment simpleToDoItemsDisplayFragment;

    private int counterOfSortByPrioritySelectedTimes = 0;
    private int counterOfSortByDeadlineSelectedTimes = 0;
    private int counterOfSortByTimeAddedSelectedTimes = 0;
    private int counterOfSortByTitleSelectedTimes = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_main_activity);

        incompleteToDoItemsArrayList = new ArrayList<>();
        completedToDoItemsArrayList = new ArrayList<>();
        simpleToDoItemsArrayList = new ArrayList<>();

        if (savedInstanceState != null) {
            incompleteToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_TODOITEMS_ARRAYLIST_IDENTIFIER);
            completedToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_COMPLETED_TODOITEMS_ARRAYLIST_IDENTIFIER);
        } else {
            dbHelper = new DatabaseHelper(ToDoListMainActivity.this);

            incompleteToDoItemsArrayList = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.INCOMPLETED);
            GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(incompleteToDoItemsArrayList, "displayIncompleteToDoItemsAsArrayList");
            completedToDoItemsArrayList = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.COMPLETED);
            GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(completedToDoItemsArrayList, "completedToDoItemsArrayList");
        }

        FragmentManager fragmentManager = getFragmentManager();
        NewItemAddedFragment newItemAddedFragment =
                (NewItemAddedFragment) fragmentManager.findFragmentById(R.id.todolist_newitem);

        incompleteToDoItemDisplayListFragment
                = (IncompleteItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_incomplete_items);
        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsArrayList);
        if (!incompleteToDoItemsArrayList.isEmpty()) {
            incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
//            showFragment(incompleteToDoItemDisplayListFragment);
        } else {
            incompleteToDoItemDisplayListFragment.setListAdapter(null);
        }

        completedToDoItemDisplayListFragment
                = (CompletedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_completed_items);
        completedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, completedToDoItemsArrayList);
        if (!completedToDoItemsArrayList.isEmpty()) {
            completedToDoItemDisplayListFragment.setListAdapter(completedToDoItemsCustomAdapter);
//            showFragment(incompleteToDoItemDisplayListFragment);
        } else {
            completedToDoItemDisplayListFragment.setListAdapter(null);
        }

        simpleToDoItemsDisplayFragment = (SimpleToDoItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_simple_todoitems);
        simpleToDoItemsListViewCustomAdapter = new SimpleToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, simpleToDoItemsArrayList);
        if (!simpleToDoItemsArrayList.isEmpty()) {
            simpleToDoItemsDisplayFragment.setListAdapter(simpleToDoItemsListViewCustomAdapter);
        } else {
            simpleToDoItemsDisplayFragment.setListAdapter(null);
        }
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
                                        ArrayList<ToDoItem> incompleteToDoItemsArrayList
                                                = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.INCOMPLETED);
                                        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsArrayList);
                                        incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
                                        hideFragment(completedToDoItemDisplayListFragment);
                                        showFragment(incompleteToDoItemDisplayListFragment);
                                        break;
                                    case 1:     // Display only completed items
                                        ArrayList<ToDoItem> completeToDoItemsArrayList
                                                = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.COMPLETED);
                                        completedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, completeToDoItemsArrayList);
                                        completedToDoItemDisplayListFragment.setListAdapter(completedToDoItemsCustomAdapter);
                                        hideFragment(incompleteToDoItemDisplayListFragment);
                                        showFragment(completedToDoItemDisplayListFragment);
                                        break;
                                    case 2:     // Display all items
//                                        displayCompletedAndIncompleteToDoItemsFragment();
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
                                        sortToDoItemsArrayList(DatabaseHelper.SORTING_WAY_BY_PRIORITY, counterOfSortByPrioritySelectedTimes);
                                        break;
                                    case 1:     // Sort by deadline
                                        sortToDoItemsArrayList(DatabaseHelper.SORTING_WAY_BY_DEADLINE, counterOfSortByDeadlineSelectedTimes);
                                        break;
                                    case 2:     // Sort by time added
                                        sortToDoItemsArrayList(DatabaseHelper.SORTING_WAY_BY_TIME_ADDED, counterOfSortByTimeAddedSelectedTimes);
                                        break;
                                    case 3:     // Sort by title
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

    private void sortToDoItemsArrayList(String sortingPreference, int counter) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ToDoListMainActivity.this);

        ArrayList<ToDoItem> incompleteToDoItemsAsArrayListSorted = null;
        ArrayList<ToDoItem> completedToDoItemsAsArrayListSorted = null;
        sharedPreferences.edit().putString(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER, sortingPreference).commit();

        if (counter % 2 == 0) {
            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_DESC).commit();
            Log.v(LOG_TAG, "Set sorting sharedPreference DESC");
        } else {
            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_ASC).commit();
            Log.v(LOG_TAG, "Set sorting sharedPreference ASC");
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
                GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_TODOITEMS_ARRAYLIST_IDENTIFIER, incompleteToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_COMPLETED_TODOITEMS_ARRAYLIST_IDENTIFIER, completedToDoItemsArrayList);
    }

    @Override
    public void onDateSelected(Date dateSelected) {
        this.dateSelected = dateSelected;
        Toast.makeText(ToDoListMainActivity.this, "Date set: " + dateSelected.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSelected(Time timeSelected) {
        this.timeSelected = timeSelected;
        Toast.makeText(ToDoListMainActivity.this, "Time set: " + timeSelected.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewItemAdded(ToDoItem toDoItem) {
        Long deadline = GeneralHelper.dateAndTimeFormattedToLong(dateSelected, timeSelected);
        Log.v(LOG_TAG, "deadline, onNewItemAdded, ToDoListMainActivity: " + deadline);
        Toast.makeText(ToDoListMainActivity.this, "A new ToDoItem added", Toast.LENGTH_SHORT).show();
        toDoItem.setToDoItemDeadline(deadline);
        dbHelper.insertToDoListItem(toDoItem);
        incompleteToDoItemsArrayList.add(0, toDoItem);
//        incompleteToDoItemsCustomAdapter.notifyDataSetChanged();
        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsArrayList);
        incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
    }

    @Override
    public void onNewSimpleItemAdded(SimpleToDoItem newSimpleItem) {
        dbHelper.insertSimpleToDoItem(newSimpleItem);
        Log.v(LOG_TAG, "A new simple ToDoItem added.");
        Toast.makeText(ToDoListMainActivity.this, "A new simple ToDoItem added", Toast.LENGTH_SHORT).show();
        simpleToDoItemsArrayList.add(0, newSimpleItem);
//        incompleteToDoItemsCustomAdapter.notifyDataSetChanged();
        simpleToDoItemsListViewCustomAdapter = new SimpleToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, simpleToDoItemsArrayList);
        incompleteToDoItemDisplayListFragment.setListAdapter(simpleToDoItemsListViewCustomAdapter);
    }
}