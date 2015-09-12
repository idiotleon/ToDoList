package com.leontheprofessional.todolist.fragment.display.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leontheprofessional.todolist.R;
import com.leontheprofessional.todolist.model.SimpleToDoItem;

import java.util.ArrayList;

public class SimpleToDoItemsListViewCustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SimpleToDoItem> simpleToDoItemsArrayListModel;

    public SimpleToDoItemsListViewCustomAdapter(Context context, ArrayList<SimpleToDoItem> simpleToDoItemsArrayListModel) {
        this.context = context;
        this.simpleToDoItemsArrayListModel = simpleToDoItemsArrayListModel;
    }

    @Override
    public int getCount() {
        return simpleToDoItemsArrayListModel.size();
    }

    @Override
    public SimpleToDoItem getItem(int position) {
        return simpleToDoItemsArrayListModel.get(position);
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
        View rootView = inflater.inflate(R.layout.simple_new_simple_todoitem, null);
        TextView textViewNumbering = (TextView) rootView.findViewById(R.id.textview_number_simple_new_todoitem);
        TextView textViewTitle = (TextView) rootView.findViewById(R.id.textview_title_simple_new_todoitem);
        textViewNumbering.setText(Integer.toString(position + 1));
        textViewTitle.setText(getTitle(position));

        return rootView;
    }
}
