package org.hinex.alpha.callso;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
    
    public String detect(String path, int width, int height) {
        String result = "";
        short[] pixs;
        byte[] sign;
        byte[] bytes;
        try {
            pixs = ImageDisposer.getInstance(windowManager).dispose(path);
            sign = getSign();
            bytes = LPRProxy.detect(pixs, width, height, 16, sign);
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
    
    private static byte[] getSign() {
        byte[] bytes = null;
        try {
            String packageName = context.getPackageName();
            
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature sign = packageInfo.signatures[0];
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(sign.toByteArray()));
            
            String issuerName = cert.getIssuerDN().getName();
            String serialNumber = cert.getSerialNumber() + "";
            
            bytes = (issuerName + packageName + serialNumber).getBytes(charsetName);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return bytes;
    }

}
