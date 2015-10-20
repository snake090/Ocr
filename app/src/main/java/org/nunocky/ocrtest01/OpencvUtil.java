package org.nunocky.ocrtest01;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Owner on 2015/10/19.
 */
public class OpencvUtil {
    public Bitmap Imageprocessing(Bitmap bitmap){
        Mat mat = new Mat();
        Mat mat1 = new Mat();
        Mat mat2=new Mat();

        Utils.bitmapToMat(bitmap, mat);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(mat1, mat2, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        Imgproc.cvtColor(mat2, mat2, Imgproc.COLOR_GRAY2BGRA, 4);
        Utils.matToBitmap(mat2, bitmap);
        return bitmap;
    }

}
