package com.leontheprofessional.todolist.fragment.display.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leontheprofessional.todolist.R;
import com.leontheprofessional.todolist.model.SimpleToDoItem;

import java.util.ArrayList;

/**
 * Created by Leon on 9/29/2015.
 */
public class CompletedSimpleToDoItemsListViewCustomAdatper extends BaseAdapter {

    private static final String LOG_TAG = CompletedSimpleToDoItemsListViewCustomAdatper.class.getSimpleName();

    private Context context;
    private ArrayList<SimpleToDoItem> completedSimpleToDoItemArrayList;

    public CompletedSimpleToDoItemsListViewCustomAdatper(Context context, ArrayList<SimpleToDoItem> completedSimpleToDoItemArrayList) {
        this.context = context;
        this.completedSimpleToDoItemArrayList = completedSimpleToDoItemArrayList;
    }

    @Override
    public int getCount() {
        return completedSimpleToDoItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return completedSimpleToDoItemArrayList.get(position);
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
        textViewPriority.setText(Integer.toString(completedSimpleToDoItemArrayList.get(position).getPriority()));
        textViewPriority.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));

        TextView textViewDeadline = (TextView) rootView.findViewById(R.id.todolist_row_deadline);
        textViewDeadline.setText(context.getResources().getString(R.string.completed));
        textViewDeadline.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));


        TextView textViewTitle = (TextView) rootView.findViewById(R.id.todolist_row_title);
        textViewTitle.setText(completedSimpleToDoItemArrayList.get(position).getTitle());
        textViewTitle.setBackground(context.getResources().getDrawable(R.drawable.completed_todoitem_textview));

        return rootView;
    }
}
