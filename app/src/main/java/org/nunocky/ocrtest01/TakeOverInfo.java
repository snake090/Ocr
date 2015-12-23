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
    private boolean function; //true 辞書
    private boolean kind;  //true 英語から日本語
    private String lang;



    public void setMozi(String mozi) {
        this.mozi = mozi;
    }

    public void setKind(boolean kind) {
        this.kind = kind;
    }

    public boolean isKind() {

        return kind;
    }
    public void setFunction(boolean function) {
        this.function = function;
    }

    public boolean isFunction() {

        return function;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
