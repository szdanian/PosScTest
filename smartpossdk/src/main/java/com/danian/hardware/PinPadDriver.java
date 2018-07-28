package com.danian.hardware;

import android.os.Bundle;
import android.os.RemoteException;

import com.bw.spdev.Ped;
import com.bw.spdev.RspCode;

import com.jxnx.smartpos.api.ServiceResult;
import com.jxnx.smartpos.api.device.pinpad.DesMode;
import com.jxnx.smartpos.api.device.pinpad.MacAlgorithmType;
import com.jxnx.smartpos.api.device.pinpad.OnPinPadInputListener;
import com.jxnx.smartpos.api.device.pinpad.PinPad;
import com.jxnx.smartpos.api.device.pinpad.WorkKeyType;

import java.util.Random;

public class PinPadDriver extends PinPad.Stub {
    private final Ped pinPad = Ped.getInstance();

    @Override
    public int initPinPad(int type) throws RemoteException {
        return 0;
    }

    @Override
    public int loadPlainMKey(int mKeyIdx, byte[] keyData, int keyDataLen, boolean isTmsKey)
            throws RemoteException {
        if (pinPad.WriteKey(0, 0, Ped.PED_TLK, 1, Ped.Algthflag_DES,
                keyDataLen, keyData, 0, 0, null) == RspCode.RSPOK) {
            return ServiceResult.Success;
        } else {
            return ServiceResult.PinPad_Base_Error;
        }
    }

    @Override
    public int loadEncryptMKey(int mKeyIdx, byte[] keyData, int keyDataLen, int decMKeyIdx, boolean isTmsKey)
            throws RemoteException {
        if (pinPad.WriteKey(Ped.PED_TLK, 1, Ped.PED_TMK, mKeyIdx + 1, Ped.Algthflag_DES,
                keyDataLen, keyData, 0, 0, null) == RspCode.RSPOK) {
            return ServiceResult.Success;
        } else {
            return ServiceResult.PinPad_Base_Error;
        }
    }

    @Override
    public int loadKeyByCom(int portNo, int mKeyType) throws RemoteException {
        return 0;
    }

    @Override
    public int loadWKey(int mKeyIdx, int wKeyType, byte[] keyData, int keyDataLen) throws RemoteException {
        int keyType;
        if (wKeyType == WorkKeyType.PINKEY) {
            keyType = Ped.PED_TPK;
        } else if (wKeyType == WorkKeyType.MACKEY) {
            keyType = Ped.PED_TAK;
        } else {
            keyType = Ped.PED_TTK;
        }

        if (pinPad.WriteKey(Ped.PED_TMK, mKeyIdx + 1, keyType, keyType, Ped.Algthflag_DES,
                keyDataLen, keyData, 0, 0, null) == RspCode.RSPOK) {
            return ServiceResult.Success;
        } else {
            return ServiceResult.PinPad_Base_Error;
        }
    }

    @Override
    public int loadPlainDesKey(int keyIdx, byte[] keyData, int keyDataLen) throws RemoteException {
        if (pinPad.WriteKey(0, 0, Ped.PED_TDK, keyIdx + 10, Ped.Algthflag_DES,
                keyDataLen, keyData, 0, 0, null) == RspCode.RSPOK) {
            if (pinPad.WriteKey(0, 0, Ped.PED_TEK, keyIdx + 20, Ped.Algthflag_DES,
                    keyDataLen, keyData, 0, 0, null) == RspCode.RSPOK) {
                return ServiceResult.Success;
            }
        }
        return ServiceResult.PinPad_Base_Error;
    }

    @Override
    public byte[] calcWKeyKCV(int mKeyIdx, int wKeyType) throws RemoteException {
        return null;
    }

    @Override
    public int desEncByWKey(int mKeyIdx, int wKeyType, byte[] data, int dataLen, int desType, byte[] desResult)
            throws RemoteException {
        return ServiceResult.PinPad_Base_Error;
    }

    @Override
    public byte[] getMac(int mKeyIdx, int mode, int type, byte[] data) throws RemoteException {
        byte[] macOut = new byte[8];
        byte macType;

        if (type == MacAlgorithmType.ECB) {
            macType = 1;
        } else {
            macType = 0;
        }

        if (pinPad.GetMac((byte) (mKeyIdx + 1), (short) data.length, data, macOut, macType) != RspCode.RSPERR) {
            return macOut;
        } else {
            return null;
        }
    }

    @Override
    public byte[] desByPlainKey(int desKeyId, byte[] data, int dataLen, int desType, int desMode)
            throws RemoteException {
        byte[] dataOut = new byte[dataLen];

        if (desMode == DesMode.DECRYPT) {
            if (pinPad.CalcDES(desKeyId + 10, (short) dataLen, data, dataOut, (byte) 0) != RspCode.RSPERR) {
                return dataOut;
            }
        } else {
            if (pinPad.CalcDES(desKeyId + 20, (short) dataLen, data, dataOut, (byte) 1) != RspCode.RSPERR) {
                return dataOut;
            }
        }
        return null;
    }

    @Override
    public byte[] desByTmsKey(int tmsMKeyIdx, byte[] data, int dataLen, int desType, int desMode)
            throws RemoteException {
        return null;
    }

    @Override
    public boolean deleteMKey(int mKeyIdx) throws RemoteException {
        return false;
    }

    @Override
    public boolean format() throws RemoteException {
        if (pinPad.Erase() == RspCode.RSPOK) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public byte[] getRandom() throws RemoteException {
        byte[] random = new byte[8];
        if (pinPad.GetRandom(8, random) != RspCode.RSPERR) {
            return random;
        } else {
            return null;
        }
    }

    @Override
    public int inputText(OnPinPadInputListener onPinPadInputListener, int i) throws RemoteException {
        return 0;
    }

    @Override
    public int inputOnlinePin(byte[] bytes, int i, int i1, OnPinPadInputListener onPinPadInputListener) throws RemoteException {
        return 0;
    }

    @Override
    public boolean isInputting() throws RemoteException {
        return false;
    }

    @Override
    public boolean cancelInput() throws RemoteException {
        return false;
    }

    @Override
    public void setTimeOut(int i) throws RemoteException {

    }

    @Override
    public void ppDispText(String s, int i) throws RemoteException {

    }

    @Override
    public void ppScrClr(int i) throws RemoteException {

    }

    @Override
    public void setSupportPinLen(int[] ints) throws RemoteException {

    }

    @Override
    public Bundle getKSN() throws RemoteException {
        return null;
    }

    @Override
    public boolean increaseKSN(int i) throws RemoteException {
        return false;
    }

    @Override
    public String getDiversifiedEncryptData(int i, String s, String s1) throws RemoteException {
        return null;
    }

    @Override
    public String getDiversifiedDecryptData(int i, String s, String s1) throws RemoteException {
        return null;
    }
}
