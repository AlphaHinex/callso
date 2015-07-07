package com.lpr;

public class LPR {
    
    static {
        System.loadLibrary("LPRecognition");
    }
    
    public native byte[] DetectLPR(short[] pixs, int width, int height, int maxsize);
    
    private static LPR instance;
    
    private LPR() { }
    
    public static LPR getInstance() {
        if (instance == null) {
            instance = new LPR();
        }
        return instance;
    }

}
