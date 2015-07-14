package org.hinex.alpha.callso;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

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
        String sign;
        byte[] bytes;
        try {
            ImageDisposer instance = ImageDisposer.getInstance(windowManager);
            pixs = instance.dispose(path);
            sign = getSign();
            bytes = LPRProxy.detect(pixs, instance.getWidth(), instance.getHeight(), 16, sign);
            result = new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            bytes = null;
            sign = null;
            pixs = null;
        }
        return result;
    }
    
    private String getSign() throws UnsupportedEncodingException {
        String result = "";
        try {
            byte[] bytes = null;
            String packageName = context.getPackageName();
            String input = "CN=whf, PKG=" + packageName + ", O=hinex";
            MessageDigest digest = MessageDigest.getInstance("MD5");
            bytes = digest.digest(input.getBytes("utf8"));
            result = new String(bytes, charsetName);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return result;
    }

}
