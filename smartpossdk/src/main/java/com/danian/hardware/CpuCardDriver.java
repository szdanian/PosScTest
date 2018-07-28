package com.danian.hardware;

import android.os.RemoteException;

import com.bw.spdev.IccReader;
import com.bw.spdev.PiccReader;
import com.bw.spdev.RspCode;

import com.danian.hardware.DriverInterface.ApduCallback;

import com.jxnx.smartpos.api.card.cpu.APDUCmd;
import com.jxnx.smartpos.api.card.cpu.CPUCardHandler;
import com.jxnx.smartpos.api.device.reader.icc.IccCardReader;
import com.jxnx.smartpos.api.ServiceResult;

public class CpuCardDriver extends CPUCardHandler.Stub {
    private final IccCardDriver mIccCardReader;
    private final int mCardType;
    private IccReader iccReader;
    private PiccReader piccReader;

    public CpuCardDriver(IccCardReader iccCardReader) {
        mIccCardReader = (IccCardDriver) iccCardReader;
        mCardType = mIccCardReader.getCardType();
        if (mCardType == IccCardDriver.TYPE_ICC) {
            iccReader = IccReader.getInstance();
        } else {
            piccReader = PiccReader.getInstance();
        }
    }

    @Override
    public boolean setPowerOn(byte[] atr) throws RemoteException {
        if (mCardType == IccCardDriver.TYPE_ICC) {
            if (iccReader.Reset() == RspCode.RSPOK) {
                if (iccReader.GetAtr(atr) != RspCode.RSPERR) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setPowerOff() throws RemoteException {
        if (mCardType == IccCardDriver.TYPE_ICC) {
            iccReader.Eject();
        } else {
            piccReader.Close();
        }
    }

    @Override
    public int exchangeAPDUCmd(APDUCmd apduCmd) throws RemoteException {
        int apduLen = apduCmd.getLc() + 6;
        byte[] apdu = new byte[apduLen];
        byte[] rsp = new byte[apduCmd.getLe()];
        byte[] sw = new byte[2];
        int rspLen;

        apdu[0] = apduCmd.getCla();
        apdu[1] = apduCmd.getIns();
        apdu[2] = apduCmd.getP1();
        apdu[3] = apduCmd.getP2();
        apdu[4] = (byte) apduCmd.getLc();
        if (apdu[4] > 0) {
            System.arraycopy(apduCmd.getDataIn(), 0, apdu, 5, apdu[4]);
        }
        apdu[apduLen - 1] = (byte) apduCmd.getLe();

        if (mCardType == IccCardDriver.TYPE_ICC) {
            rspLen = iccReader.ApduTransmit(apdu, apduLen, rsp, sw);
        } else {
            rspLen = piccReader.ApduTransmit(apdu, apduLen, rsp, sw);
        }

        if (rspLen != RspCode.RSPERR) {
            apduCmd.setDataOut(rsp);
            apduCmd.setDataOutLen(rspLen);
            apduCmd.setSwa(sw[0]);
            apduCmd.setSwb(sw[1]);
            return ServiceResult.Success;
        } else {
            return ServiceResult.CardHandler_Base_Error;
        }
    }

    @Override
    public boolean halt() throws RemoteException {
        return false;
    }

    @Override
    public boolean active() throws RemoteException {
        return false;
    }

    public void apduComand(byte[] apdu, byte[] rsp, byte[] sw, ApduCallback apduResult) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mCardType == IccCardDriver.TYPE_ICC) {
                    apduResult.callApduResult(iccReader.ApduTransmit(apdu, apdu.length, rsp, sw));
                } else {
                    apduResult.callApduResult(piccReader.ApduTransmit(apdu, apdu.length, rsp, sw));
                }
            }
        }).start();
    }

    public boolean getIccChallenge(int len, byte[] challenge) {
        if (iccReader.GetChallenge((byte) len, challenge) != RspCode.RSPERR) {
            return true;
        } else {
            return false;
        }
    }

    public byte getPiccCardTypeFunction() {
        return piccReader.GetCardTypeFunction();
    }

    public int getPiccCardSNFunction(byte[] sn) {
        return piccReader.GetCardSNFunction(sn);
    }
}
