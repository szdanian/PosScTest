package com.danian.hardware;

import com.bw.spdev.Ped;
import com.bw.spdev.RspCode;

import com.danian.hardware.DriverInterface.KeyCallback;

public class PinDriver {
    public static final int KEY_TRANSMIT = Ped.PED_TLK;
    public static final int KEY_MASTER = Ped.PED_TMK;
    public static final int KEY_PIN = Ped.PED_TPK;
    public static final int KEY_MAC = Ped.PED_TAK;
    public static final int KEY_DECRYPT = Ped.PED_TDK;
    public static final int KEY_ENCRYPT = Ped.PED_TEK;
    public static final int KEY_TRACK = Ped.PED_TTK;

    public static final int MODE_DECRYPT = 0;
    public static final int MODE_ENCRYPT = 1;

    private static final PinDriver pinDriver = new PinDriver();
    private final Ped ped = Ped.getInstance();

    public static PinDriver getInstance() {
        return pinDriver;
    }

    public void eraseKey(KeyCallback keyResult) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ped.Erase() == RspCode.RSPOK) {
                    keyResult.callKeyResult(true);
                } else {
                    keyResult.callKeyResult(false);
                }
            }
        }).start();
    }

    public void setTransmitKey(byte[] transmitKey, KeyCallback keyResult) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ped.WriteKey(0, 0, KEY_TRANSMIT, KEY_TRANSMIT, Ped.Algthflag_DES,
                        transmitKey.length, transmitKey, 0, 0, null) == RspCode.RSPOK) {
                    keyResult.callKeyResult(true);
                } else {
                    keyResult.callKeyResult(false);
                }
            }
        }).start();
    }

    public boolean setMasterKey(byte[] masterKey, byte[] check) {
        if (check == null) {
            if (ped.WriteKey(KEY_TRANSMIT, KEY_TRANSMIT, KEY_MASTER, KEY_MASTER, Ped.Algthflag_DES,
                    masterKey.length, masterKey, 0, 0, null) == RspCode.RSPOK) {
                return true;
            }
        } else {
            if (ped.WriteKey(KEY_TRANSMIT, KEY_TRANSMIT, KEY_MASTER, KEY_MASTER, Ped.Algthflag_DES,
                    masterKey.length, masterKey, 1, 4, check) == RspCode.RSPOK) {
                return true;
            }
        }
        return false;
    }

    public boolean setWorkKey(int keyMode, byte[] workey, byte[] check) {
        if (check == null) {
            if (ped.WriteKey(KEY_MASTER, KEY_MASTER, keyMode, keyMode, Ped.Algthflag_DES,
                    workey.length, workey, 0, 0, null) == RspCode.RSPOK) {
                return true;
            }
        } else {
            if (ped.WriteKey(KEY_MASTER, KEY_MASTER, keyMode, keyMode, Ped.Algthflag_DES,
                    workey.length, workey, 1, 4, check) == RspCode.RSPOK) {
                return true;
            }
        }
        return false;
    }

    public byte[] calcDes(int keyIdx, byte[] dataIn, int keyMode) {
        byte[] dataOut = new byte[dataIn.length];
        if (ped.CalcDES(keyIdx, (short) dataIn.length, dataIn, dataOut, (byte) keyMode) != RspCode.RSPERR) {
            return dataOut;
        } else {
            return null;
        }
    }
}
