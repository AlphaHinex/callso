package com.lpr;

public class LPRProxy {
    
    private LPRProxy() { }
    
    public static byte[] detect(short[] pixs, int width, int height, int maxsize, byte[] sign) {
        return LPR.getInstance().DetectLPR(pixs, width, height, maxsize, sign);
    }

}
