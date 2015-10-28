package org.nunocky.ocrtest01;

import android.graphics.Bitmap;
import android.nfc.Tag;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Owner on 2015/10/19.
 */
public class OpencvUtil {
    private static final String TAG = "AndroidOCR.java";

    public Bitmap Imageprocessing(Bitmap bitmap) {
        Mat mat = new Mat();
        Mat mat1 = new Mat();
        Mat mat2 = new Mat();

        Utils.bitmapToMat(bitmap, mat);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

        //鮮鋭化 ラプラシアンフィルタ
        Imgproc.Laplacian(mat, mat1, mat.type(), 5, 1, 1);
        //二値化
        Imgproc.threshold(mat1, mat2, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        //ノイズ除去 メディアンフィルタ
        Imgproc.medianBlur(mat2, mat2, 3);
        Core.bitwise_not(mat2, mat2);
        Imgproc.cvtColor(mat2, mat2, Imgproc.COLOR_GRAY2BGRA, 4);


        Utils.matToBitmap(mat2, bitmap);

        Log.v(TAG, "OpenCvFinish");
        return bitmap;
    }

}
