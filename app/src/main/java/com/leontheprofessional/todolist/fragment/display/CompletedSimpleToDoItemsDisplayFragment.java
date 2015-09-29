package com.leontheprofessional.todolist.fragment.display;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.leontheprofessional.todolist.model.SimpleToDoItem;

import java.util.ArrayList;

public class CompletedSimpleToDoItemsDisplayFragment extends ListFragment {

    private static final String LOG_TAG = CompletedSimpleToDoItemsDisplayFragment.class.getSimpleName();

    private ArrayList<SimpleToDoItem> completedSimpleToDoItemsArrayList;
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

        completedSimpleToDoItemsArrayList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                completedSimpleToDoItemsArrayList
                        = GeneralHelper.getSortedCompletedSimpleToDoItemsAsArrayList(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.do_you_want_to)).
                        setItems(R.array.complete_todoitem_operation, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final SimpleToDoItem simpleToDoItem = completedSimpleToDoItemsArrayList.get(position);
                                switch (which) {
                                    case 0:
                                        simpleToDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.INCOMPLETE);
                                        GeneralHelper.updateToDoListItem(getActivity(), simpleToDoItem);
                                        Toast.makeText(getActivity(), "DetailedToDoItem: " + simpleToDoItem.getTitle() + " is marked as complete.", Toast.LENGTH_SHORT).show();
                                        toDoItemStatusChangeListener.onStatusChanged();
                                        break;
                                    case 1:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(R.string.delete_confirmation).setPositiveButton(R.string.todolist_confirm_text, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                GeneralHelper.deleteToDoItem(getActivity(), simpleToDoItem);
                                                toDoItemStatusChangeListener.onStatusChanged();
                                                Toast.makeText(getActivity(), "DetailedToDoItem: " + simpleToDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                                            }
                                        }).setNegativeButton(R.string.todolist_cancel_text, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        (builder.create()).show();
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
        completedSimpleToDoItemsArrayList = GeneralHelper.getSortedCompletedSimpleToDoItemsAsArrayList(getActivity());

        Intent intent = new Intent(getActivity(), ToDoItemDetailsActivity.class);
        intent.putExtra(GeneralConstants.DETAILED_TO_DO_ITEM_IDENTIFIER, completedSimpleToDoItemsArrayList.get(position));
        startActivity(intent);
    }
}
