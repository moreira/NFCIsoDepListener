package com.example.nfcisodeplistener;

import android.nfc.NdefRecord;

public class NdefRecordInfo {

    private NdefRecord record;

    public NdefRecordInfo(NdefRecord record) {
        this.record = record;
    }

    String getTNFTypeName(){
        switch (record.getTnf()){
            case NdefRecord.TNF_EMPTY: return "TNF_EMPTY";
            case NdefRecord.TNF_WELL_KNOWN: return "TNF_WELL_KNOWN";
            case NdefRecord.TNF_MIME_MEDIA: return "TNF_MIME_MEDIA";
            case NdefRecord.TNF_ABSOLUTE_URI: return "TNF_ABSOLUTE_URI";
            case NdefRecord.TNF_EXTERNAL_TYPE: return "TNF_EXTERNAL_TYPE";
            case NdefRecord.TNF_UNKNOWN: return "TNF_UNKNOWN";
            case NdefRecord.TNF_UNCHANGED: return "TNF_UNCHANGED";
        }
        return "COULD_NOT_RECOGNIZE_TNF";
    }

    String getTypeName(){
        byte[] type = record.getType();

        if(byteArrayEquals(type, NdefRecord.RTD_URI)) return "RTD_URI";
        if(byteArrayEquals(type, NdefRecord.RTD_ALTERNATIVE_CARRIER)) return "RTD_ALTERNATIVE_CARRIER";
        if(byteArrayEquals(type, NdefRecord.RTD_TEXT)) return "RTD_TEXT";
        if(byteArrayEquals(type, NdefRecord.RTD_HANDOVER_CARRIER)) return "RTD_HANDOVER_CARRIER";
        if(byteArrayEquals(type, NdefRecord.RTD_HANDOVER_REQUEST)) return "RTD_HANDOVER_REQUEST";
        if(byteArrayEquals(type, NdefRecord.RTD_HANDOVER_SELECT)) return "RTD_HANDOVER_SELECT";
        if(byteArrayEquals(type, NdefRecord.RTD_SMART_POSTER)) return "RTD_SMART_POSTER";

        return "COULD_NOT_RECOGNIZE_TYPE";
    }


    private boolean byteArrayEquals(byte[] a, byte[] b){
        if(a == null && b == null){
            return true;
        }
        if((a == null && b != null) || (a != null && b == null) || a.length != b.length){
            return false;
        }

        for(int i=0; i < a.length; i++){
            if(a[i] != b[i]) return false;
        }

        return true;
    }
}
