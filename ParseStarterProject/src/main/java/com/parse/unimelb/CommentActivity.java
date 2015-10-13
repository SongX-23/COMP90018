package com.parse.unimelb;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

/* Comment Activity implements the comment functionality of the app. */
public class CommentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        EditText comments = (EditText) findViewById(R.id.commentEditText);
        String commentText = comments.getText().toString();
        Button cancel = (Button) findViewById(R.id.cancelButton);
        //cancel button to go back
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        //implement the comment requesting here. Although it always return HTTP400, we are still
        //requesting/.
        Button comment = (Button) findViewById(R.id.commentButton);
        comment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //request url
                String url = getResources().getString(R.string.instagram_api_url)
                        + getResources().getString(R.string.instagram_api_media_method)
                        + "id/comments?access_token="
                        + getResources().getString(R.string.instagram_access_token);

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonResponse = response.getJSONObject("meta");
                                    String errorMsg = jsonResponse.getString("error_message");
                                    String errorType = jsonResponse.getString("error_type");
                                    System.out.println("Comments: " + errorMsg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "This request requires scope=comments, but this access token" +
                                                " is not authorized with this scope. Sorry!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                Volley.newRequestQueue(getApplicationContext()).add(postRequest);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
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
