package com.example.nfcisodeplistener;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class MyHostApduService extends HostApduService {

    private static final String TAG = "APDUService";

    private static final byte[] SELECT_OK_SW = { (byte) 0x90, (byte) 0x00 };
    private static final byte[] UNKNOWN_CMD_SW = { (byte) 0x6F, (byte) 0x00 };

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.d(TAG, "Comando APDU recebido: " + byteToHex(apdu));

        if (apdu != null && apdu.length > 0) {
            if (apdu[0] == (byte) 0x00 && apdu[1] == (byte) 0xA4) {
                Log.d(TAG, "Comando SELECT APDU detectado");
                return SELECT_OK_SW;
            }else if (apdu[0] == (byte) 0x00 && apdu[1] == (byte) 0xD6) {
                Log.d(TAG, "Comando UPDATE BINARY APDU detectado");
                int length = apdu[4] & 0xFF;
                byte[] emv = Arrays.copyOfRange(apdu, 5, 5+length);
                EMVManager.getInstance().notify(new String(emv));
                return SELECT_OK_SW;
            }
        }

        return UNKNOWN_CMD_SW;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "HCE deativado, motivo: " + reason);
    }

    private String byteToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("0x%02X ", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }
}