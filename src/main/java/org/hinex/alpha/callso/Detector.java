package org.hinex.alpha.callso;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.lpr.LPR;

public class Detector {
    
    private Detector() { }
    
    public static String detect(Context context, String path, int width, int height) {
        LPR l = LPR.getInstance();
        byte[] result = l.DetectLPR(getBitmap(path), width, height, 16, getSign(context));
        return new String(result);
    }
    
    private static byte[] getBitmap(String path) {
        // TODO
        return new byte[0];
    }
    
    private static byte[] getSign(Context context) {
        StringBuffer result = new StringBuffer();
        try {
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature sign = packageInfo.signatures[0];
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(sign.toByteArray()));
            result.append(cert.getIssuerDN().getName())
                  .append("; ")
                  .append(cert.getSerialNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString().getBytes();
    }

}
