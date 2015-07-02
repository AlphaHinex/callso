package com.lpr;

public class LPR {
    
    static {
        System.out.println("LPRecognition");
    }
    
    public native byte[] DetectLPR(byte[] pBytes, int width, int height, int maxsize, byte[] sign);
    
    private static LPR instance;
    
    private LPR() { }
    
    public static LPR getInstance() {
        if (instance == null) {
            instance = new LPR();
        }
        return instance;
    }

}
