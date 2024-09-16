package com.example.nfcisodeplistener;

public class BytesUtil {

    public static String byteToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("0x%02X ", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
