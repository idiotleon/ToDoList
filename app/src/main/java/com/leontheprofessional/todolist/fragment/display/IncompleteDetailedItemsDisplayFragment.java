package com.leontheprofessional.todolist.fragment.display;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.leontheprofessional.todolist.R;
import com.leontheprofessional.todolist.activity.ToDoItemDetailsActivity;
import com.leontheprofessional.todolist.helper.GeneralConstants;
import com.leontheprofessional.todolist.helper.GeneralHelper;
import com.leontheprofessional.todolist.model.DetailedToDoItem;
import com.leontheprofessional.todolist.helper.GeneralHelper.ToDoItemStatusChangeListener;

import java.util.ArrayList;

public class IncompleteDetailedItemsDisplayFragment extends ListFragment {

    private static final String LOG_TAG = IncompleteDetailedItemsDisplayFragment.class.getSimpleName();

    private ArrayList<DetailedToDoItem> toDoItemsArrayList;

    private ToDoItemStatusChangeListener toDoItemStatusChangeListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            toDoItemStatusChangeListener = (ToDoItemStatusChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement ToDoItemStatusChangeListener.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        toDoItemsArrayList = new ArrayList<>();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(toDoItemsArrayList, "getSortedIncompleteToDoItemsAsArrayList(INCOMPLETE)");
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                toDoItemsArrayList
                        = GeneralHelper.getSortedIncompleteDetailedToDoItemsAsArrayList(getActivity());
                Log.v(LOG_TAG, "onItemLongClick(), IncompleteDetailedItemsDisplayFragment, executed");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Do you want to: ")
                        .setItems(R.array.incomplete_todoitem_operation, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DetailedToDoItem toDoItem = toDoItemsArrayList.get(position);
                                switch (which) {
                                    case 0:
                                        toDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.COMPLETED);
                                        GeneralHelper.updateToDoListItem(getActivity(), toDoItem);
                                        Toast.makeText(getActivity(), "DetailedToDoItem: " + toDoItem.getTitle() + " is marked as complete.", Toast.LENGTH_SHORT).show();
                                        toDoItemStatusChangeListener.onStatusChanged();
                                        break;
                                    case 1:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(R.string.delete_confirmation).setPositiveButton(R.string.todolist_confirm_text, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                GeneralHelper.deleteToDoItem(getActivity(), toDoItem);
                                                toDoItemStatusChangeListener.onStatusChanged();
                                            }
                                        }).setNegativeButton(R.string.todolist_cancel_text, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        (builder.create()).show();
                                        Toast.makeText(getActivity(), "DetailedToDoItem: " + toDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                (builder.create()).show();
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        toDoItemsArrayList = GeneralHelper.getSortedIncompleteDetailedToDoItemsAsArrayList(getActivity());
        Log.v(LOG_TAG, "Position, onListItemClick(), IncompleteDetailedToDoItemsDisplayFragment: " + position);
        Intent intent = new Intent(getActivity(), ToDoItemDetailsActivity.class);
        intent.putExtra(GeneralConstants.DETAILED_TO_DO_ITEM_IDENTIFIER, toDoItemsArrayList.get(position));
        startActivity(intent);
    }
}
