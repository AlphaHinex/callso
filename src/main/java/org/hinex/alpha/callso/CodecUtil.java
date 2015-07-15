
/* ##################### 
 * Created by AlphaHinex
 * 2011-4-1 
 * 1 byte   = 8  bits
 * 1 char   = 16 bits
 * 1 short  = 16 bits
 * 1 int    = 32 bits
 * 1 float  = 32 bits
 * 1 long   = 64 bits
 * 1 double = 64 bits
 */

package org.hinex.alpha.callso;

public class CodecUtil {
    
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();
    
    public static String toHexString(byte[] bytes) {
        char[] chs = new char[bytes.length * 2];
        for (int i = 0, offset = 0; i < bytes.length; i++) {
            chs[offset++] = HEX[bytes[i] >> 4 & 0xf];
            chs[offset++] = HEX[bytes[i] & 0xf];
        }
        return new String(chs);
    }
    
    public static byte[] encode(long[] input) {
        int inputLen = input.length;
        int outputLen = inputLen*4;
        byte[] output = new byte[outputLen];
        
        for(int i = 0; i < inputLen; i++) {
            // type "long" in java is 64bit, 
            // only encode lower 32bit in order to adapt to C
            output[i*4] = (byte)(input[i] & 0xFFL);
            output[i*4 + 1] = (byte)((input[i]>>>8) & 0xFFL);
            output[i*4 + 2] = (byte)((input[i]>>>16) & 0xFFL);
            output[i*4 + 3] = (byte)((input[i]>>>24) & 0xFFL);
        }
        
        return output;
    }
    
    public static long[] decode(byte[] input) {
        int outputLen = input.length/4;
        
        // type "long" in java is 64bit, 
        // only decode lower 32bit and set higher 32bit to 0 in order to adapt to C
        long[] output = new long[outputLen];
        
        for(int i = 0; i < outputLen; i++) {
            output[i] = b2Unsign(input[i*4])
                        | b2Unsign(input[i*4 + 1]) << 8
                        | b2Unsign(input[i*4 + 2]) << 16
                        | b2Unsign(input[i*4 + 3]) << 24;
        }
        
        return output;
    }

    public static long b2Unsign(byte b) {
        return b < 0 ? b & 0x7F + 128 : b;
    }
    
}
