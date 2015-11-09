package org.nunocky.ocrtest01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by Owner on 2015/10/28.
 */
public class TakeOverInfo implements Serializable {
    private byte[] bytes;
    private String mozi;
    private boolean kind;

    public void setMozi(String mozi) {
        this.mozi = mozi;
    }

    public void setKind(boolean kind) {
        this.kind = kind;
    }

    public boolean isKind() {

        return kind;
    }

    public void setBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        bytes=baos.toByteArray();
    }

    public String getMozi() {

        return mozi;
    }

    public Bitmap getBitmap() {

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
