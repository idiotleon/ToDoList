package com.leontheprofessional.todolist.fragment.display.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leontheprofessional.todolist.R;
import com.leontheprofessional.todolist.helper.GeneralHelper;
import com.leontheprofessional.todolist.model.DetailedToDoItem;

import java.util.ArrayList;

public class CompletedDetailedToDoItemsListViewCustomAdatper extends BaseAdapter {

    private static String LOG_TAG = CompletedDetailedToDoItemsListViewCustomAdatper.class.getSimpleName();

    private Context context;
    private ArrayList<DetailedToDoItem> completedToDoListItemsArrayList;

    public CompletedDetailedToDoItemsListViewCustomAdatper(Context context, ArrayList<DetailedToDoItem> completedToDoListItemsArrayList) {
        this.context = context;
        this.completedToDoListItemsArrayList = completedToDoListItemsArrayList;
    }

    @Override
    public int getCount() {
        return completedToDoListItemsArrayList.size();
    }

    @Override
    public DetailedToDoItem getItem(int position) {
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
        textViewPriority.setText(Integer.toString(getItem(position).getPriority()));

        textViewTitle.setBackgroundColor(context.getResources().getColor(R.color.todolist_mark_as_complete));
        textViewTitle.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));
        textViewTitle.setText(completedToDoListItemsArrayList.get(position).getTitle());

        textViewDeadline.setText(context.getResources().getString(R.string.completed));
        textViewDeadline.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));

        return rootView;
    }
}
