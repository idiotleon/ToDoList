package com.example.tek.first.servant.todolist.activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.example.tek.first.servant.todolist.fragment.display.adapter.SimpleToDoItemsListViewCustomAdapter;
import com.example.tek.first.servant.todolist.fragment.display.adapter.ToDoItemsListViewCustomAdapter;
import com.example.tek.first.servant.todolist.fragment.dialog.DatePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import com.example.tek.first.servant.todolist.fragment.NewItemAddedFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.TimePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.display.CompletedDetailedItemsDisplayFragment;
import com.example.tek.first.servant.todolist.fragment.display.IncompleteDetailedItemsDisplayFragment;
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
        NewItemAddedFragment.OnNewSimpleItemAddedListener,
        IncompleteDetailedItemsDisplayFragment.ToDoItemStatusChangeListener,
        CompletedDetailedItemsDisplayFragment.ToDoItemStatusChangeListener,
        SimpleToDoItemsDisplayFragment.ToDoItemStatusChangeListener {

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

    private NewItemAddedFragment newItemAddedFragment;
    private IncompleteDetailedItemsDisplayFragment incompleteToDoItemDisplayListFragment;
    private CompletedDetailedItemsDisplayFragment completedToDoItemDisplayListFragment;
    private SimpleToDoItemsDisplayFragment simpleToDoItemsDisplayFragment;

    private static int counterOfSortByPrioritySelectedTimes = 0;
    private static int counterOfSortByDeadlineSelectedTimes = 0;
    private static int counterOfSortByTimeAddedSelectedTimes = 0;
    private static int counterOfSortByTitleSelectedTimes = 0;

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
            simpleToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_SIMPLE_TODOITEM_IDENTIFIER);
        } else {
            dbHelper = new DatabaseHelper(ToDoListMainActivity.this);
        }

        FragmentManager fragmentManager = getFragmentManager();
        newItemAddedFragment
                = (NewItemAddedFragment) fragmentManager.findFragmentById(R.id.todolist_newitem);
        incompleteToDoItemDisplayListFragment
                = (IncompleteDetailedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_incomplete_items);
        completedToDoItemDisplayListFragment
                = (CompletedDetailedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_completed_items);
        simpleToDoItemsDisplayFragment
                = (SimpleToDoItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_simple_todoitems);

        refreshPage();
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
                                        GeneralHelper.showFragment(ToDoListMainActivity.this, incompleteToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListMainActivity.this, completedToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListMainActivity.this, simpleToDoItemsDisplayFragment);
                                        break;
                                    case 1:     // Display only completed items
                                        GeneralHelper.showFragment(ToDoListMainActivity.this, completedToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListMainActivity.this, incompleteToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListMainActivity.this, simpleToDoItemsDisplayFragment);
                                        break;
                                    case 2: // Display all simple todoitems
                                        GeneralHelper.showFragment(ToDoListMainActivity.this, simpleToDoItemsDisplayFragment);
                                        GeneralHelper.hideFragment(ToDoListMainActivity.this, completedToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListMainActivity.this, incompleteToDoItemDisplayListFragment);
                                        break;
                                    default:     // Display all items
                                        GeneralHelper.showFragment(ToDoListMainActivity.this, completedToDoItemDisplayListFragment);
                                        GeneralHelper.showFragment(ToDoListMainActivity.this, incompleteToDoItemDisplayListFragment);
                                        GeneralHelper.showFragment(ToDoListMainActivity.this, simpleToDoItemsDisplayFragment);
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
                                        setSortingPreference(DatabaseHelper.SORTING_WAY_BY_PRIORITY, counterOfSortByPrioritySelectedTimes);
                                        counterOfSortByPrioritySelectedTimes++;
                                        refreshPage();
                                        break;
                                    case 1:     // Sort by deadline
                                        setSortingPreference(DatabaseHelper.SORTING_WAY_BY_DEADLINE, counterOfSortByDeadlineSelectedTimes);
                                        counterOfSortByDeadlineSelectedTimes++;
                                        refreshPage();
                                        break;
                                    case 2:     // Sort by time added
                                        setSortingPreference(DatabaseHelper.SORTING_WAY_BY_TIME_ADDED, counterOfSortByTimeAddedSelectedTimes);
                                        counterOfSortByTimeAddedSelectedTimes++;
                                        refreshPage();
                                        break;
                                    case 3:     // Sort by title
                                        setSortingPreference(DatabaseHelper.SORTING_WAY_BY_TITLE, counterOfSortByTitleSelectedTimes);
                                        counterOfSortByTitleSelectedTimes++;
                                        refreshPage();
                                        break;
                                }
                            }
                        });
                (displayBuilder.create()).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSortingPreference(String sortingPreference, int counter) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ToDoListMainActivity.this);

        sharedPreferences.edit().putString(GeneralConstants.TODOITEMS_SORTING_WAY_SHAREDPREFERENCE_IDENTIFIER, sortingPreference).commit();

        if (counter % 2 == 0) {
            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_DESC).commit();
            Log.v(LOG_TAG, "Set sorting sharedPreference DESC");
        } else {
            sharedPreferences.edit().putInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER, DatabaseHelper.SORTING_STANDARD_ASC).commit();
            Log.v(LOG_TAG, "Set sorting sharedPreference ASC");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_TODOITEMS_ARRAYLIST_IDENTIFIER, incompleteToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_COMPLETED_TODOITEMS_ARRAYLIST_IDENTIFIER, completedToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_SIMPLE_TODOITEM_IDENTIFIER, simpleToDoItemsArrayList);
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
        refreshPage();
    }

    @Override
    public void onNewSimpleItemAdded(SimpleToDoItem newSimpleItem) {
        dbHelper.insertSimpleToDoItem(newSimpleItem);
        Log.v(LOG_TAG, "A new simple ToDoItem added.");
        Toast.makeText(ToDoListMainActivity.this, "A new simple ToDoItem added", Toast.LENGTH_SHORT).show();
        simpleToDoItemsArrayList.add(0, newSimpleItem);
        refreshPage();
    }

    @Override
    public void onStatusChanged() {
        Log.v(LOG_TAG, "onStatusChanged(), ToDoListMainActivity executed.");
        refreshPage();
    }

    private void refreshPage() {
        incompleteToDoItemsArrayList = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.INCOMPLETE);
        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsArrayList);
        if (!incompleteToDoItemsArrayList.isEmpty()) {
            incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
        } else {
            incompleteToDoItemDisplayListFragment.setListAdapter(null);
        }

        completedToDoItemsArrayList = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(CompletionStatus.COMPLETED);
        completedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, completedToDoItemsArrayList);
        if (!completedToDoItemsArrayList.isEmpty()) {
            completedToDoItemDisplayListFragment.setListAdapter(completedToDoItemsCustomAdapter);
        } else {
            completedToDoItemDisplayListFragment.setListAdapter(null);
        }

        simpleToDoItemsArrayList = dbHelper.getSortedSimpleToDoItemsAsArrayList();
        simpleToDoItemsListViewCustomAdapter = new SimpleToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, simpleToDoItemsArrayList);
        if (!simpleToDoItemsArrayList.isEmpty()) {
            simpleToDoItemsDisplayFragment.setListAdapter(simpleToDoItemsListViewCustomAdapter);
        } else {
            simpleToDoItemsDisplayFragment.setListAdapter(null);
        }
    }


}