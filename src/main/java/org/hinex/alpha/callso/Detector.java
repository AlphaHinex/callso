package org.hinex.alpha.callso;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.lpr.LPRProxy;

public class Detector {
    
    private static final String TAG = "Detector";

    private static Detector instance;
    private static Context context;
    private static WindowManager windowManager;
    private static String charsetName;
    
    private Detector() { }
    
    public static Detector getInstance(Context ctx, WindowManager manager, String encoding) {
        if (instance == null) {
            if (ctx == null || manager == null) {
                Log.e(TAG, "The passed in context or window manager SHOULD NOT NULL!");
                return null;
            }
            context = ctx;
            windowManager = manager;
            charsetName = encoding;
            instance = new Detector();
        }
        return instance;
    }
    
    public String detect(String path) {
        String result = "";
        short[] pixs;
        byte[] sign;
        byte[] bytes;
        try {
            ImageDisposer instance = ImageDisposer.getInstance(windowManager);
            pixs = instance.dispose(path);
            sign = getSign();
            bytes = LPRProxy.detect(pixs, instance.getWidth(), instance.getHeight(), sign);
            if (bytes != null) {
                result = new String(bytes, charsetName);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            bytes = null;
            sign = null;
            pixs = null;
        }
        return result;
    }
    
    private byte[] getSign() throws UnsupportedEncodingException {
        byte[] result = null;
        try {
            String packageName = context.getPackageName();
            String input = "CN=whf, PKG=" + packageName + ", O=hinex";
            result = MD5.hexdigest(input, "utf8").getBytes("utf8");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return result;
    }

}
