package com.parse.unimelb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by songxue on 29/09/2015.
 * This class is providing login service for users
 */
public class LoginActivity extends AppCompatActivity {
    Button loginbutton;
    EditText email_str;
    EditText psw;
    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link to Sign up
        setContentView(R.layout.activity_login);
        final ParseUser currentUser = ParseUser.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        String flag = "";
        if (bundle != null) {
            flag = bundle.getString("currentUser");
        }
        System.out.println("Logout?" + currentUser.get("FullName"));
        //if no current logged in user then login user
        if (currentUser.get("FullName") != null && !flag.equals("Logout")) {
            TextView signupTextView = (TextView) this.findViewById(R.id.signupTextView);
            signupTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            });
            Intent indent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(indent);
        } else {
            System.out.println("Logout?true");
            TextView signupTextView = (TextView) this.findViewById(R.id.signupTextView);
            signupTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            });
            // login
            loginbutton = (Button) this.findViewById(R.id.loginButton);
            email_str = (EditText) this.findViewById(R.id.emailEditText);
            psw = (EditText) this.findViewById(R.id.passwordEditText);

            loginbutton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    // Retrieve the text entered from the EditText
                    email = email_str.getText().toString();
                    password = psw.getText().toString();
                    // Send data to Parse.com for verification
                    ParseUser.logInInBackground(email, password,
                            new LogInCallback() {
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null) {
                                        // If user exist and authenticated, send user to Welcome.class
                                        Toast.makeText(getApplicationContext(),
                                                "Successfully Logged in",
                                                Toast.LENGTH_LONG).show();
                                        Intent indent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(indent);
                                        finish();
                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "No such user exist, please signup",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
