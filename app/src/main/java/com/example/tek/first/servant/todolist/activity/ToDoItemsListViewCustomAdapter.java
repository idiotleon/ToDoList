package com.example.tek.first.servant.todolist.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tek.first.servant.R;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.ToDoItemModel;
import com.example.tek.first.servant.todolist.helper.GeneralHelper.CompletionStatus;

import java.util.ArrayList;

public class ToDoItemsListViewCustomAdapter extends BaseAdapter {

    private static String LOG_TAG = ToDoItemsListViewCustomAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<ToDoItemModel> toDoListItemsArrayList;

    public ToDoItemsListViewCustomAdapter(Context context, ArrayList<ToDoItemModel> toDoListItemsArrayList) {
        this.context = context;
        this.toDoListItemsArrayList = toDoListItemsArrayList;
    }

    @Override
    public int getCount() {
        return toDoListItemsArrayList.size();
    }

    @Override
    public ToDoItemModel getItem(int position) {
        return toDoListItemsArrayList.get(position);
    }

    public int getPriority(int position) {
        return (getItem(position)).getPriority();
    }

    public CompletionStatus getCompletionStatus(int position) {
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
        TextView textViewPriority = (TextView) rootView.findViewById(R.id.todolist_row_priority);
        Log.v(LOG_TAG, "getPriority(position): " + Integer.toString(getPriority(position)));
        // todo: textview with custom color based on different priority level
        TextView textViewDeadline = (TextView) rootView.findViewById(R.id.todolist_row_deadline);
        TextView textViewTitle = (TextView) rootView.findViewById(R.id.todolist_row_title);

        if (CompletionStatus.COMPLETED == getCompletionStatus(position)) {
//            rootView.setBackgroundColor(context.getResources().getColor(R.color.todolist_mark_as_complete));
            textViewPriority.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));
            textViewDeadline.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));
            textViewTitle.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));
            textViewDeadline.setText("Completed");
        } else {
            Long deadline = toDoListItemsArrayList.get(position).getToDoItemDeadline();
            if (deadline > 0L) {
                textViewDeadline.setText(GeneralHelper.parseDateAndTimeToString(deadline));
            }
            textViewPriority.setBackgroundColor(GeneralConstants.PRIORITY_LEVEL_COLOR[getPriority(position) - 1]);
        }

        textViewTitle.setText(toDoListItemsArrayList.get(position).getTitle());
        textViewPriority.setText(Integer.toString(position + 1));

        return rootView;
    }

}
