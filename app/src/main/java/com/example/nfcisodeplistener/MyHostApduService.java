package com.example.nfcisodeplistener;

import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefRecord;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

public class MyHostApduService extends HostApduService {

    private static final String TAG = "APDUService";

    // para mais respostas https://www.eftlab.com/knowledge-base/complete-list-of-apdu-responses
    private static final byte[] SELECT_OK_SW = { (byte) 0x90, (byte) 0x00 };
    private static final byte[] CMD_ABORTED = { (byte) 0x6F, (byte) 0x00 };

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.d(TAG, "APDU recebido: " + BytesUtil.byteToHex(apdu));

        try{
            APDUCommand apduCommand = APDUCommand.parse(apdu);
            Log.d(TAG, "APDU name:" + apduCommand.getCommandName());
            Log.d(TAG, "APDU sw1:" + apduCommand.getSw1());
            Log.d(TAG, "APDU sw2:" + apduCommand.getSw2());
            Log.d(TAG, "APDU declared length:" + apduCommand.getDataLength());
            Log.d(TAG, "APDU data length:" + apduCommand.getData().length);
            Log.d(TAG, "APDU data:" + BytesUtil.byteToHex(apduCommand.getData()));

            if(apduCommand.isUpdateBinary()) {

                NdefRecord ndefRecord = new NdefRecord(apduCommand.getData());
                NdefRecordInfo ndefRecordInfo = new NdefRecordInfo(ndefRecord);
                byte[] payload = ndefRecord.getPayload();

                Log.d(TAG, "UPDATE BINARY APDU inf:" + ndefRecordInfo.getTNFTypeName());
                Log.d(TAG, "UPDATE BINARY APDU type:" + ndefRecordInfo.getTypeName());
                Log.d(TAG, "UPDATE BINARY APDU id:" + BytesUtil.byteToHex(ndefRecord.getId()));
                Log.d(TAG, "UPDATE BINARY APDU payload:" + BytesUtil.byteToHex(payload));

                PixURIManager.getInstance().newPixURI(Uri.parse(new String(payload)));
            }

        }catch (IllegalArgumentException e){
            Log.e(TAG, "APDU falhou", e);
            return CMD_ABORTED;
        } catch (FormatException e) {
            Log.e(TAG, "NDefRecord parse falhou", e);
            return CMD_ABORTED;
        }
        return SELECT_OK_SW;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "HCE deativado, motivo: " + reason);
    }

}