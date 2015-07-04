package org.hinex.alpha.callso;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class ImageDisposer {
    
    private static final String TAG = "ImageDisposer";
    
    public static short[] dispose(InputStream stream) {
        return dispose(stream, null);
    }
    
    public static short[] dispose(InputStream stream, Options opts) {
        if (stream == null) {
            Log.e(TAG, "The passed in stream SHOULD NOT NULL!");
            return null;
        }
        
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, opts);
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
