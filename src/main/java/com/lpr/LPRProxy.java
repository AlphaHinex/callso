package com.lpr;

public class LPRProxy {
    
    private LPRProxy() { }
    
    public static byte[] detect(short[] pixs, int width, int height, int maxsize, byte[] sign) {
        if (pixs == null || pixs.length == 0) {
            return new byte[0];
        }
        
        byte[] bytes = new byte[pixs.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) pixs[i];
        }
        return LPR.getInstance().DetectLPR(bytes, width, height, maxsize);
    }

}
