package com.parse.unimelb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.unimelb.R;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // get the login text
        TextView loginTextView = (TextView) this.findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //signup 
        Button signupButton = (Button) this.findViewById(R.id.signupButton);
        EditText first_name = (EditText) this.findViewById(R.id.firstNameEditText);
        EditText last_name = (EditText) this.findViewById(R.id.lastNameEditText);
        EditText email = (EditText) this.findViewById(R.id.emailEditText);
        EditText password = (EditText) this.findViewById(R.id.passwordEditText);
        String firstname = first_name.getText().toString();
        String lastname = last_name.getText().toString();
        final String email_str = email.getText().toString();
        final String psw = password.getText().toString();
        final String username = firstname + " " + lastname;
        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
        public void onClick(View v){
                // Dummy user
                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(psw);
                user.setEmail(email_str);

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.
                            Log.v("Sign up successfully.", e.toString());
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                        }
                    }
                });
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
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
