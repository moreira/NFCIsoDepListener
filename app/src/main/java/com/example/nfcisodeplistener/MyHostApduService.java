package com.example.nfcisodeplistener;

import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefRecord;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.net.URI;
import java.util.Arrays;

public class MyHostApduService extends HostApduService {

    private static final String TAG = "APDUService";

    // para mais respostas https://www.eftlab.com/knowledge-base/complete-list-of-apdu-responses
    private static final byte[] SELECT_OK_SW = { (byte) 0x90, (byte) 0x00 };
    private static final byte[] CMD_ABORTED = { (byte) 0x6F, (byte) 0x00 };

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
                byte[] recordRaw = Arrays.copyOfRange(apdu, 5, 5+length);
                try {
                    NdefRecord ndefRecord = new NdefRecord(recordRaw);
                    NdefRecordInfo ndefRecordInfo = new NdefRecordInfo(ndefRecord);
                    byte[] payload = ndefRecord.getPayload();

                    Log.d(TAG, "Comando UPDATE BINARY APDU inf:" + ndefRecordInfo.getTNFTypeName());
                    Log.d(TAG, "Comando UPDATE BINARY APDU type:" + ndefRecordInfo.getTypeName());
                    Log.d(TAG, "Comando UPDATE BINARY APDU id:" + byteToHex(ndefRecord.getId()));
                    Log.d(TAG, "Comando UPDATE BINARY APDU payload:" + byteToHex(payload));

                    if(ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN || !"RTD_URI".equals(ndefRecordInfo.getTypeName())){
                        return CMD_ABORTED;
                    }

                    PixURIManager.getInstance().newPixURI(Uri.parse(new String(payload)));
                } catch (FormatException e) {
                    Log.e(TAG, "Comando UPDATE BINARY APDU falhou", e);
                    return CMD_ABORTED;
                }
                return SELECT_OK_SW;
            }
        }

        return CMD_ABORTED;
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