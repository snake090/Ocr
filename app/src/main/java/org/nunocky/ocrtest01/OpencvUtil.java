package org.nunocky.ocrtest01;

import android.graphics.Bitmap;
import android.nfc.Tag;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Owner on 2015/10/19.
 */
public class OpencvUtil {
    private static final String TAG = "AndroidOCR.java";

    private Mat changeGrayMat(Bitmap src) {
        Mat mat = changeBitmapToMat(src);
        Mat gray = new Mat();
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_RGB2GRAY);
        return gray;
    }

    private Bitmap changeMatToBitmap(Mat mat) {
        Bitmap dst = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, dst);
        return dst;
    }


    private Mat changeBitmapToMat(Bitmap src) {
        Mat mat = new Mat(src.getWidth(), src.getHeight(), CvType.CV_32FC1);
        Utils.bitmapToMat(src, mat);
        return mat;
    }


    public Bitmap Imageprocessing(Bitmap bitmap) {
        Mat mat=changeGrayMat(bitmap);
        Mat mat1=new Mat();

        Imgproc.adaptiveThreshold(mat, mat1, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 37, 17);

        Log.v(TAG,"OpenCV_finish");
        return changeMatToBitmap(mat1);

    }




}
