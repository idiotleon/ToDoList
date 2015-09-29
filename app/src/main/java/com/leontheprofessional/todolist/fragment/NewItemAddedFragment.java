package com.leontheprofessional.todolist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.leontheprofessional.todolist.R;
import com.leontheprofessional.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import com.leontheprofessional.todolist.helper.GeneralHelper;
import com.leontheprofessional.todolist.model.SimpleToDoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NewItemAddedFragment extends Fragment {

    private static final String LOG_TAG = NewItemAddedFragment.class.getSimpleName();

    private ImageButton btnAddDetailedToDoItem;
    private ImageButton btnAddSimpleToDoItemBySpeech;
    private EditText editTextInput;
    private OnNewSimpleItemAddedListener onNewSimpleItemAddedListener;

    private final int REQUEST_CODE_SPEECH_INPUT = 777;

    public interface OnNewSimpleItemAddedListener {
        void onNewSimpleItemAdded(SimpleToDoItem newSimpleToDoItemModel);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onNewSimpleItemAddedListener = (OnNewSimpleItemAddedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnNewSimpleItemAddedListener.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.todolist_newitem, null);

        btnAddDetailedToDoItem = (ImageButton) rootView.findViewById(R.id.imagebutton_create_detailed_todoitem);
        btnAddDetailedToDoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailedNewToDoItemDialogFragment detailedToDoListItemDialog = new DetailedNewToDoItemDialogFragment();
                detailedToDoListItemDialog.show(getFragmentManager(), "DetailedNewToDoItemDialogFragment");
            }
        });

        editTextInput = (EditText) rootView.findViewById(R.id.todoiteminput_edittext_activitytodolist);
        editTextInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        String newSimpleItemTitle = editTextInput.getText().toString();
                        if (newSimpleItemTitle != null && newSimpleItemTitle.length() > 0) {
                            Long currentTime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
                            SimpleToDoItem newSimpleToDoItem = new SimpleToDoItem(newSimpleItemTitle, currentTime);
                            onNewSimpleItemAddedListener.onNewSimpleItemAdded(newSimpleToDoItem);
                            editTextInput.setText("");
                            GeneralHelper.hideSoftKeyBoard(getActivity(), v);
                        } else {
                            Toast.makeText(getActivity(), "Please input a title", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                return false;
            }
        });

/*        editTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GeneralHelper.hideSoftKeyBoard(getActivity(), editTextInput);
            }
        });*/

        btnAddSimpleToDoItemBySpeech = (ImageButton) rootView.findViewById(R.id.imagebutton_create_simple_todoitem_by_speech);
        btnAddSimpleToDoItemBySpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        return rootView;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v(LOG_TAG, "onActivityResult() executed.");

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK || null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.v(LOG_TAG, "result.get(0): " + result.get(0));
                    editTextInput.setText(result.get(0));
                }
                break;
            }
        }
    }
}
