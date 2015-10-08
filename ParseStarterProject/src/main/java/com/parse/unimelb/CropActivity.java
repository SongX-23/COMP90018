package com.parse.unimelb;

import android.app.Activity;
import android.content.Intent;
import android.database.CrossProcessCursor;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.unimelb.Helper.BitmapStore;
import com.parse.unimelb.R;

public class CropActivity extends Activity {

    private Bitmap rawBitmap = null;
    private Bitmap cropBitmap = null;
    private CropView cropview = null;

    private Button btnOK = null;
    private Button btnReturn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        cropview = (CropView) findViewById(R.id.crop_view);
        Intent intent = getIntent();
        if (intent != null) {
            rawBitmap = BitmapStore.getBitmap();
            cropview.setImageBitmap(rawBitmap);
        }

        btnOK = (Button) findViewById(R.id.button_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cropBitmap = cropview.getCroppedImage();
                    cropview.setImageBitmap(cropBitmap);
                } catch (Exception e){
                    Toast.makeText(getBaseContext(), "Please choose another area",
                            Toast.LENGTH_LONG);
                }
            }
        });

        btnReturn = (Button) findViewById(R.id.button_return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cropBitmap != null) {
                    BitmapStore.setBitmap(cropBitmap);
                    Intent intent = new Intent(CropActivity.this, EditPhotoActivity.class);
                    startActivity(intent);
                } else {
                    CropActivity.this.finish();
                }
            }
        });
    }


}
