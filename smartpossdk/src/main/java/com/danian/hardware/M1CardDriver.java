package com.danian.hardware;

import android.os.RemoteException;

import com.bw.spdev.PiccReader;
import com.bw.spdev.RspCode;

import com.jxnx.smartpos.api.card.mifare.M1CardHandler;
import com.jxnx.smartpos.api.device.reader.icc.IccCardReader;
import com.jxnx.smartpos.api.ServiceResult;

public class M1CardDriver extends M1CardHandler.Stub {
    private final PiccReader piccReader = PiccReader.getInstance();
    private IccCardReader mIccCardReader;

    public M1CardDriver(IccCardReader iccCardReader) {
        mIccCardReader = iccCardReader;
    }

    @Override
    public int authority(int keyType, int blkNo, byte[] pwd, byte[] serialNo) throws RemoteException {
        if (piccReader.KeyAuth_M1(keyType, blkNo, pwd.length, pwd, serialNo.length, serialNo) == RspCode.RSPOK) {
            return ServiceResult.Success;
        } else {
            return ServiceResult.M1Card_Verify_Err;
        }
    }

    @Override
    public int readBlock(int blkNo, byte[] blkValue) throws RemoteException {
        if (piccReader.ReadBlk_M1(blkNo, blkValue) != RspCode.RSPERR) {
            return ServiceResult.Success;
        } else {
            return ServiceResult.M1Card_Data_Block_Err;
        }
    }

    @Override
    public int writeBlock(int blkNo, byte[] blkValue) throws RemoteException {
        if (piccReader.WriteBlk_M1(blkNo, blkValue.length, blkValue) == RspCode.RSPOK) {
            return ServiceResult.Success;
        } else {
            return ServiceResult.M1Card_Data_Block_Err;
        }
    }

    @Override
    public int operateBlock(int operType, int blkNo, byte[] value, int updBlkNo) throws RemoteException {
        return ServiceResult.M1Card_Other_Error;
    }
}
