package com.example.tek.first.servant.todolist.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tek.first.servant.R;
import com.example.tek.first.servant.todolist.fragment.display.GeneralToDoItemsDisplayFragment;
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

    private GeneralToDoItemsDisplayFragment generalToDoItemDisplayFragment;
    private IncompleteItemsDisplayFragment incompleteToDoItemDisplayFragment;
    private CompletedItemsDisplayFragment completedToDoItemDisplayFragment;
    private NotStartedItemsDisplayFragment notStartedToDoItemDisplayFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_main_activity);

        toDoItemsArrayList = new ArrayList<>();
        incompleteToDoItemsArrayList = new ArrayList<>();
        completedToDoItemsArrayList = new ArrayList<>();
        notStartedToDoItemsArrayList = new ArrayList<>();

        if (savedInstanceState != null) {
            toDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_TODOITEMS_ARRAYLIST_IDENTIFIER);
        } else {
            dbHelper = new DatabaseHelper(ToDoListMainActivity.this);
            toDoItemsArrayList = dbHelper.getAllToDoItemsAsArrayList();
            incompleteToDoItemsArrayList = dbHelper.getAllToDoItemsAsArrayList(CompletionStatus.INCOMPLETED);
            GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(incompleteToDoItemsArrayList, "incompleteToDoItemsArrayList");
            notStartedToDoItemsArrayList = dbHelper.getAllToDoItemsAsArrayList(CompletionStatus.NOTSTARTED);
            GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(notStartedToDoItemsArrayList, "notStartedToDoItemsArrayList");
            completedToDoItemsArrayList = dbHelper.getAllToDoItemsAsArrayList(CompletionStatus.COMPLETED);
            GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(completedToDoItemsArrayList, "completedToDoItemsArrayList");
        }

        FragmentManager fragmentManager = getFragmentManager();
        NewItemAddedFragment newItemAddedFragment =
                (NewItemAddedFragment) fragmentManager.findFragmentById(R.id.todolist_newitem);

//        generalToDoItemDisplayFragment
//                = (GeneralToDoItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment);
//        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, toDoItemsArrayList);
//        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);


        Log.v(LOG_TAG, "incompleteToDoItemsArrayList.isEmpty(): " + incompleteToDoItemsArrayList.isEmpty());
        incompleteToDoItemDisplayFragment
                = (IncompleteItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_incomplete_items);
        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsArrayList);
        if (!incompleteToDoItemsArrayList.isEmpty()) {
            incompleteToDoItemDisplayFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
        } else {
            incompleteToDoItemDisplayFragment.setListAdapter(null);
        }


        completedToDoItemDisplayFragment
                = (CompletedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_completed_items);
        completedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, completedToDoItemsArrayList);
        if (!completedToDoItemsArrayList.isEmpty()) {
            completedToDoItemDisplayFragment.setListAdapter(completedToDoItemsCustomAdapter);
        } else {
            completedToDoItemDisplayFragment.setListAdapter(null);
        }

        notStartedToDoItemDisplayFragment
                = (NotStartedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_not_started_items);
        notStartedToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, notStartedToDoItemsArrayList);
        if (!notStartedToDoItemsArrayList.isEmpty()) {
            notStartedToDoItemDisplayFragment.setListAdapter(notStartedToDoItemsCustomAdapter);
        } else {
            notStartedToDoItemDisplayFragment.setListAdapter(null);
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
                                // todo: sorting standards should be saved as SharedPreferences
                                switch (which) {
                                    case 0:     // Display incomplete items
                                        ArrayList<ToDoItemModel> incompleteToDoItemsArrayList = dbHelper.incompleteToDoItemsArrayList();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsArrayList);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);
                                        break;
                                    case 1:     // Display completed items
                                        ArrayList<ToDoItemModel> completeToDoItemsArrayList = dbHelper.completeToDoItemsArrayList();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, completeToDoItemsArrayList);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);
                                        break;
                                    case 2:     // Display not started items
                                        ArrayList<ToDoItemModel> notStartedToDoItemsArrayList = dbHelper.notStartedToDoItemsArrayList();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, notStartedToDoItemsArrayList);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);
                                        break;
                                    case 3:     // Display all items
                                        ArrayList<ToDoItemModel> allToDoItemsArrayList = dbHelper.getAllToDoItemsAsArrayList();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, allToDoItemsArrayList);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);
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
                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByPriority = dbHelper.toDoItemsArrayListSortByPriority();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByPriority);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);
                                        break;
                                    case 1:     // Sort by deadline
                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByDeadline = dbHelper.toDoItemsArrayListSortByDeadline();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByDeadline);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);
                                        break;
                                    case 2:     // Sort by time added
                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByTimeAdded = dbHelper.toDoItemsArrayListSortByTimeAdded();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByTimeAdded);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);
                                        break;
                                    case 3:     // Sort by title
                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByTitle = dbHelper.toDoItemsArrayListSortByTitle();
                                        generalToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByTitle);
                                        generalToDoItemDisplayFragment.setListAdapter(generalToDoItemsCustomAdapter);
                                        break;
                                }
                            }
                        });
                (displayBuilder.create()).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_TODOITEMS_ARRAYLIST_IDENTIFIER, toDoItemsArrayList);
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