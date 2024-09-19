package com.example.nfcisodeplistener;

import android.util.Log;

import java.util.Arrays;

public class APDUCommand {

    private static final String TAG = "APDUCommand";
    public static final byte UPDATE_BINARY_CMD = (byte) 0xD6;
    public static final byte SELECT_CMD = (byte) 0xA4;

    private String commandName;
    private byte commandByte;
    private byte sw1;
    private byte sw2;
    private int dataLength;
    private boolean isExtended;
    private byte[] data;
    private byte le;

    private APDUCommand(){}

    public static APDUCommand parse(byte[] apdu) {
        if(apdu == null || apdu.length < 5){
            throw new IllegalArgumentException("APDU bytes cannot be null, nor length smaller than 5");
        }

        APDUCommand apduCommand = new APDUCommand();
//        // validar CLA
//        if(apdu[0] != 0x00 && apdu[0] != (byte)0x80 && apdu[0] != (byte)0xA0) {
//            throw new IllegalArgumentException("invalid APDU command");
//        }

        if(apdu[1] == SELECT_CMD) {
            Log.d(TAG, "Comando SELECT detectado");
            apduCommand.commandByte = apdu[1];
            apduCommand.commandName = "SELECT";
        } else if(apdu[1] == UPDATE_BINARY_CMD) {
            Log.d(TAG, "Comando UPDATE BINARY detectado");
            apduCommand.commandByte = apdu[1];
            apduCommand.commandName = "UPDATE_BINARY";
        } else {
            throw new IllegalArgumentException("Command not supported: "+BytesUtil.byteToHex(new byte[]{apdu[1]}));
        }

        apduCommand.sw1 = apdu[2];
        apduCommand.sw2 = apdu[3];

        if(apdu[4] == 0x00 && apdu.length > 6) {
            // extended
            apduCommand.isExtended = true;
            int i1 = apdu[5] & 0xFF;
            int i2 = apdu[6] & 0xFF;
            apduCommand.dataLength =  (i1<<8) + i2;
        } else {
            apduCommand.isExtended = false;
            apduCommand.dataLength = apdu[4] & 0xFF;
        }

        int headerOffset = apduCommand.isExtended ? 7 : 5;
        apduCommand.data = Arrays.copyOfRange(apdu, headerOffset, apdu.length-1);

        apduCommand.le = apdu[apdu.length-1];

        return apduCommand;
    }

    public String getCommandName() {
        return commandName;
    }

    public byte getCommandByte() {
        return commandByte;
    }

    public byte getSw1() {
        return sw1;
    }

    public byte getSw2() {
        return sw2;
    }

    public int getDataLength() {
        return dataLength;
    }

    public boolean isExtended() {
        return isExtended;
    }

    public byte[] getData() {
        return data;
    }

    public byte getLe() {
        return le;
    }

    public boolean isUpdateBinary() {
        return this.commandByte == UPDATE_BINARY_CMD;
    }
}
