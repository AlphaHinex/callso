package org.hinex.alpha.callso;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import com.lpr.LPR;

public class Detector {
    
    private static final String TAG = "Detector";
    
    private Detector() { }
    
    public static String detect(Context context, String path, int width, int height) {
        LPR l = LPR.getInstance();
        byte[] result = l.DetectLPR(ImageDisposer.dispose(path), width, height, 16, getSign(context));
        return new String(result);
    }
    
    private static byte[] getSign(Context context) {
        if (context == null) {
            Log.e(TAG, "The passed in context SHOULD NOT NULL!");
        }
        StringBuffer result = new StringBuffer();
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
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }
        return result.toString().getBytes();
    }

}
