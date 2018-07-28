package com.danian.hardware;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import com.bw.spdev.IccReader;
import com.bw.spdev.PiccReader;
import com.bw.spdev.RspCode;

import com.jxnx.smartpos.api.device.reader.icc.IccCardReader;
import com.jxnx.smartpos.api.device.reader.icc.IccCardType;
import com.jxnx.smartpos.api.device.reader.icc.ICCSearchResult;
import com.jxnx.smartpos.api.device.reader.icc.OnSearchIccCardListener;
import com.jxnx.smartpos.api.ServiceResult;

import java.util.Date;

public class IccCardDriver extends IccCardReader.Stub {
    public static final int TYPE_ICC = 1;
    public static final int TYPE_PICC = 2;

    private final int mCardType;
    private IccReader iccReader;
    private PiccReader piccReader;
    private OnSearchIccCardListener mListener;
    private Bundle cardInfo;
    private Date startTime, endTime;
    private int mTimeout;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case RspCode.RSPOK:
                        cardInfo = new Bundle();
                        cardInfo.putString(ICCSearchResult.CARDTYPE, IccCardType.CPUCARD);
                        mListener.onSearchResult(ServiceResult.Success, cardInfo);
                        break;
                    case RspCode.RSPERR:
                        mListener.onSearchResult(ServiceResult.Icc_Base_Error, cardInfo);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private final Runnable iccSearch = new Runnable() {
        @Override
        public void run() {
            try {
                if (!isCardExists()) {
                    if (mTimeout != 0) {
                        endTime = new Date();
                        if ((endTime.getTime() - startTime.getTime() / 1000 < mTimeout)) {
                            mHandler.post(iccSearch);
                        } else {
                            mHandler.sendEmptyMessage(RspCode.RSPERR);
                        }
                    } else {
                        mHandler.post(iccSearch);
                    }
                } else {
                    mHandler.sendEmptyMessage(RspCode.RSPOK);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    public IccCardDriver(int cardType) {
        mCardType = cardType;
        if (mCardType == TYPE_ICC) {
            iccReader = IccReader.getInstance();
        } else {
            piccReader = PiccReader.getInstance();
        }
    }

    @Override
    public int searchCard(OnSearchIccCardListener listener, int timeout, String[] type)
            throws RemoteException {
        mListener = listener;
        mTimeout = timeout;
        startTime = new Date();
        new Thread(iccSearch).start();
        return ServiceResult.Success;
    }

    @Override
    public void stopSearch() throws RemoteException {
        mHandler.removeCallbacks(iccSearch);
    }

    @Override
    public boolean isCardExists() throws RemoteException {
        if (mCardType == TYPE_ICC) {
            if (iccReader.Detect() == RspCode.RSPOK) {
                return true;
            }
        } else if (piccReader.Detect((byte) 1) == RspCode.RSPOK) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setupReaderConfig(Bundle bundle) throws RemoteException {
        return false;
    }

    public int getCardType() {
        return mCardType;
    }
}
