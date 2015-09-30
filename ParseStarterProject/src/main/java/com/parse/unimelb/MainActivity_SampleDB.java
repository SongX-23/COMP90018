package com.muaranauli.imageapplication;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.muaranauli.ImageAdapter;
import com.muaranauli.db.DBImageAdapter;
import com.muaranauli.db.ImageInstance;
import com.muaranauli.utils.Info;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static ImageView imageView;
    private static Button button;
    private int curImagIndex;
    private int[] images={
            R.drawable.btn_star_big_on_pressed,
            R.drawable.btn_star_big_off_pressed,
            R.drawable.btn_star_big_off_disable_focused,
            R.drawable.btn_star_big_on_disable_focused
    };
    private int[] moreImage={
            R.drawable.firststeps_select,
            R.drawable.overview_select,
            R.drawable.webresources_select,
            R.drawable.whatsnew_select
    };

    //DBAdapter
    DBImageAdapter dbImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonClick();
        openDB();
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

    //Load DB
    public void loadDB(View view) {
        openDB();
        Info.notify(this,"Loading DB");
        Resources resources=this.getResources();
        ImageInstance imageInstance;

        for (int resID : moreImage) {
            //Info.notify(this,"ResID "+resID);
            imageInstance=new ImageInstance(
                    getResources().getDrawable(resID).toString(),
                    BitmapFactory.decodeResource(
                            resources,
                            resID
                    )
            );
            //Info.notify(this, "Created " + imageInstance.getImageName());
            dbImageAdapter.insertImage(imageInstance);
            Info.notify(this, "inserted " + imageInstance.getImageName());
        }
    }

    // Opend DB
    private void openDB() {
        dbImageAdapter =new DBImageAdapter(this);
        dbImageAdapter.open();
    }

    //Change Icon
    public void buttonClick() {
        imageView=(ImageView) findViewById(R.id.iconView);
        button=(Button) findViewById(R.id.btnChangePic);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curImagIndex++;
                        curImagIndex = curImagIndex % images.length;
                        imageView.setImageResource(images[curImagIndex]);
                        Info.notify(
                                getBaseContext(),
                                getResources().getDrawable(images[curImagIndex]).toString()
                        );
                    }
                }

        );
    }

    //populate list view
    public  void populateListView (View v) {
        ArrayList<ImageInstance> arrOfImages=new ArrayList<>();
        ImageAdapter adapter=new ImageAdapter(this,arrOfImages);
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Info.notify(this, "Listing DB");
        ArrayList<ImageInstance> dbImages=ImageInstance.getImages(dbImageAdapter.getAllRows());
        if(dbImages!=null) adapter.addAll(dbImages);



    }
}
