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
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.unimelb.R;

public class SignupActivity extends AppCompatActivity {
    Button signupButton;
    EditText first_name, last_name, email, password;
    String firstname, lastname, email_str, psw, username;
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
        signupButton = (Button) this.findViewById(R.id.signupButton);
        first_name = (EditText) this.findViewById(R.id.firstNameEditText);
        last_name = (EditText) this.findViewById(R.id.lastNameEditText);
        email = (EditText) this.findViewById(R.id.emailEditText);
        password = (EditText) this.findViewById(R.id.passwordEditText);

        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
        public void onClick(View v){
                //retreive the text input
                firstname = first_name.getText().toString();
                lastname = last_name.getText().toString();
                email_str = email.getText().toString();
                psw = password.getText().toString();
                // create user

                if (email_str.equals("") && password.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();

                }else{
                    ParseUser user = new ParseUser();
                    user.setUsername(email_str);
                    user.setPassword(psw);
                    user.setEmail(email_str);
                    user.put("FullName", (firstname + " " + lastname));
                    user.put("Bio", "Blank");
                    user.put("Phone","Blank");
                    user.put("City", "Blank");
                    user.put("Gender", "Blank");
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                // Show a simple Toast message upon successful registration
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Signed up, logging in now.",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                Toast.makeText(getApplicationContext(),
                                        "Sign up Error", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });

                }

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
        if (id == R.id.action_log_out) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
