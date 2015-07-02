package org.hinex.alpha.callso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class ImageDisposer {
    
    private static final String TAG = "ImageDisposer";
    
    public static short[] dispose(String path) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "The passed in path SHOULD NOT NULL!");
            return null;
        }
        
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return dispose(bitmap);
    }
    
    public static short[] dispose(String path, Options opts) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "The passed in path SHOULD NOT NULL!");
            return null;
        }
        
        Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
        return dispose(bitmap);
    }
    
    public static short[] dispose(Bitmap rawBitmap) {
        if (rawBitmap == null) {
            Log.d(TAG, "Input raw bitmap is null");
            return new short[0];
        }
        
        int rawHeight = rawBitmap.getHeight();
        int rawWidth = rawBitmap.getWidth();
        int size = rawHeight * rawWidth;
        
        int[] pix = new int[size];
        short[] pixs = new short[size * 3];
        Log.d(TAG, "Start to get pixes ...");
        rawBitmap.getPixels(pix, 0, rawWidth, 0, 0, rawWidth, rawHeight);
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
