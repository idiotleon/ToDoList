package com.example.tek.first.servant.todolist.fragment.display.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tek.first.servant.R;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.ToDoItem;

import java.util.ArrayList;

public class CompletedToDoItemsListViewCustomAdatper extends BaseAdapter {

    private static String LOG_TAG = CompletedToDoItemsListViewCustomAdatper.class.getSimpleName();

    private Context context;
    private ArrayList<ToDoItem> completedToDoListItemsArrayList;

    public CompletedToDoItemsListViewCustomAdatper(Context context, ArrayList<ToDoItem> completedToDoListItemsArrayList) {
        this.context = context;
        this.completedToDoListItemsArrayList = completedToDoListItemsArrayList;
    }

    @Override
    public int getCount() {
        return completedToDoListItemsArrayList.size();
    }

    @Override
    public ToDoItem getItem(int position) {
        return completedToDoListItemsArrayList.get(position);
    }

    public int getPriority(int position) {
        return (getItem(position)).getPriority();
    }

    public GeneralHelper.CompletionStatus getCompletionStatus(int position) {
        return getItem(position).getCompletionStatus();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.todoitem_custom_textview, null);
        TextView textViewPriority = (TextView) rootView.findViewById(R.id.todolist_row_numbering);
        Log.v(LOG_TAG, "getPriority(position): " + Integer.toString(getPriority(position)));
        TextView textViewDeadline = (TextView) rootView.findViewById(R.id.todolist_row_deadline);
        TextView textViewTitle = (TextView) rootView.findViewById(R.id.todolist_row_title);

        textViewPriority.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));
        textViewDeadline.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));
        textViewTitle.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));
        textViewDeadline.setText("Completed");
        textViewTitle.setText(completedToDoListItemsArrayList.get(position).getTitle());
        textViewPriority.setText(Integer.toString(position + 1));

        return rootView;
    }
}
