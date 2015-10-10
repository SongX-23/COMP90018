package com.parse.unimelb;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.unimelb.R;

public class EditProfileActivity extends AppCompatActivity {
    Button updateProfile, cancelUpdate;
    Spinner genderSpinner, citySpinner;
    EditText fullNameText, bioText, emailText, phoneText;
    private String gender, fullName, email, phone, city, bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final ParseUser currentUser = ParseUser.getCurrentUser();
        updateProfile = (Button) this.findViewById(R.id.updateButton);
        cancelUpdate = (Button) this.findViewById(R.id.cancelButton);
        genderSpinner = (Spinner) this.findViewById(R.id.genderSpinner);
        fullNameText = (EditText) this.findViewById(R.id.fullNameEditText);
        emailText = (EditText) this.findViewById(R.id.emailEditText);
        phoneText = (EditText) this.findViewById(R.id.phoneEditText);
        bioText = (EditText) this.findViewById(R.id.bioEditText);
        citySpinner = (Spinner) this.findViewById(R.id.citySpinner);
        // fetch the basic user info
        emailText.setText(currentUser.getEmail().toString());
        fullNameText.setText(currentUser.get("FullName").toString());
        phoneText.setText(currentUser.get("Phone").toString());
        bioText.setText(currentUser.get("Bio").toString());
        String cityValue = currentUser.get("City").toString();
        String compareValue = currentUser.get("Gender").toString();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_arrays, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, R.array.city_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            genderSpinner.setSelection(spinnerPosition);
        }
        //set the spinner
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int pos, long id) {
                int loc;
                loc = pos;
                gender = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg1) {
            }
        });
        citySpinner.setAdapter(cityAdapter);
        if (!cityValue.equals(null)) {
            int spinnerPosition = cityAdapter.getPosition(cityValue);
            citySpinner.setSelection(spinnerPosition);
        }
        //set the spinner
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int pos, long id) {
                int loc;
                loc = pos;
                city = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg1) {
            }
        });
        //cancel button
        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0){
                onBackPressed();
            }
        });
        // update button
        updateProfile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //update user info
                fullName = fullNameText.getText().toString();
                email = emailText.getText().toString();
                bio = bioText.getText().toString();
                phone = phoneText.getText().toString();
                //city = cityText.getText().toString();
                if (fullName==null || email==null){
                    Toast.makeText(getApplicationContext(),
                            "Full name and Email can't be blank.",
                            Toast.LENGTH_LONG).show();
                }else{
                    currentUser.put("FullName", fullName);
                    currentUser.setUsername(email);
                    currentUser.put("Bio", bio);
                    currentUser.put("Phone", phone);
                    currentUser.put("City", city);
                    currentUser.put("Gender", gender);
                    currentUser.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(),
                                        "Profile successfully updated",
                                        Toast.LENGTH_LONG).show();
                                onBackPressed();
                            } else {
                                // ParseException
                                Toast.makeText(getApplicationContext(),
                                        "Network failure",
                                        Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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
