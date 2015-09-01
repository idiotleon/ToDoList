package com.example.tek.first.servant.todolist.fragment.display;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.tek.first.servant.todolist.activity.ToDoItemDetailsActivity;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.ToDoItem;

import java.util.ArrayList;

public class IncompleteItemsDisplayFragment extends ListFragment {

    private static final String LOG_TAG = IncompleteItemsDisplayFragment.class.getSimpleName();

    private DatabaseHelper dbHelper;
    private ArrayList<ToDoItem> toDoItemsArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getActivity());
        toDoItemsArrayList = new ArrayList<>();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        toDoItemsArrayList = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(GeneralHelper.CompletionStatus.INCOMPLETED);

        Log.v(LOG_TAG, "Position: " + position);

        Intent intent = new Intent(getActivity(), ToDoItemDetailsActivity.class);
        intent.putExtra(GeneralConstants.TO_DO_ITEM_IDENTIFIER, toDoItemsArrayList.get(position));
        startActivity(intent);
    }
}
