package org.hinex.alpha.callso;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class ImageDisposer {
    
    private static final String TAG = "ImageDisposer";
    
    private static final int IMAGE_MIN_HEIGHT= 1920;
    private static final int IMAGE_MIN_WIDHT = 1080;
    private static final int MIN_IMAGE_SIZE = IMAGE_MIN_WIDHT * IMAGE_MIN_HEIGHT;   // 130W
    private static final int MAX_IMAGE_SIZE = 2048*1536;                            // 300W
    
    private static ImageDisposer instance;
    private static WindowManager windowManager;
    private int width;
    private int height;
    
    private ImageDisposer() { }
    
    public static ImageDisposer getInstance(WindowManager wm) {
        if (instance == null) {
            windowManager = wm;
            instance = new ImageDisposer();
        }
        return instance;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public short[] dispose(String path) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "The passed in path SHOULD NOT NULL!");
            return null;
        }
        
        return doDispose(preDispose(path));
    }
    
    private Bitmap preDispose(String path) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();  
        bmpFactoryOptions.inJustDecodeBounds = true;  
        BitmapFactory.decodeFile(path, bmpFactoryOptions);
        
        Bitmap bitmap;
        if (bmpFactoryOptions.outHeight * bmpFactoryOptions.outWidth > (MIN_IMAGE_SIZE * 0.8)) {
            bitmap =  adjustForDisplay(path, false);
        } else {
            bitmap = BitmapFactory.decodeFile(path, null);
        }
        
        if (bitmap != null) {
            int degree = readPictureDegree(path);
            Bitmap b = rotaingImageView(degree, bitmap);
            if (bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return b;
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("deprecation")
    private Bitmap adjustForDisplay(String path, boolean adjustDisplay) {
        Display currentDisplay = windowManager.getDefaultDisplay();
        int dw = currentDisplay.getWidth();  
        int dh = currentDisplay.getHeight();  
        if (dw > dh){
            int t = dw;
            dw = dh;
            dh = t;
        }

        if (!adjustDisplay && dw * dh < MIN_IMAGE_SIZE){
            dw = IMAGE_MIN_WIDHT;
            dh = IMAGE_MIN_HEIGHT;
        }
        
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();  
        bmpFactoryOptions.inJustDecodeBounds = true;  
        BitmapFactory.decodeFile(path, bmpFactoryOptions);
        
        int width = bmpFactoryOptions.outWidth;
        int height = bmpFactoryOptions.outHeight;
        if (width > height){
            int t = width;
            width = height;
            height = t;
        }
        
        float heightRatio = (float) Math.ceil(height * 1.0 / (float)dh);  
        float widthRatio = (float) Math.ceil(width * 1.0 / (float)dw);  

        bmpFactoryOptions.inJustDecodeBounds = false;  
        int resize = (int)(((heightRatio > widthRatio ? heightRatio : widthRatio) + 0.5) * 1.2);
        if (heightRatio > 1 || widthRatio > 1) {
            bmpFactoryOptions.inSampleSize = resize;  
        }
        return BitmapFactory.decodeFile(path, bmpFactoryOptions);
    }
    
    private int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return degree;
    }
    
    private Bitmap rotaingImageView(int degree, Bitmap bitmap) {
        if (degree == 0 || bitmap == null) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        matrix = null;
        return resizedBitmap;
    }
    
    private short[] doDispose(Bitmap rawBitmap) {
        if (rawBitmap == null) {
            Log.e(TAG, "Input raw bitmap SHOULD NOT NULL");
            return new short[0];
        }
        width = rawBitmap.getWidth();
        height = rawBitmap.getHeight();
        if (width * height > MAX_IMAGE_SIZE) {
            if (rawBitmap.isRecycled()) {
                rawBitmap.recycle();
            }
            Log.e(TAG, "Input raw bitmap [" + width + " x " + height + "] SHOULD NOT LARGER than MAX IMAGE SIZE [" + MAX_IMAGE_SIZE + "].");
            return new short[0];
        }
        
        int size = width * height;
        int[] pix = new int[size];
        short[] pixs = new short[size * 3];
        rawBitmap.getPixels(pix, 0, width, 0, 0, width, height);
        if (!rawBitmap.isRecycled()) {
            rawBitmap.recycle();
        }
        
        for (int i = 0, j = 0; i < size; i++) {
            pixs[j++] = (short) ((pix[i] & 0x00FF0000) >> 16);
            pixs[j++] = (short) ((pix[i] & 0x0000FF00) >> 8);
            pixs[j++] = (short) ((pix[i] & 0X000000FF) >> 0);
        }
        return pixs;
    }
    
}
