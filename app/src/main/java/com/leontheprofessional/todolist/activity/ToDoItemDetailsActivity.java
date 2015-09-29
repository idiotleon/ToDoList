package com.leontheprofessional.todolist.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leontheprofessional.todolist.R;
import com.leontheprofessional.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import com.leontheprofessional.todolist.helper.GeneralConstants;
import com.leontheprofessional.todolist.helper.GeneralHelper;
import com.leontheprofessional.todolist.model.DetailedToDoItem;
import com.leontheprofessional.todolist.model.SimpleToDoItem;

public class ToDoItemDetailsActivity extends AppCompatActivity
        implements DetailedNewToDoItemDialogFragment.OnNewItemAddedListener {

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

    private DetailedToDoItem detailedToDoItem;
    private SimpleToDoItem simpleToDoItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_todoitem_details_activity);

        Intent intent = getIntent();
        if (intent != null) {
            detailedToDoItem = intent.getExtras().getParcelable(GeneralConstants.DETAILED_TO_DO_ITEM_IDENTIFIER);
            simpleToDoItem = intent.getExtras().getParcelable(GeneralConstants.SIMPLE_TO_DO_ITEM_IDENTIFIER);
        } else {
            detailedToDoItem = savedInstanceState.getParcelable(GeneralConstants.SAVEINSTANCESTATE_TODOITEM_IDENTIFIER);
            simpleToDoItem = savedInstanceState.getParcelable(GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_SIMPLE_TODOITEM_IDENTIFIER);
        }
//        Log.v(LOG_TAG, "onCreate(), intent received, ToDoItemDetailsActivity: " + GeneralHelper.formatToString(detailedToDoItem.getToDoItemDeadline()));
        if (detailedToDoItem != null) {
            priority = detailedToDoItem.getPriority();
            title = detailedToDoItem.getTitle();
            deadline = detailedToDoItem.getToDoItemDeadline();
            dateAndTimeCreated = detailedToDoItem.getItemCreatedDateAndTime();
        }

        if (simpleToDoItem != null) {
            title = simpleToDoItem.getTitle();
            dateAndTimeCreated = simpleToDoItem.getItemCreatedDateAndTime();
        }

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
                                detailedToDoItem.setDetailDescription(description);
                                GeneralHelper.updateToDoListItem(ToDoItemDetailsActivity.this, detailedToDoItem);
                                Toast.makeText(ToDoItemDetailsActivity.this, "Description of DetailedToDoItem: " + title + " updated", Toast.LENGTH_SHORT).show();
                                Log.v(LOG_TAG, "Description of DetailedToDoItem updated");
                                refreshToDoItemsDetailsPage(detailedToDoItem);
                            }
                        }).setNegativeButton(R.string.todolist_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                (builder.create()).show();
                return true;
            }
        });

        dateAndTimeCreatedTextView = (TextView) findViewById(R.id.textview_dateandtimecreated_todoitem_detailactivity);

        btnMarkAsComplete = (Button) findViewById(R.id.btn_markascomplete_detailactivity);
        btnDelete = (Button) findViewById(R.id.btn_delete_detailactivity);

        if (detailedToDoItem != null)
            refreshToDoItemsDetailsPage(detailedToDoItem);

        if (simpleToDoItem != null)
            refreshSimpleToDoItemPage(simpleToDoItem);

        /**
         * Styling the ActionBar
         */
        ActionBar actionBar = getSupportActionBar();
        if (deadline > 1) {
            actionBar.setTitle("Deadline: " + GeneralHelper.parseDateAndTimeToString(deadline));
        } else {
            actionBar.setTitle("Added on: " + GeneralHelper.parseDateAndTimeToString(dateAndTimeCreated));
        }
        actionBar.setSubtitle("Details");
//        String[] hexColorCode = getResources().getStringArray(R.array.priority_level_color_hex_code);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor(GeneralConstants.PRIORITY_LEVEL_COLOR_HEX_CODE[priority]));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnMarkAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailedToDoItem != null) {
                    detailedToDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.COMPLETED);
                    GeneralHelper.updateToDoListItem(ToDoItemDetailsActivity.this, detailedToDoItem);
                    Log.v(LOG_TAG, "DetailedToDoItem: " + detailedToDoItem.getTitle() + " is marked as complete");
                }
                if (simpleToDoItem != null) {
                    simpleToDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.COMPLETED);
                    GeneralHelper.updateToDoListItem(ToDoItemDetailsActivity.this, simpleToDoItem);
                    Log.v(LOG_TAG, "SimpleToDoItem: " + simpleToDoItem.getTitle() + " is marked as complete");
                }
                Intent intent = new Intent(ToDoItemDetailsActivity.this, ToDoListDisplayActivity.class);
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
//                                        detailedToDoItem.setCompleteStatusCode(GeneralConstants.TODOLISTITEM_COMPLETION_STATUS_INCOMPLETE);
                                        detailedToDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.INCOMPLETE);
                                        Log.v(LOG_TAG, "DetailedToDoItem: " + detailedToDoItem.getTitle() + " is marked as incomplete");
                                        break;
                                    case 2:
//                                        detailedToDoItem.setCompleteStatusCode(GeneralConstants.TODOLISTITEM_COMPLETION_STATUS_COMPLETE);
                                        detailedToDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.COMPLETED);
                                        Log.v(LOG_TAG, "DetailedToDoItem: " + detailedToDoItem.getTitle() + " is marked as complete");
                                        break;
                                }
                                GeneralHelper.updateToDoListItem(ToDoItemDetailsActivity.this, detailedToDoItem);
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
                alertDialogBuilder.setMessage(R.string.delete_confirmation + detailedToDoItem.getTitle());
                alertDialogBuilder.setPositiveButton(R.string.todolist_confirm_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (detailedToDoItem != null) {
                            GeneralHelper.deleteToDoItem(ToDoItemDetailsActivity.this, detailedToDoItem);
                            Toast.makeText(ToDoItemDetailsActivity.this, "DetailedToDoItem: " + detailedToDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                        }
                        if (simpleToDoItem != null) {
                            GeneralHelper.deleteToDoItem(ToDoItemDetailsActivity.this, detailedToDoItem);
                            Toast.makeText(ToDoItemDetailsActivity.this, "SimpleToDoItem: " + simpleToDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(ToDoItemDetailsActivity.this, ToDoListDisplayActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.todolist_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todolist_detail_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                Intent intent = new Intent(this, ToDoListDisplayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshToDoItemsDetailsPage(DetailedToDoItem toDoItem) {
        // todo: check completionStatus of detailedToDoItem, depending on which to improve UI of details activity
        titleTextView.setText(toDoItem.getTitle());
        Long toDoItemDeadline = toDoItem.getToDoItemDeadline();
        if (toDoItemDeadline > 0) {
            String deadline = GeneralHelper.parseDateAndTimeToString(toDoItemDeadline);
            Log.v(LOG_TAG, "deadline, refreshToDoItemsDetailsPage(), ToDoItemDetailsActivity: " + deadline);
        } else {
        }
        descriptionTextView.setText(GeneralHelper.formatToString(toDoItem.getDetailDescription()));
        int priority = toDoItem.getPriority();
        int colorId = GeneralConstants.PRIORITY_LEVEL_COLOR[priority - 1];
        Log.v(LOG_TAG, "colorId, ToDoItemDetailsActivity: " + colorId);

        String timeAdded = GeneralHelper.parseDateAndTimeToString(toDoItem.getItemCreatedDateAndTime());
        Log.v(LOG_TAG, "timeAdded, refreshToDoItemsDetailsPage(), ToDoItemDetailsActivity: " + timeAdded);
        dateAndTimeCreatedTextView.setText("Time created: " + timeAdded);
    }

    private void refreshSimpleToDoItemPage(SimpleToDoItem simpleToDoItemModel) {
        titleTextView.setText(detailedToDoItem.getTitle());
        String timeAdded = GeneralHelper.parseDateAndTimeToString(detailedToDoItem.getItemCreatedDateAndTime());
        Log.v(LOG_TAG, "timeAdded, refreshToDoItemsDetailsPage(), ToDoItemDetailsActivity: " + timeAdded);
        dateAndTimeCreatedTextView.setText("Time added: " + timeAdded);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GeneralConstants.SAVEINSTANCESTATE_INCOMPLETE_SIMPLE_TODOITEM_IDENTIFIER, simpleToDoItem);
        outState.putParcelable(GeneralConstants.SAVEINSTANCESTATE_TODOITEM_IDENTIFIER, detailedToDoItem);
    }

    @Override
    public void onNewItemAdded(DetailedToDoItem todoItem) {
    }
}
