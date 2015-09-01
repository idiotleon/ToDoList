package com.example.tek.first.servant.todolist.fragment.display;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.tek.first.servant.todolist.activity.ToDoItemDetailsActivity;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.ToDoItem;

import java.util.ArrayList;

public class CompletedDetailedItemsDisplayFragment extends ListFragment {

    private DatabaseHelper dbHelper;
    private ArrayList<ToDoItem> incompleteToDoItemsArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getActivity());
        incompleteToDoItemsArrayList = new ArrayList<>();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        incompleteToDoItemsArrayList = dbHelper.getSortedToDoItemsInDifferentCompletionStatusAsArrayList(GeneralHelper.CompletionStatus.COMPLETED);

        Intent intent = new Intent(getActivity(), ToDoItemDetailsActivity.class);
        intent.putExtra(GeneralConstants.TO_DO_ITEM_IDENTIFIER, incompleteToDoItemsArrayList.get(position));
        startActivity(intent);
    }
}
