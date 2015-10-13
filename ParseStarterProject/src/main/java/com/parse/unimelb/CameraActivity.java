package com.parse.unimelb;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.unimelb.Helper.BitmapStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/* Camera Activity implements the camera functionality of the app. */
public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "Camera";
    private Camera camera = null;
    private SurfaceView cameraSurfaceView = null;
    private SurfaceHolder cameraSurfaceHolder = null;
    private boolean previewing = false;
    RelativeLayout relativeLayout;
    private MediaScannerConnection mediaScanner = null;

    private Button btnCapture = null;
    private ToggleButton btnFlash = null;
    private ImageButton btnGallery = null;

    // TODO: Improve Camera Quality
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Button to Capture:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        relativeLayout=(RelativeLayout) findViewById(R.id.containerImg);
        relativeLayout.setDrawingCacheEnabled(true);

        cameraSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);

        cameraSurfaceHolder = cameraSurfaceView.getHolder();
        cameraSurfaceHolder.setKeepScreenOn(true);
        cameraSurfaceHolder.addCallback(this);

        btnCapture = (Button)findViewById(R.id.button1);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(cameraShutterCallback,
                        cameraPictureCallbackRaw,
                        cameraPictureCallbackJpeg);
            }
        });


        // Button to Control FlashLight
        btnFlash = (ToggleButton) findViewById(R.id.button_flash);
        // BugFixed - when return with isChecked state, there is a error message on screen
        // saying that camera isn't working properly, but does not affect the functioning
        btnFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    if (camera == null) {
                        camera = Camera.open();
                    }
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    camera.setParameters(parameters);
                    camera.startPreview();
                } else {
                    // The toggle is disabled
                    if (camera == null) {
                        camera = Camera.open();
                    }
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    camera.setParameters(parameters);
                    camera.startPreview();
                }
            }
        });

        // Button to Enter Gallery
        // Bug-fixed need to access all the pictures
        btnGallery = (ImageButton) findViewById(R.id.button_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 0;
                startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
            }
        });
    }


    Camera.ShutterCallback cameraShutterCallback = new Camera.ShutterCallback(){
        @Override
        public void onShutter(){

        }
    };

    Camera.PictureCallback cameraPictureCallbackRaw = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    Camera.PictureCallback cameraPictureCallbackJpeg = new Camera.PictureCallback(){
        @Override
        public void onPictureTaken(byte[] data, Camera camera){
            Bitmap cameraBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            int wid = cameraBitmap.getWidth();
            int hgt = cameraBitmap.getHeight();

            Bitmap newImage = Bitmap.createBitmap(wid, hgt, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newImage);
//            canvas.drawBitmap(cameraBitmap, 0f, 0f, null);
//            Drawable drawable = getResources().getDrawable(R.drawable.mark3);
//            drawable.setBounds(20, 30, drawable.getIntrinsicWidth() + 20,
//                    drawable.getIntrinsicHeight() + 30);
//            drawable.draw(canvas);


            File storagePath = new File(Environment.getExternalStorageDirectory()
                    + "/DCIM/100ANDRO/");
            storagePath.mkdirs();

            File myImage = new File(storagePath, Long.toString(System.currentTimeMillis()) + ".jpg");
            try {
                FileOutputStream out = new FileOutputStream(myImage);
                cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                out.flush();
                out.close();
            } catch(FileNotFoundException e) {
                Log.d("In Saving File", e + "");
            } catch(IOException e) {
                Log.d("In Saving File", e + "");
            }

//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
//                    Uri.parse("file://"+ Environment.getExternalStorageDirectory())));

            // parse the image to the gallery
//            MediaScannerConnection.scanFile(MainActivity.this, new String[] {
//                    myImage.getPath() }, new String[] { "image/jpeg" }, null);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myImage)));

            // Pass the new image to the next edit view
            BitmapStore.setBitmap(cameraBitmap);
            Intent intent = new Intent();
            intent.setClass(CameraActivity.this, EditPhotoActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(previewing) {
            camera.stopPreview();
            previewing = false;
        }
        try {
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
            Camera.Size previewSize = previewSizes.get(0);
            Camera.Size pictureSize = pictureSizes.get(0);

//            parameters.setPreviewSize(previewSize.width, previewSize.height);
//            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            parameters.setPreviewSize(640, 480);
            parameters.setPictureSize(640, 480);
            if(this.getResources().getConfiguration().orientation
                    != Configuration.ORIENTATION_LANDSCAPE) {
                camera.setDisplayOrientation(90);
            }

            camera.setParameters(parameters);
            camera.setPreviewDisplay(cameraSurfaceHolder);
            camera.startPreview();
            previewing = true;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        } catch(RuntimeException e) {
            Toast.makeText(getApplicationContext(), "Device Camera is " +
                    "not working properly, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }


    // Bug-fixed: out of memory error when go back and select photo 2nd time
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri selectedImage = data.getData();

            try{
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(selectedImage));
                BitmapStore.setBitmap(yourSelectedImage);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }

            // Pass the new image to the next edit view

            Intent intent = new Intent();
            intent.setClass(CameraActivity.this, EditPhotoActivity.class);
            startActivity(intent);
        }
    }
}
