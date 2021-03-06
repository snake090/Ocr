package org.nunocky.ocrtest01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends Activity  {
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d("TAG", "Filed OpenCVLoader.initDebug()");
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";

    public String lang = "eng";

    private static final String TAG = "AndroidOCR.java";

    protected Button _button;
    protected Button _button1;
    protected Button _button2;
    protected Button _button3;
    protected EditText _field;
    protected String _path;
    protected boolean _taken;
    protected Bitmap bitmap;

    protected static final String PHOTO_TAKEN = "photo_taken";
    private ProgressDialog progressDialog;
    private Spinner selectSpinner;
    private TakeOverInfo takeOverInfo;
    private static final int REQUEST_GALLERY = 0;
    protected Intent imageIntent;
    private int mFlag = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String[] paths = new String[]{DATA_PATH, DATA_PATH + "tessdata/"};

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }

        }

        setContentView(R.layout.activity_main);

        _button = (Button) findViewById(R.id.button);
        _button.setOnClickListener(new ButtonClickHandler());
        _button1 = (Button) findViewById(R.id.button4);
        _button1.setOnClickListener(new ButtonClickHandler());
        _button2 = (Button) findViewById(R.id.button5);
        _button2.setOnClickListener(new ButtonClickHandler());
        _button3 = (Button) findViewById(R.id.button6);
        _button3.setOnClickListener(new ButtonClickHandler());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");

        _path = DATA_PATH + "/ocr.jpg";
        takeOverInfo = new TakeOverInfo();


        imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);

        ArrayAdapter<CharSequence> adapter2 =
                ArrayAdapter.createFromResource(this, R.array.sample_array2,
                        android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectSpinner = (Spinner) findViewById(R.id.spinner2);
        selectSpinner.setAdapter(adapter2);
        selectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                if (spinner.getSelectedItemPosition() == 0) {
                    takeOverInfo.setKind(true);
                    lang = "eng";

                    takeOverInfo.setLang("eng");
                } else {
                    takeOverInfo.setKind(false);
                    lang = "jpn";

                    takeOverInfo.setLang("jpn");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "OpenCV library found inside package. Using it!");
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

    }

    public class ButtonClickHandler implements View.OnClickListener {
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.button:
                    OnClickButton();
                    break;
                case R.id.button4:
                    OnClickButton4();
                    break;
                case R.id.button5:
                    OnclickButton5();
                    break;
                case R.id.button6:
                    OnclickButton6();
            }
        }
    }





    protected void startCameraActivity() {
        File file = new File(_path);
        Uri outputFileUri = Uri.fromFile(file);

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "resultCode: " + resultCode);

        if (mFlag == 1) {
            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                Log.v(TAG,"bitmapSelectError");
                mFlag=0;
            }
            if(mFlag!=0) {
                onPhotoTaken();
            }

        } else {
            if (resultCode == -1) {
                onPhotoTaken();
            } else {
                Log.v(TAG, "User cancelled");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MainActivity.PHOTO_TAKEN, _taken);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(MainActivity.PHOTO_TAKEN)) {
            onPhotoTaken();
        }
    }


    protected void onPhotoTaken() {
        _taken = true;
        progressDialog.show();
        new Thread(new Runnable() {
            public void run() {
                if (mFlag == 0) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    bitmap = BitmapFactory.decodeFile(_path, options);


                    try {
                        ExifInterface exif = new ExifInterface(_path);
                        int exifOrientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL);

                        Log.v(TAG, "Orient: " + exifOrientation);

                        int rotate = 0;

                        switch (exifOrientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotate = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotate = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotate = 270;
                                break;
                        }

                        Log.v(TAG, "Rotation: " + rotate);

                        if (rotate != 0) {

                            // Getting width & height of the given image.
                            int w = bitmap.getWidth();
                            int h = bitmap.getHeight();

                            // Setting pre rotate
                            Matrix mtx = new Matrix();
                            mtx.preRotate(rotate);

                            // Rotating Bitmap
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                        }

                        // Convert to ARGB_8888, required by tess
                        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                    } catch (IOException e) {
                        Log.e(TAG, "Couldn't correct orientation: " + e.toString());
                    }
                }
                // _image.setImageBitmap( bitmap );
                OpencvUtil opencvUtil = new OpencvUtil();

                bitmap = opencvUtil.Imageprocessing(bitmap);
                Log.i(TAG, "TessBaseAPI");

                takeOverInfo.setBitmap(bitmap);
                Intent intent;

                intent = new Intent(MainActivity.this, Main23Activity.class);

                intent.putExtra("key", takeOverInfo);
                startActivity(intent);
            }
      }).start();

    }

    private void OnClickButton() {
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {
                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }
        Log.v(TAG, "Starting Camera app");
        startCameraActivity();
    }

    protected void OnClickButton4() {
        takeOverInfo.setMozi("");

        Intent intent;

        intent = new Intent(MainActivity.this, Main2Activity.class);

        intent.putExtra("key", takeOverInfo);
        startActivity(intent);

    }

    protected void OnclickButton5() {
        Intent intent;

        intent = new Intent(MainActivity.this, Main22Activity.class);

        startActivity(intent);
    }

    protected void OnclickButton6() {
        mFlag = 1;
        startActivityForResult(imageIntent, REQUEST_GALLERY);

    }


}


