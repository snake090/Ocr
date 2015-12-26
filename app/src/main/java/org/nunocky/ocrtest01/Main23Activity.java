package org.nunocky.ocrtest01;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.ExifInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.nunocky.ocrtest01.R;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Main23Activity extends AppCompatActivity {

    DrawNoteView view;
    private static final int MENU_CLEAR = 0;
    private static final int MENU_OK = 1;
    private static final String TAG = "AndroidOCR";
    private TakeOverInfo takeOverInfo;
    private Bitmap bitmap;
    private int Flag;
    private ProgressDialog progressDialog;
    protected String _path;
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";


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
        setContentView(R.layout.activity_main23);
        setTitle("検索範囲を囲って下さい");
        takeOverInfo = (TakeOverInfo) getIntent().getSerializableExtra("key");
        Flag = 0;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");

        _path = DATA_PATH + "/ocr.jpg";
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_CLEAR, 0, "Clear");
        menu.add(0, MENU_OK, 0, "OK");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CLEAR:
                view.clearDrawList();

                break;
            case MENU_OK:
                onPhotoTaken();
                break;
        }
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Flag == 0) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.ralativeLayout);
            Log.v(TAG, Integer.toString(rl.getWidth()) + " " + Integer.toString(rl.getHeight()));
            if (rl.getHeight() >= rl.getWidth()) {
                bitmap = Bitmap.createScaledBitmap(takeOverInfo.getBitmap(), rl.getWidth(), rl.getWidth(), false);
            } else {
                bitmap = Bitmap.createScaledBitmap(takeOverInfo.getBitmap(), rl.getHeight(), rl.getHeight(), false);
            }
            view = new DrawNoteView(getApplication(), bitmap);
            takeOverInfo.setBitmap(bitmap);
            Flag = 1;
            setContentView(view);
        }
    }

    protected void onPhotoTaken() {
        progressDialog.show();
        new Thread(new Runnable() {
            public void run() {
/*
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
        */
        TessBaseAPI baseApi = new TessBaseAPI();
        if (view.getFlag()) {
            OpencvUtil opencvUtil = new OpencvUtil();

            bitmap = opencvUtil.Imageprocessing(bitmap);
            Log.i(TAG, "TessBaseAPI");


            baseApi.setDebug(true);
            baseApi.init(DATA_PATH, takeOverInfo.getLang());
            baseApi.setImage(bitmap);

            Log.v(TAG, Integer.toString(view.getMin_x()) + " " + Integer.toString(view.getMin_y()) + " " + Integer.toString(view.getMax_x()) + " " + Integer.toString(view.getMax_y()));

            baseApi.setRectangle(view.getMin_x(), view.getMin_y(),  view.getMax_x() - view.getMin_x(),view.getMax_y() - view.getMin_y());
        }else{
            Log.i(TAG, "TessBaseAPI");
            baseApi.setDebug(true);
            baseApi.init(DATA_PATH, takeOverInfo.getLang());
            baseApi.setImage(takeOverInfo.getBitmap());
        }

        String recognizedText = baseApi.getUTF8Text();

        baseApi.end();

        Log.i(TAG, "TessBaseAPIEnd");

        Log.v(TAG, "OCRED TEXT: " + recognizedText);

        if (takeOverInfo.getLang().equalsIgnoreCase("eng")) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        }

        recognizedText = recognizedText.trim();


        takeOverInfo.setMozi(recognizedText);
        Intent intent;

        intent = new Intent(Main23Activity.this, Main2Activity.class);

        intent.putExtra("key", takeOverInfo);
        startActivity(intent);
    }
      }).start();
     }
}

class DrawNoteView extends android.view.View {

    Bitmap bmp;

    ArrayList<Point> draw_list = new ArrayList<Point>();
    ArrayList<Integer> x = new ArrayList<Integer>();
    ArrayList<Integer> y = new ArrayList<Integer>();
    boolean flag;

    public DrawNoteView(Context c, Bitmap bmp) {
        super(c);
        setFocusable(true);
        this.bmp = bmp;
        flag = false;

    }

    public void clearDrawList() {

        draw_list.clear();
        clear();
        invalidate();
    }

    public int getMax_x() {
        Collections.sort(x);
        return x.get(x.size() - 1);
    }

    public int getMin_y() {
        Collections.sort(y);
        return y.get(0);
    }

    public int getMax_y() {
        Collections.sort(y);
        return y.get(y.size() - 1);
    }

    public int getMin_x() {
        Collections.sort(x);
        return x.get(0);
    }

    public boolean getFlag() {
        return flag;
    }

    private void clear() {
        flag = false;
        x.clear();
        y.clear();
    }

    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(bmp, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4);
// 記録した座標を順に繋げて描画する
        Point q = new Point(-1, -1);
        for (int i = 0; i < draw_list.size(); i++) {
            Point p = draw_list.get(i);
            if (p.x >= 0) {
                if (q.x < 0) {
                    q = p;
                }
                x.add(q.x);
                x.add(p.x);
                y.add(q.y);
                y.add(p.y);
                canvas.drawLine(q.x, q.y, p.x, p.y, paint);
            }
            q = p;
        }

    }


    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        draw_list.add(new Point(x, y));
        if (event.getAction() == MotionEvent.ACTION_UP) {
            draw_list.add(new Point(-1, -1));
        }
        flag = true;
        invalidate();
        return true;

    }

}
