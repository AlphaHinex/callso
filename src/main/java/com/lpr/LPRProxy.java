package com.lpr;

import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class LPRProxy {
    
    public LPRProxy() { }
    
    public byte[] callso(byte[] input, int width, int height) throws Exception {
        byte[] result = null;
        
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new FileInputStream("/Users/alphahinex/workspace/callso/src/test/resources/servercas.cer"));
        byte[] sign = (cert.getIssuerDN().getName() + cert.getSerialNumber()).getBytes();
        
        if (System.currentTimeMillis() % 2 == 0) {
            result = LPR.getInstance().DetectLPR(input, width, height, 16, sign);
        } else {
            result = LPR.getInstance().DetectLPR(input, width, height, 32, sign);
        }
        
        return result;
    }
    
}
