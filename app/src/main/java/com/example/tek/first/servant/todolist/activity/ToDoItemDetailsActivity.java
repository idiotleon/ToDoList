package com.example.tek.first.servant.todolist.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tek.first.servant.R;
import com.example.tek.first.servant.todolist.fragment.dialog.DatePickerDialogFragment;
import com.example.tek.first.servant.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.Date;
import com.example.tek.first.servant.todolist.model.SimpleToDoItem;
import com.example.tek.first.servant.todolist.model.ToDoItem;

public class ToDoItemDetailsActivity extends Activity
        implements DetailedNewToDoItemDialogFragment.OnNewItemAddedListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialogFragment.DatePickerDialogListener {

    private static String LOG_TAG = ToDoItemDetailsActivity.class.getSimpleName();

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dateAndTimeCreatedTextView;

    private Button btnMarkAsComplete;
    private Button btnDelete;

    private int priority;
    private String title;
    private String description;
    private long deadline;
    private long dateAndTimeCreated;

    private ToDoItem toDoItem;

    private SimpleToDoItem simpleToDoItem;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_todoitem_details_activity);

        Intent intent = getIntent();
        if (intent != null) {
            toDoItem = intent.getExtras().getParcelable(GeneralConstants.TO_DO_ITEM_IDENTIFIER);
        } else {
            toDoItem = savedInstanceState.getParcelable(GeneralConstants.SAVEINSTANCESTATE_TODOITEM_IDENTIFIER);
        }
        Log.v(LOG_TAG, "onCreate(), intent received, ToDoItemDetailsActivity: " + GeneralHelper.formatToString(toDoItem.getToDoItemDeadline()));
        if (toDoItem != null) {
            priority = toDoItem.getPriority();
            title = toDoItem.getTitle();
            deadline = toDoItem.getToDoItemDeadline();
            dateAndTimeCreated = toDoItem.getItemCreatedDateAndTime();
        }

        if (intent != null) {
            simpleToDoItem = intent.getExtras().getParcelable(GeneralConstants.SIMPLE_TO_DO_ITEM_IDENTIFIER);
        } else {
            simpleToDoItem = savedInstanceState.getParcelable(GeneralConstants.SAVEINSTANCESTATE_SIMPLE_TODOITEM_IDENTIFIER);
        }
        if (simpleToDoItem != null) {
            title = simpleToDoItem.getTitle();
            dateAndTimeCreated = simpleToDoItem.getItemCreatedDateAndTime();
        }

        dbHelper = new DatabaseHelper(ToDoItemDetailsActivity.this);

        titleTextView = (TextView) findViewById(R.id.textview_title_todoitem_detailactivity);
        descriptionTextView = (TextView) findViewById(R.id.textview_description_todoitem_detailactivity);
        descriptionTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.todoitem_description_edittext, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ToDoItemDetailsActivity.this);
                builder.setView(view).setTitle("Description:")
                        .setPositiveButton(R.string.todolist_confirm_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) view.findViewById(R.id.edittext_revise_description_todolistitem);
                                description = editText.getText().toString();
                                toDoItem.setDetailDescription(description);
                                dbHelper.updateToDoListItem(toDoItem);
                                Toast.makeText(ToDoItemDetailsActivity.this, "Description of ToDoItem: " + title + " updated", Toast.LENGTH_SHORT).show();
                                Log.v(LOG_TAG, "Description of ToDoItem updated");
                                refreshToDoItemDetailsPage(toDoItem);
                            }
                        }).setNegativeButton(R.string.todolist_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                (builder.create()).show();
                return true;
            }
        });

        dateAndTimeCreatedTextView = (TextView) findViewById(R.id.textview_dateandtimecreated_todoitem_detailactivity);

        btnMarkAsComplete = (Button) findViewById(R.id.btn_markascomplete_detailactivity);
        btnDelete = (Button) findViewById(R.id.btn_delete_detailactivity);

        if (toDoItem != null)
            refreshToDoItemDetailsPage(toDoItem);

        if (simpleToDoItem != null)
            refreshSimpleToDoItemPage(simpleToDoItem);

        /**
         * Styling the ActionBar
         */
        ActionBar actionBar = getActionBar();
        if (deadline > 0) {
            actionBar.setTitle("Deadline: " + GeneralHelper.parseDateAndTimeToString(deadline));
        } else {
            actionBar.setTitle("Added on: " + GeneralHelper.parseDateAndTimeToString(dateAndTimeCreated));
        }
        actionBar.setSubtitle("Details");
//        String[] hexColorCode = getResources().getStringArray(R.array.priority_level_color_hex_code);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor(GeneralConstants.PRIORITY_LEVEL_COLOR_HEX_CODE[priority - 1]));
        actionBar.setBackgroundDrawable(colorDrawable);

        btnMarkAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toDoItem != null) {
                    toDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.COMPLETED);
                    dbHelper.updateToDoListItem(toDoItem);
                    Log.v(LOG_TAG, "ToDoItem: " + toDoItem.getTitle() + " is marked as complete");
                }
                if (simpleToDoItem != null) {
                    simpleToDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.COMPLETED);
                    dbHelper.updateToDoListItem(simpleToDoItem);
                    Log.v(LOG_TAG, "SimpleToDoItem: " + simpleToDoItem.getTitle() + " is marked as complete");
                }
                Intent intent = new Intent(ToDoItemDetailsActivity.this, ToDoListMainActivity.class);
                startActivity(intent);
            }
        });

        btnMarkAsComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder markAsStatus = new AlertDialog.Builder(ToDoItemDetailsActivity.this);
                markAsStatus.setTitle("Please select a status to be marked")
                        .setItems(R.array.incomplete_todoitem_operation, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 1:
//                                        toDoItem.setCompleteStatusCode(GeneralConstants.TODOLISTITEM_COMPLETION_STATUS_INCOMPLETE);
                                        toDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.INCOMPLETE);
                                        Log.v(LOG_TAG, "ToDoItem: " + toDoItem.getTitle() + " is marked as incomplete");
                                        break;
                                    case 2:
//                                        toDoItem.setCompleteStatusCode(GeneralConstants.TODOLISTITEM_COMPLETION_STATUS_COMPLETE);
                                        toDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.COMPLETED);
                                        Log.v(LOG_TAG, "ToDoItem: " + toDoItem.getTitle() + " is marked as complete");
                                        break;
                                }
                                dbHelper.updateToDoListItem(toDoItem);
                            }
                        });
                (markAsStatus.create()).show();
                return false;
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ToDoItemDetailsActivity.this);
                alertDialogBuilder.setMessage(R.string.delete_confirmation + toDoItem.getTitle());
                alertDialogBuilder.setPositiveButton(R.string.todolist_confirm_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (toDoItem != null) {
                            dbHelper.deleteToDoItem(toDoItem);
                            Toast.makeText(ToDoItemDetailsActivity.this, "ToDoItem: " + toDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                        }
                        if (simpleToDoItem != null) {
                            dbHelper.deleteToDoItem(simpleToDoItem);
                            Toast.makeText(ToDoItemDetailsActivity.this, "SimpleToDoItem: " + simpleToDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(ToDoItemDetailsActivity.this, ToDoListMainActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.todolist_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void refreshToDoItemDetailsPage(ToDoItem toDoItem) {
        // todo: check completionStatus of toDoItem, depending on which to improve UI of details activity
        titleTextView.setText(toDoItem.getTitle());
        Long toDoItemDeadline = toDoItem.getToDoItemDeadline();
        if (toDoItemDeadline > 0) {
            String deadline = GeneralHelper.parseDateAndTimeToString(toDoItemDeadline);
            Log.v(LOG_TAG, "deadline, refreshToDoItemDetailsPage(), ToDoItemDetailsActivity: " + deadline);
        } else {
        }
        descriptionTextView.setText(GeneralHelper.formatToString(toDoItem.getDetailDescription()));
        int priority = toDoItem.getPriority();
        int colorId = GeneralConstants.PRIORITY_LEVEL_COLOR[priority - 1];
        Log.v(LOG_TAG, "colorId, ToDoItemDetailsActivity: " + colorId);

        String timeAdded = GeneralHelper.parseDateAndTimeToString(toDoItem.getItemCreatedDateAndTime());
        Log.v(LOG_TAG, "timeAdded, refreshToDoItemDetailsPage(), ToDoItemDetailsActivity: " + timeAdded);
        dateAndTimeCreatedTextView.setText("Time created: " + timeAdded);
    }

    private void refreshSimpleToDoItemPage(SimpleToDoItem simpleToDoItem) {
        titleTextView.setText(toDoItem.getTitle());
        String timeAdded = GeneralHelper.parseDateAndTimeToString(toDoItem.getItemCreatedDateAndTime());
        Log.v(LOG_TAG, "timeAdded, refreshToDoItemDetailsPage(), ToDoItemDetailsActivity: " + timeAdded);
        dateAndTimeCreatedTextView.setText("Time added: " + timeAdded);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GeneralConstants.SAVEINSTANCESTATE_SIMPLE_TODOITEM_IDENTIFIER, simpleToDoItem);
        outState.putParcelable(GeneralConstants.SAVEINSTANCESTATE_TODOITEM_IDENTIFIER, toDoItem);
    }

    @Override
    public void onDateSelected(Date dateSelected) {

    }

    @Override
    public void onNewItemAdded(ToDoItem todoItem) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
