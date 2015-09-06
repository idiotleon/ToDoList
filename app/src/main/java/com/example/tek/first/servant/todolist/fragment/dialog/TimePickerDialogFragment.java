package com.example.tek.first.servant.todolist.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;


import com.example.tek.first.servant.todolist.helper.GeneralConstants;
import com.example.tek.first.servant.todolist.model.Time;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static String LOG_TAG = TimePickerDialogFragment.class.getSimpleName();

    private Bundle bundle;
    private Time timeSelected;
    private int hour;
    private int minute;

    public interface TimePickerDialogListener {
        void onTimeSelected(Time timeSelected);
    }

    private TimePickerDialogListener timePickerDialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            timePickerDialogListener = (TimePickerDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TimePickerDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bundle = getArguments();
        if (bundle != null) {
            hour = bundle.getInt(GeneralConstants.HOUR_IDENTIFIER);
            minute = bundle.getInt(GeneralConstants.MINUTE_IDENTIFIER);
        } else {
            final Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }
        Log.v(LOG_TAG, "onCreateDialog() method, TimePickerDialogFragment executed");

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        timeSelected = new Time(hourOfDay, minute);
        Log.v(LOG_TAG, "onTimeSet() method executed");
        Log.v(LOG_TAG, "hourOfDay, onTimeSet(): " + hourOfDay);
        Log.v(LOG_TAG, "minute, onTimeSet(): " + minute);

        timePickerDialogListener.onTimeSelected(timeSelected);
    }
}
