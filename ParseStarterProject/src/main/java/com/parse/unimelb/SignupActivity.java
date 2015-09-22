package com.parse.unimelb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.unimelb.R;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //get the login text
        TextView loginTextView = (TextView) this.findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //signup button
        Button signupButton = (Button) this.findViewById(R.id.signupButton);
        EditText first_name = (EditText) this.findViewById(R.id.firstNameEditText);
        EditText last_name = (EditText) this.findViewById(R.id.lastNameEditText);
        EditText email = (EditText) this.findViewById(R.id.emailEditText);
        EditText password = (EditText) this.findViewById(R.id.passwordEditText);
        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
        public void onClick(View v){

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
}
