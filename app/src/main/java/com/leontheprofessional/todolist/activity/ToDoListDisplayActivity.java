package com.leontheprofessional.todolist.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.leontheprofessional.todolist.R;
import com.leontheprofessional.todolist.fragment.display.adapter.CompletedToDoItemsListViewCustomAdatper;
import com.leontheprofessional.todolist.fragment.display.adapter.SimpleToDoItemsListViewCustomAdapter;
import com.leontheprofessional.todolist.fragment.display.adapter.ToDoItemsListViewCustomAdapter;
import com.leontheprofessional.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import com.leontheprofessional.todolist.fragment.NewItemAddedFragment;
import com.leontheprofessional.todolist.fragment.display.CompletedDetailedItemsDisplayFragment;
import com.leontheprofessional.todolist.fragment.display.IncompleteDetailedItemsDisplayFragment;
import com.leontheprofessional.todolist.fragment.display.IncompleteSimpleToDoItemsDisplayFragment;
import com.leontheprofessional.todolist.helper.GeneralConstants;
import com.leontheprofessional.todolist.helper.GeneralHelper;
import com.leontheprofessional.todolist.model.Date;
import com.leontheprofessional.todolist.model.SimpleToDoItem;
import com.leontheprofessional.todolist.model.Time;

import com.leontheprofessional.todolist.model.DetailedToDoItem;
import com.leontheprofessional.todolist.provider.DatabaseHelper;

import java.util.ArrayList;

public class ToDoListDisplayActivity extends AppCompatActivity
        implements DetailedNewToDoItemDialogFragment.OnNewItemAddedListener,
        NewItemAddedFragment.OnNewSimpleItemAddedListener,
        GeneralHelper.ToDoItemStatusChangeListener {

    public static String LOG_TAG = ToDoListDisplayActivity.class.getSimpleName();

    private Date dateSelected = null;
    private Time timeSelected = null;

    private ArrayList<DetailedToDoItem> incompleteDetailedToDoItemsArrayList;
    private ArrayList<DetailedToDoItem> completedToDoItemsArrayList;
    private ArrayList<SimpleToDoItem> incompleteSimpleToDoItemsArrayList;

    private ToDoItemsListViewCustomAdapter incompleteToDoItemsCustomAdapter;
    private CompletedToDoItemsListViewCustomAdatper completedToDoItemsCustomAdapter;
    private SimpleToDoItemsListViewCustomAdapter simpleToDoItemsListViewCustomAdapter;

    private NewItemAddedFragment newItemAddedFragment;
    private IncompleteDetailedItemsDisplayFragment incompleteToDoItemDisplayListFragment;
    private CompletedDetailedItemsDisplayFragment completedToDoItemDisplayListFragment;
    private IncompleteSimpleToDoItemsDisplayFragment simpleToDoItemsDisplayFragment;

    private static int counterOfSortByPrioritySelectedTimes = 0;
    private static int counterOfSortByDeadlineSelectedTimes = 0;
    private static int counterOfSortByTimeAddedSelectedTimes = 0;
    private static int counterOfSortByTitleSelectedTimes = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_main_activity);

        incompleteDetailedToDoItemsArrayList = new ArrayList<>();
        completedToDoItemsArrayList = new ArrayList<>();
        incompleteSimpleToDoItemsArrayList = new ArrayList<>();

        FragmentManager fragmentManager = getFragmentManager();
        newItemAddedFragment
                = (NewItemAddedFragment) fragmentManager.findFragmentById(R.id.todolist_newitem);
        incompleteToDoItemDisplayListFragment
                = (IncompleteDetailedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_incomplete_items);
        completedToDoItemDisplayListFragment
                = (CompletedDetailedItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_completed_items);
        simpleToDoItemsDisplayFragment
                = (IncompleteSimpleToDoItemsDisplayFragment) fragmentManager.findFragmentById(R.id.todolist_displayfragment_simple_todoitems);

        refreshPage(savedInstanceState);

/*        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

            ArrayList<CharSequence> a1 = new ArrayList<>();
            a1.add("Item1");
            a1.add("Item2");
            ArrayAdapter<CharSequence> dropDownAdapter =
                    new ArrayAdapter<CharSequence>(this, android.R.layout.simple_expandable_list_item_1, a1);
            actionBar.setListNavigationCallbacks(dropDownAdapter, new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                    Log.v(LOG_TAG, "onNavigationItemSelected, ToDoListDisplayActivity: " + itemId);
                    return true;
                }
            });*/

/*             actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setDisplayShowTitleEnabled(false);
           ActionBar.Tab incompleteToDoItemsTab = actionBar.newTab();
            incompleteToDoItemsTab.setText("Incomplete ToDoItems").setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }
            });

            ActionBar.Tab completeToDoItemsTab = actionBar.newTab().setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }
            });
            completeToDoItemsTab.setText("Complete ToDoItems");

            ActionBar.Tab simpleToDoItemsTab = actionBar.newTab().setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

                }
            });
            simpleToDoItemsTab.setText("Simple ToDoItems");

            actionBar.addTab(incompleteToDoItemsTab);
            actionBar.addTab(completeToDoItemsTab);
            actionBar.addTab(simpleToDoItemsTab);
        } else {
            Log.e(LOG_TAG, "actionBar is null, ToDoListDisplayActivity");
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todolist_display_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.display_menu_activitytodolist:
                Log.v(LOG_TAG, "Displaying-Menu Selected");
                AlertDialog.Builder sortBuilder = new AlertDialog.Builder(ToDoListDisplayActivity.this);
                sortBuilder.setTitle("Please select what to be displayed: ")
                        .setItems(R.array.menu_display_standard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:     // Display only incomplete items, sorted in SharedPreference mode
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, incompleteToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, completedToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, simpleToDoItemsDisplayFragment);
                                        break;
                                    case 1:     // Display only completed items
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, completedToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, incompleteToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, simpleToDoItemsDisplayFragment);
                                        break;
                                    case 2: // Display all simple todoitems
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, simpleToDoItemsDisplayFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, completedToDoItemDisplayListFragment);
                                        GeneralHelper.hideFragment(ToDoListDisplayActivity.this, incompleteToDoItemDisplayListFragment);
                                        break;
                                    default:     // Display all items
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, completedToDoItemDisplayListFragment);
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, incompleteToDoItemDisplayListFragment);
                                        GeneralHelper.showFragment(ToDoListDisplayActivity.this, simpleToDoItemsDisplayFragment);
                                        break;
                                }
                            }
                        });
                (sortBuilder.create()).show();
                break;
            case R.id.sortby_menu_activitytodolist:
                Log.v(LOG_TAG, "Sorting-Menu Selected");
                AlertDialog.Builder displayBuilder = new AlertDialog.Builder(ToDoListDisplayActivity.this);
                displayBuilder.setTitle("Please select a sorting standard: ")
                        .setItems(R.array.menu_sort_standard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:     // Sort by priority
                                        setSortingPreference(DatabaseHelper.SORT_BY_PRIORITY, counterOfSortByPrioritySelectedTimes);
                                        counterOfSortByPrioritySelectedTimes++;
                                        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
                                        break;
                                    case 1:     // Sort by deadline
                                        setSortingPreference(DatabaseHelper.SORT_BY_DEADLINE, counterOfSortByDeadlineSelectedTimes);
                                        counterOfSortByDeadlineSelectedTimes++;
                                        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
                                        break;
                                    case 2:     // Sort by time added
                                        setSortingPreference(DatabaseHelper.SORT_BY_TIME_ADDED, counterOfSortByTimeAddedSelectedTimes);
                                        counterOfSortByTimeAddedSelectedTimes++;
                                        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
                                        break;
                                    case 3:     // Sort by title
                                        setSortingPreference(DatabaseHelper.SORT_BY_TITLE, counterOfSortByTitleSelectedTimes);
                                        counterOfSortByTitleSelectedTimes++;
                                        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
                                        break;
                                }
                            }
                        });
                (displayBuilder.create()).show();
                break;
            case R.id.preference_menu_activitytodolist:
                Intent intent = new Intent(this, ToDoListPreferenceActivity.class);
                startActivityForResult(intent, GeneralConstants.SHOW_PREFERENCES);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSortingPreference(String sortingPreference, int counter) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ToDoListDisplayActivity.this);

        sharedPreferences.edit().putString(GeneralConstants.TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER, sortingPreference).commit();

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
                GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_TODOITEMS_ARRAYLIST_IDENTIFIER, incompleteDetailedToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_COMPLETED_TODOITEMS_ARRAYLIST_IDENTIFIER, completedToDoItemsArrayList);
        outState.putParcelableArrayList(
                GeneralConstants.SAVEINSTANCESTATE_SIMPLE_TODOITEM_IDENTIFIER, incompleteSimpleToDoItemsArrayList);
    }

    @Override
    public void onNewItemAdded(DetailedToDoItem newDetailedToDoItem) {
        Toast.makeText(ToDoListDisplayActivity.this, "A new DetailedToDoItem added", Toast.LENGTH_SHORT).show();
        GeneralHelper.insertToDoListItem(ToDoListDisplayActivity.this, newDetailedToDoItem);
        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
    }

    @Override
    public void onNewSimpleItemAdded(SimpleToDoItem newSimpleToDoItem) {
        GeneralHelper.insertToDoListItem(ToDoListDisplayActivity.this, newSimpleToDoItem);
        Log.v(LOG_TAG, "A new simple ToDoItem added.");
        Toast.makeText(ToDoListDisplayActivity.this, "A new simple ToDoItem added", Toast.LENGTH_SHORT).show();
        incompleteSimpleToDoItemsArrayList.add(0, newSimpleToDoItem);
        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
    }

    @Override
    public void onStatusChanged() {
        Log.v(LOG_TAG, "onStatusChanged(), ToDoListDisplayActivity executed.");
        refreshPage(GeneralConstants.NULL_SAVED_INSTANCE_STATE);
    }

    private void refreshPage(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            incompleteDetailedToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_TODOITEMS_ARRAYLIST_IDENTIFIER);
            completedToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_COMPLETED_TODOITEMS_ARRAYLIST_IDENTIFIER);
            incompleteSimpleToDoItemsArrayList = savedInstanceState.getParcelableArrayList(GeneralConstants.SAVEINSTANCESTATE_SIMPLE_TODOITEM_IDENTIFIER);
        } else {
            incompleteDetailedToDoItemsArrayList = GeneralHelper.getSortedIncompleteDetailedToDoItemsAsArrayList(ToDoListDisplayActivity.this);
            completedToDoItemsArrayList = GeneralHelper.getSortedCompletedToDoItemsAsArrayList(ToDoListDisplayActivity.this);
            incompleteSimpleToDoItemsArrayList = GeneralHelper.getSortedIncompleteSimpleToDoItemsAsArrayList(ToDoListDisplayActivity.this);
        }

        incompleteToDoItemsCustomAdapter = new ToDoItemsListViewCustomAdapter(ToDoListDisplayActivity.this, incompleteDetailedToDoItemsArrayList);
        if (!incompleteDetailedToDoItemsArrayList.isEmpty()) {
            incompleteToDoItemDisplayListFragment.setListAdapter(incompleteToDoItemsCustomAdapter);
        } else {
            incompleteToDoItemDisplayListFragment.setListAdapter(null);
        }

        completedToDoItemsCustomAdapter = new CompletedToDoItemsListViewCustomAdatper(ToDoListDisplayActivity.this, completedToDoItemsArrayList);
        if (!completedToDoItemsArrayList.isEmpty()) {
            completedToDoItemDisplayListFragment.setListAdapter(completedToDoItemsCustomAdapter);
        } else {
            completedToDoItemDisplayListFragment.setListAdapter(null);
        }

        simpleToDoItemsListViewCustomAdapter = new SimpleToDoItemsListViewCustomAdapter(ToDoListDisplayActivity.this, incompleteSimpleToDoItemsArrayList);
        if (!incompleteSimpleToDoItemsArrayList.isEmpty()) {
            simpleToDoItemsDisplayFragment.setListAdapter(simpleToDoItemsListViewCustomAdapter);
        } else {
            simpleToDoItemsDisplayFragment.setListAdapter(null);
        }
    }
}