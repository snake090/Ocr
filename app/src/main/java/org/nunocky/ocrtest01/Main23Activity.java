package org.nunocky.ocrtest01;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;

import org.nunocky.ocrtest01.R;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class Main23Activity extends Activity {

    private ImageView imageView;
    private Button button;
    DrawNoteView view;
    private static final int MENU_CLEAR = 0;
    private static final int MENU_SAVE = 1;
/** アプリの初期化 */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main23);
// 描画クラスを設定
        final TakeOverInfo takeOverInfo = (TakeOverInfo) getIntent().getSerializableExtra("key");
        imageView=(ImageView)findViewById(R.id.imageView2);
        imageView.setImageBitmap(takeOverInfo.getBitmap());
        view = new DrawNoteView(getApplication(),takeOverInfo.getBitmap(),imageView);

        setContentView(view);

    }

/** メニューの生成イベント */

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_CLEAR, 0, "Clear");
        menu.add(0, MENU_SAVE, 0, "Save");
        return true;
    }
/** メニューがクリックされた時のイベント */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case MENU_CLEAR:
                view.clearDrawList(); break;
            case MENU_SAVE:

                break;
        }
        return true;
    }
}
/** 描画クラスの定義 */

class DrawNoteView extends android.view.View {
    Bitmap bmp ;
    Canvas bmpCanvas;
    Point oldpos = new Point(-1,-1);
    ImageView imageView;
    public DrawNoteView(Context c,Bitmap bmp,ImageView imageView) {
        super(c);
        setFocusable(true);
        this.bmp=bmp;
        this.imageView=imageView;

    }
    public void clearDrawList() {

        bmpCanvas.drawBitmap(bmp, 0, 0, null);
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w,h,oldw,oldh);

        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bmpCanvas = new Canvas(bmp);

    }
/** 描画イベント */

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bmp, 0, 0, null);

    }
/** タッチイベント */
    public boolean onTouchEvent(MotionEvent event) {
// 描画位置の確認
        Point cur = new Point((int)event.getX(), (int)event.getY());
        if (oldpos.x < 0) { oldpos = cur; }
// 描画属性を設定
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4);
// 線を描画

        bmpCanvas.drawLine(oldpos.x, oldpos.y, cur.x, cur.y, paint);
        oldpos = cur;
// 指を持ち上げたら座標をリセット
        if (event.getAction() == MotionEvent.ACTION_UP) {
            oldpos = new Point(-1, -1);

        }

        invalidate();
        return true;
    }
}
