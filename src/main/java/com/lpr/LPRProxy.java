package com.lpr;

public class LPRProxy {
    
    private LPRProxy() { }
    
    public static byte[] detect(short[] pixs, int width, int height, int maxsize, String sign) {
        if (pixs == null || pixs.length == 0) {
            return new byte[0];
        }
        
        return LPR.getInstance().DetectLPR(pixs, width, height, maxsize, sign);
    }

}
