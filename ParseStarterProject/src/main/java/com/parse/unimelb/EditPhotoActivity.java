package com.parse.unimelb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.parse.unimelb.Helper.BitmapStore;
import com.parse.unimelb.Helper.ImageProcessing;
import com.parse.unimelb.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditPhotoActivity extends Activity {
    private ImageView imageView;
    private Bitmap currentBitmap;
    private Bitmap newBitmap;

    private Button btnColorFilter = null;
    private Button btnSaturation = null;
    private Button btnEngrave = null;
    private Button btnCrop = null;
    private SeekBar seekBarContrast = null;
    private SeekBar seekBarBrightness = null;
    private TextView textview_contrast = null;
    private TextView textview_brightness = null;
    private Button btnNext = null;

    static int progress_contrast = 0;
    static int progress_brightness = 0;

    //TODO: add crop function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        imageView = (ImageView)findViewById(R.id.imageview_edit);
        Intent intent = getIntent();
        if (intent != null) {
            currentBitmap = BitmapStore.getBitmap();
            imageView.setImageBitmap(currentBitmap);
            newBitmap = currentBitmap;
        }

        // Filters
        btnColorFilter = (Button) findViewById(R.id.button_color);
        btnSaturation = (Button) findViewById(R.id.button_saturation);
        btnEngrave = (Button) findViewById(R.id.button_engrave);
        btnCrop = (Button) findViewById(R.id.button_crop);


        btnColorFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBitmap = ImageProcessing.doColorFilter(currentBitmap,
                        0.5, 0.5, 0.5);
                imageView.setImageBitmap(newBitmap);
            }
        });

        btnSaturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBitmap = ImageProcessing
                        .applySaturationFilter(currentBitmap, 1);
                imageView.setImageBitmap(newBitmap);
            }
        });

        btnEngrave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBitmap = ImageProcessing.engrave(currentBitmap);
                imageView.setImageBitmap(newBitmap);
            }
        });

        // Contrast & Brightness
        seekBarContrast = (SeekBar) findViewById(R.id.seekbar_contrast);
        seekBarBrightness = (SeekBar) findViewById(R.id.seekbar_brightness);
        textview_contrast = (TextView) findViewById(R.id.text_contrast);
        textview_brightness = (TextView) findViewById(R.id.textview_brightness);
        btnNext = (Button) findViewById(R.id.button_next);


        // Listener for seekbar object
        //Bug-fixed need to figure out how to change contrast of the picture on the fly
        //TODO: Bug-fix 2nd time return to editing, it will throw an error
        seekBarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textview_contrast.setText("Contrast: " + String.valueOf(progress_contrast));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (newBitmap != null) newBitmap.recycle();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress_contrast = seekBarBrightness.getProgress();
                newBitmap = ImageProcessing.changeBitmapContrastBrightness(currentBitmap,
                        (float) progress_contrast, (float) progress_brightness);
                imageView.setImageBitmap(newBitmap);
            }
        });

        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textview_brightness.setText("Brightness: " + String.valueOf(progress_brightness));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (newBitmap != null) newBitmap.recycle();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress_brightness = (seekBarBrightness.getProgress()) - 255;
                newBitmap = ImageProcessing.changeBitmapContrastBrightness(currentBitmap,
                        (float) progress_contrast, (float) progress_brightness);
                imageView.setImageBitmap(newBitmap);
            }
        });

        //listen for next action button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // save the modified picture
                File storagePath = new File(Environment.getExternalStorageDirectory()
                        + "/DCIM/100ANDRO/");
                storagePath.mkdirs();

                File myImage = new File(storagePath, Long.toString(System.currentTimeMillis())
                        + "_mod.jpg");
                try {
                    FileOutputStream out = new FileOutputStream(myImage);
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    out.flush();
                    out.close();
                } catch(FileNotFoundException e) {
                    Log.d("In Saving File", e + "");
                } catch(IOException e) {
                    Log.d("In Saving File", e + "");
                }

                // Pass the new image to the next edit view
                Intent intent = new Intent();
                intent.putExtra("post_img", myImage.toString());
                intent.setClass(EditPhotoActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        newBitmap.recycle();
//        currentBitmap.recycle();
    }
}

