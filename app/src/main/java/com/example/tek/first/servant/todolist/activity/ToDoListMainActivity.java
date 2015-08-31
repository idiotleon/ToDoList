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
import com.example.tek.first.servant.todolist.fragment.dialog.DatePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import com.example.tek.first.servant.todolist.fragment.NewItemAddedFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.TimePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.ToDoListDisplayFragment;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.DateModel;
import com.example.tek.first.servant.todolist.model.TimeModel;
import com.example.tek.first.servant.todolist.model.ToDoItemModel;

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
    private ToDoListCustomAdapter toDoListCustomAdapter;
    private ToDoListDisplayFragment toDoListDisplayFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_main_activity);

        toDoItemsArrayList = new ArrayList<>();
        if (savedInstanceState != null) {
            toDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_TODOITEMS_ARRAYLIST_IDENTIFIER);
        } else {
            dbHelper = new DatabaseHelper(ToDoListMainActivity.this);
            toDoItemsArrayList = dbHelper.getAllToDoItemsAsArrayList();
        }

        FragmentManager fragmentManager = getFragmentManager();
        NewItemAddedFragment newItemAddedFragment =
                (NewItemAddedFragment) fragmentManager.findFragmentById(R.id.todolist_newitem);

        toDoListDisplayFragment
                = (ToDoListDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment);

        toDoListCustomAdapter = new ToDoListCustomAdapter(ToDoListMainActivity.this, toDoItemsArrayList);

        toDoListDisplayFragment.setListAdapter(toDoListCustomAdapter);
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
                                        toDoListCustomAdapter = new ToDoListCustomAdapter(ToDoListMainActivity.this, incompleteToDoItemsArrayList);
                                        toDoListDisplayFragment.setListAdapter(toDoListCustomAdapter);
                                        break;
                                    case 1:     // Display completed items
                                        ArrayList<ToDoItemModel> completeToDoItemsArrayList = dbHelper.completeToDoItemsArrayList();
                                        toDoListCustomAdapter = new ToDoListCustomAdapter(ToDoListMainActivity.this, completeToDoItemsArrayList);
                                        toDoListDisplayFragment.setListAdapter(toDoListCustomAdapter);
                                        break;
                                    case 2:     // Display not started items
                                        ArrayList<ToDoItemModel> notStartedToDoItemsArrayList = dbHelper.notStartedToDoItemsArrayList();
                                        toDoListCustomAdapter = new ToDoListCustomAdapter(ToDoListMainActivity.this, notStartedToDoItemsArrayList);
                                        toDoListDisplayFragment.setListAdapter(toDoListCustomAdapter);
                                        break;
                                    case 3:     // Display all items
                                        ArrayList<ToDoItemModel> allToDoItemsArrayList = dbHelper.getAllToDoItemsAsArrayList();
                                        toDoListCustomAdapter = new ToDoListCustomAdapter(ToDoListMainActivity.this, allToDoItemsArrayList);
                                        toDoListDisplayFragment.setListAdapter(toDoListCustomAdapter);
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
                                        toDoListCustomAdapter = new ToDoListCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByPriority);
                                        toDoListDisplayFragment.setListAdapter(toDoListCustomAdapter);
                                        break;
                                    case 1:     // Sort by deadline
                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByDeadline = dbHelper.toDoItemsArrayListSortByDeadline();
                                        toDoListCustomAdapter = new ToDoListCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByDeadline);
                                        toDoListDisplayFragment.setListAdapter(toDoListCustomAdapter);
                                        break;
                                    case 2:     // Sort by time added
                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByTimeAdded = dbHelper.toDoItemsArrayListSortByTimeAdded();
                                        toDoListCustomAdapter = new ToDoListCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByTimeAdded);
                                        toDoListDisplayFragment.setListAdapter(toDoListCustomAdapter);
                                        break;
                                    case 3:     // Sort by title
                                        ArrayList<ToDoItemModel> toDoItemModelArrayListSortByTitle = dbHelper.toDoItemsArrayListSortByTitle();
                                        toDoListCustomAdapter = new ToDoListCustomAdapter(ToDoListMainActivity.this, toDoItemModelArrayListSortByTitle);
                                        toDoListDisplayFragment.setListAdapter(toDoListCustomAdapter);
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
        toDoListCustomAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNewSimpleItemAdded(ToDoItemModel newSimpleItem) {
        dbHelper.insertToDoListItem(newSimpleItem);
        toDoItemsArrayList.add(0, newSimpleItem);
        toDoListCustomAdapter.notifyDataSetChanged();
    }
}