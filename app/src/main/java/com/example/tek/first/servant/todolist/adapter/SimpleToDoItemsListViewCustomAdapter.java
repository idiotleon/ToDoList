package com.example.tek.first.servant.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tek.first.servant.R;
import com.example.tek.first.servant.todolist.model.SimpleToDoItem;

import java.util.ArrayList;

public class SimpleToDoItemsListViewCustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SimpleToDoItem> simpleToDoItemsArrayList;

    public SimpleToDoItemsListViewCustomAdapter(Context context, ArrayList<SimpleToDoItem> simpleToDoItemsArrayList) {
        this.context = context;
        this.simpleToDoItemsArrayList = simpleToDoItemsArrayList;
    }

    @Override
    public int getCount() {
        return simpleToDoItemsArrayList.size();
    }

    @Override
    public SimpleToDoItem getItem(int position) {
        return simpleToDoItemsArrayList.get(position);
    }

    public String getTitle(int position) {
        return getItem(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.simple_new_todoitem, null);
        TextView textViewNumbering = (TextView) rootView.findViewById(R.id.textview_number_simple_new_todoitem);
        TextView textViewTitle = (TextView) rootView.findViewById(R.id.textview_title_simple_new_todoitem);
        textViewNumbering.setText(Integer.toString(position + 1));
        textViewTitle.setText(getTitle(position));

        return rootView;
    }
}
