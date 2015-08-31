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
import com.example.tek.first.servant.todolist.customview.textview.ToDoItemPriorityCustomView;

import java.util.ArrayList;

public class ToDoListCustomAdapter extends BaseAdapter {

    private static String LOG_TAG = ToDoListCustomAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<ToDoItemModel> toDoListItemsArrayList;

    public ToDoListCustomAdapter(Context context, ArrayList<ToDoItemModel> toDoListItemsArrayList) {
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.todoitem, null);
//        TextView textViewPriority = (TextView) rootView.findViewById(R.id.todolist_row_priority);
        // todo: textview with custom color based on different priority level
//        TextView textViewPriority = new ToDoItemPriorityCustomView(context, null, getPriority(position));
        TextView textViewPriority = (TextView) rootView.findViewById(R.id.todolist_row_priority);
        Log.v(LOG_TAG, "getPriority(position): " + Integer.toString(getPriority(position)));
        TextView textViewDeadline = (TextView) rootView.findViewById(R.id.todolist_row_deadline);
        TextView textViewTitle = (TextView) rootView.findViewById(R.id.todolist_row_title);

        textViewTitle.setText(toDoListItemsArrayList.get(position).getTitle());
        textViewPriority.setText(Integer.toString(position + 1));
        textViewPriority.setBackgroundColor(GeneralConstants.PRIORITY_LEVEL_COLOR[getPriority(position) - 1]);
        Long deadline = toDoListItemsArrayList.get(position).getToDoItemDeadline();
        if (deadline > 0L) {
            textViewDeadline.setText(GeneralHelper.parseDateAndTimeToString(deadline));
        }

        return rootView;
    }

}
