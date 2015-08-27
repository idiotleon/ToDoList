package com.example.tek.first.servant.todolist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tek.first.servant.R;
import com.example.tek.first.servant.todolist.helper.DatabaseHelper;
import com.example.tek.first.servant.todolist.helper.GeneralHelper;
import com.example.tek.first.servant.todolist.model.ToDoItemModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailedNewToDoItemDialogFragment extends DialogFragment {

    private static final String LOG_TAG = DetailedNewToDoItemDialogFragment.class.getSimpleName();

    private DatabaseHelper dbHelper;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Spinner spinnerPriority;
    private Button btnDatePicker;
    private Button btnTimePicker;
    private Button btnConfirm;
    private Button btnClear;

    private String title = null;
    private String descriptionText = null;
    private Long currentTimeStamp = 0L;
    private Long itemDateAndTimeSet = 0L;
    private int priority = 1;
    private int category = 0;
    private GeneralHelper.CompletionStatus completionStatus = GeneralHelper.CompletionStatus.NOTSTARTED;

    private OnNewItemAddedListener onNewItemAddedListener;

    public interface OnNewItemAddedListener {
        void onNewItemAdded(ToDoItemModel todoItem);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onNewItemAddedListener = (OnNewItemAddedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.todolistitemdetail_dialog, null);
        editTextTitle = (EditText) rootView.findViewById(R.id.edittext_title_todolistitem);
        editTextDescription = (EditText) rootView.findViewById(R.id.edittext_description_todolistitem);

        btnClear = (Button) rootView.findViewById(R.id.btn_clear_dialog);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rootView = inflater.inflate(R.layout.todolistitemdetail_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(rootView).setTitle(R.string.clear_confirmation_dialog_text)
                        .setPositiveButton(R.string.todolist_clear_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editTextTitle.setText("");
                                editTextDescription.setText("");
                            }
                        }).setNegativeButton(R.string.todolist_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create();
            }
        });

        spinnerPriority = (Spinner) rootView.findViewById(R.id.spinner_priority_todolistitem);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.priority_level, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerAdapter);

        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = position + 1;
                Toast.makeText(getActivity(), "Priority: " + priority, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                priority = 1;
            }
        });

        dbHelper = new DatabaseHelper(getActivity());

        spinnerPriority = (Spinner) rootView.findViewById(R.id.spinner_priority_todolistitem);
        btnTimePicker = (Button) rootView.findViewById(R.id.todolist_btn_selecttime);
        btnDatePicker = (Button) rootView.findViewById(R.id.todolist_btn_selectdate);
        btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm_dialog);

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TimePickerDialogFragment();
                dialogFragment.show(getFragmentManager(), "timePicker");
            }
        });

//            if (timeSelected != null) {
//                btnTimePicker.setText(timeSelected.toString());
//            }

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerDialogFragment();
                dialogFragment.show(getFragmentManager(), "datePicker");
            }
        });

//            if (dateSelected != null) {
//                btnDatePicker.setText(dateSelected.toString());
//            }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editTextTitle.getText().toString();
                if (title != null && title.length() > 0) {
                    descriptionText = editTextDescription.getText().toString();
                    currentTimeStamp =
                            Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
                    Log.v(LOG_TAG, "currentTimeStamp: " + currentTimeStamp);
                    category = 0;

                    ToDoItemModel toDoListItem = new ToDoItemModel(title, priority, descriptionText, currentTimeStamp, itemDateAndTimeSet, category, completionStatus);
                    onNewItemAddedListener.onNewItemAdded(toDoListItem);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Please input a title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
