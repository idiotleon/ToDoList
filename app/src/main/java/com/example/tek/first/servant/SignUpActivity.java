package com.example.tek.first.servant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    private EditText username_editText;
    private EditText password_editText;
    private EditText password_com_editText;
    private EditText age_editText;
    private EditText street_editText;
    private EditText city_editText;
    private EditText country_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        username_editText = (EditText) findViewById(R.id.username_editText_signUp);
        password_editText = (EditText) findViewById(R.id.password_editText_signUp);
        password_com_editText = (EditText) findViewById(R.id.password_com_editText_signUp);
        age_editText = (EditText) findViewById(R.id.age_editText_signUp);
        street_editText = (EditText) findViewById(R.id.street_editText_signUp);
        city_editText = (EditText) findViewById(R.id.city_editText_signUp);
        country_editText = (EditText) findViewById(R.id.country_editText_signUp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public void signUp(View view) {
        finish();
    }
}
