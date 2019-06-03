package com.example.server.manager.common.util;

/**
 * This class provides convenient functions to convert hex string to byte array and vice versa.
 *
 * @date 2017-10-01
 */
public class HexUtil {

    private static final String HEX_CHARS = "0123456789abcdef";

    private HexUtil() {
    }

    /**
     * Converts a byte array to hex string.
     *
     * @param b -
     *          the input byte array
     * @return hex string representation of b.
     */
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(HexUtil.HEX_CHARS.charAt(b[i] >>> 4 & 0x0F));
            sb.append(HexUtil.HEX_CHARS.charAt(b[i] & 0x0F));
        }
        return sb.toString();
    }

    /**
     * Converts a hex string into a byte array.
     *
     * @param s -
     *          string to be converted
     * @return byte array converted from s
     */
    public static byte[] toByteArray(String s) {
        byte[] buf = new byte[s.length() / 2];
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) ((Character.digit(s.charAt(j++), 16) << 4) | Character.digit(s.charAt(j++), 16));
        }
        return buf;
    }

    public static String appendParam(String returnStr, String paramId, String paramValue) {
        if (!returnStr.equals("")) {
            if (!paramValue.equals("")) {
                returnStr = returnStr + "&" + paramId + "=" + paramValue;
            }
        } else {
            if (!paramValue.equals("")) {
                returnStr = paramId + "=" + paramValue;
            }
        }
        return returnStr;
    }

    public static int hexByteToInteger(byte[] b) {
        return hexStringToInteger(toHexString(b));
    }

    public static float hexByteToFloat(byte[] b) {
        return hexStringToFloat(toHexString(b));
    }

    public static float hexStringToFloat(String s) {
        return Float.valueOf(s);
    }

    public static int hexStringToInteger(String s) {
        return Integer.valueOf(s, 16);
    }
}
