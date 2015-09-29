package com.example.tek.first.servant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends AppCompatActivity {

    private EditText username_editText;
    private EditText password_editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username_editText = (EditText) findViewById(R.id.username_editText_main);
        password_editText = (EditText) findViewById(R.id.password_editText_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {

        if (username_editText.getText().toString().trim().equals("")) {
            Crouton.makeText(MainActivity.this, "Username is empty.", Style.ALERT).show();
            return;
        }

        if (password_editText.getText().toString().trim().equals("")) {
            Crouton.makeText(MainActivity.this, "Password is empty.", Style.ALERT).show();
            return;
        }

//        Cursor cursor = getActivity().getContentResolver().query(UserContract.UserEntry.CONTENT_URI,
//                null,
//                UserProvider.sUsernameSelection,
//                new String[]{username_editText.getText().toString().trim(),password_editText.getText().toString().trim()},
//                null);
//
//        if( cursor.moveToFirst() ){
//            SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            SharedPreferences.Editor editor = mSharedPreference.edit();
//            editor.putString("username",username_editText.getText().toString().trim());
//            editor.commit();
//
//            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
//            startActivity(intent);
//        } else {
//            Crouton.makeText(getActivity(), "wrong username/password", Style.ALERT).show();
//        }

    }

    public void signUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
