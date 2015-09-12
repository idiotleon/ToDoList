package com.leontheprofessional.todolist.fragment.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.leontheprofessional.todolist.R;
import com.leontheprofessional.todolist.helper.GeneralConstants;
import com.leontheprofessional.todolist.helper.GeneralHelper;
import com.leontheprofessional.todolist.model.Date;
import com.leontheprofessional.todolist.model.DetailedToDoItem;
import com.leontheprofessional.todolist.model.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailedNewToDoItemDialogFragment extends DialogFragment {

    private static final String LOG_TAG = DetailedNewToDoItemDialogFragment.class.getSimpleName();

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
    private Long toDoItemDeadline = 0L;
    private int priority = 1;
    private int category = 0;
    private GeneralHelper.CompletionStatus completionStatus = GeneralHelper.CompletionStatus.INCOMPLETE;

    private Time timeSet;
    private Date dateSet;

    private OnNewItemAddedListener onNewItemAddedListener;

    public interface OnNewItemAddedListener {
        void onNewItemAdded(DetailedToDoItem todoItem);
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
        View rootView = inflater.inflate(R.layout.todoitem_create_dialog, null);
        editTextTitle = (EditText) rootView.findViewById(R.id.edittext_title_todolistitem);
        editTextTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    GeneralHelper.hideSoftKeyBoard(getActivity(), v);
                    return true;
                } else {
                    return false;
                }
            }
        });

        editTextDescription = (EditText) rootView.findViewById(R.id.edittext_description_todolistitem);
        editTextDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    GeneralHelper.hideSoftKeyBoard(getActivity(), v);
                    return true;
                } else {
                    return false;
                }
            }
        });

        btnClear = (Button) rootView.findViewById(R.id.btn_clear_dialog);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GeneralHelper.hideSoftKeyBoard(getActivity(), v);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.clear_confirmation_dialog_text)
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
                (builder.create()).show();
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
                if (position < GeneralConstants.PRIORITY_LEVEL_OPTIONS) {
                    priority = position + 1;
                } else
                    priority = 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                priority = 1;
            }
        });

        btnTimePicker = (Button) rootView.findViewById(R.id.todolist_btn_selecttime);
        btnDatePicker = (Button) rootView.findViewById(R.id.todolist_btn_selectdate);
        btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm_dialog);

//        btnTimePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment dialogFragment = new TimePickerDialogFragment();
//                dialogFragment.show(getFragmentManager(), "timePicker");
//            }
//        });


        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralHelper.hideSoftKeyBoard(getActivity(), v);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        btnTimePicker.setText(hourOfDay + " : " + minute);
                        timeSet = new Time(hourOfDay, minute);
                    }
                }, GeneralHelper.getCurrentTimeAndDate(GeneralConstants.HOUR_OF_DAY_OPTION),
                        GeneralHelper.getCurrentTimeAndDate(GeneralConstants.MINUTE_OF_HOUR_OPTION),
                        false);
                timePickerDialog.show();
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralHelper.hideSoftKeyBoard(getActivity(), v);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        btnDatePicker.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        dateSet = new Date(monthOfYear, dayOfMonth, year);
                    }
                }, GeneralHelper.getCurrentTimeAndDate(GeneralConstants.YEAR_OPTION),
                        GeneralHelper.getCurrentTimeAndDate(GeneralConstants.MONTH_OF_YEAR_OPTION),
                        GeneralHelper.getCurrentTimeAndDate(GeneralConstants.DAY_OF_MONTH_OPTION));
                datePickerDialog.show();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralHelper.hideSoftKeyBoard(getActivity(), v);
                title = editTextTitle.getText().toString();
                if (title != null && title.length() > 0) {
                    descriptionText = editTextDescription.getText().toString();
                    currentTimeStamp =
                            Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
                    Log.v(LOG_TAG, "currentTimeStamp: " + currentTimeStamp);
                    category = 0;
                    toDoItemDeadline = GeneralHelper.parseDateAndTimeModelToLong(dateSet, timeSet);

                    DetailedToDoItem toDoListItem = new DetailedToDoItem(title, priority, descriptionText, currentTimeStamp, toDoItemDeadline, category, completionStatus);
                    onNewItemAddedListener.onNewItemAdded(toDoListItem);
                    GeneralHelper.hideSoftKeyBoard(getActivity(), v);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Please input a title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
