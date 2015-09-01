package com.example.tek.first.servant.todolist.fragment.display;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.tek.first.servant.todolist.activity.ToDoItemDetailsActivity;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.model.SimpleToDoItem;

import java.util.ArrayList;

public class SimpleToDoItemsDisplayFragment extends ListFragment {

    private ArrayList<SimpleToDoItem> simpleToDoItemArrayList;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleToDoItemArrayList = new ArrayList<>();
        dbHelper = new DatabaseHelper(getActivity());

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        simpleToDoItemArrayList = dbHelper.getSortedSimpleToDoItemsAsArrayList();

        Intent simpleToDoItemIntent = new Intent(getActivity(), ToDoItemDetailsActivity.class);
        simpleToDoItemIntent.putExtra(GeneralConstants.SIMPLE_TO_DO_ITEM_IDENTIFIER, simpleToDoItemArrayList);
        startActivity(simpleToDoItemIntent);
    }
}
