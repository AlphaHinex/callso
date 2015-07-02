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

import com.lpr.LPRProxy;

public class Detector {
    
    private static final String TAG = "Detector";
    
    private Detector() { }
    
    public static String detect(Context context, String path, int width, int height, String charsetName) {
        String result = "";
        short[] pixs;
        byte[] sign;
        byte[] bytes;
        try {
            pixs = ImageDisposer.dispose(path);
            sign = getSign(context, charsetName);
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
    
    private static byte[] getSign(Context context, String charsetName) {
        if (context == null) {
            Log.e(TAG, "The passed in context SHOULD NOT NULL!");
            return new byte[0];
        }
        
        StringBuffer result = new StringBuffer();
        byte[] bytes = null;
        try {
            String packageName = context.getPackageName();
            Log.d(TAG, "package name: " + packageName);
            
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature sign = packageInfo.signatures[0];
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(sign.toByteArray()));
            
            result.append(cert.getIssuerDN().getName())
                  .append("; ")
                  .append(cert.getSerialNumber());
            
            Log.d(TAG, "issuer name: " + cert.getIssuerDN().getName());
            Log.d(TAG, "serial number: " + cert.getSerialNumber());
            
            bytes = result.toString().getBytes(charsetName);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }
        return bytes;
    }

}