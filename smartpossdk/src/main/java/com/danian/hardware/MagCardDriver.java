package com.danian.hardware;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import com.bw.spdev.MagCard;
import com.bw.spdev.RspCode;

import com.danian.hardware.DriverUtils.HelpUtils;

import com.jxnx.smartpos.api.device.reader.mag.MagCardInfoEntity;
import com.jxnx.smartpos.api.device.reader.mag.MagCardReader;
import com.jxnx.smartpos.api.device.reader.mag.OnSearchMagCardListener;
import com.jxnx.smartpos.api.ServiceResult;

import java.util.Date;

public class MagCardDriver extends MagCardReader.Stub {
    private static final MagCard magCard = MagCard.getInstance();

    private OnSearchMagCardListener mListener;
    private MagCardInfoEntity infoEntity;
    private Date startTime, endTime;
    private int mTimeout;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                magCard.CancelDev();
                switch (msg.what) {
                    case RspCode.RSPOK:
                        if (readTrackData() == RspCode.RSPOK) {
                            mListener.onSearchResult(ServiceResult.Success, infoEntity);
                        } else {
                            mListener.onSearchResult(ServiceResult.MagCardReader_Base_Error, infoEntity);
                        }
                        break;
                    case RspCode.RSPERR:
                        mListener.onSearchResult(ServiceResult.MagCardReader_No_Swiped, infoEntity);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private final Runnable magCardSearch = new Runnable() {
        @Override
        public void run() {
            if (magCard.CheckDev() == RspCode.RSPERR) {
                if (mTimeout != 0) {
                    endTime = new Date();
                    if ((endTime.getTime() - startTime.getTime()) / 1000 < mTimeout) {
                        mHandler.post(magCardSearch);
                    } else {
                        mHandler.sendEmptyMessage(RspCode.RSPERR);
                    }
                } else {
                    mHandler.post(magCardSearch);
                }
            } else {
                mHandler.sendEmptyMessage(RspCode.RSPOK);
            }
        }
    };

    @Override
    public int searchCard(OnSearchMagCardListener listener, int timeout) throws RemoteException {
        if (magCard.InitDev() == RspCode.RSPOK) {
            mListener = listener;
            mTimeout = timeout;
            startTime = new Date();
            new Thread(magCardSearch).start();
            return ServiceResult.Success;
        } else {
            return ServiceResult.MagCardReader_Other_Error;
        }
    }

    @Override
    public int searchCardEx(OnSearchMagCardListener listener, int timeout, Bundle bundle)
            throws RemoteException {
        return ServiceResult.MagCardReader_Other_Error;
    }

    @Override
    public void stopSearch() throws RemoteException {
        mHandler.removeCallbacks(magCardSearch);
        magCard.CancelDev();
    }

    @Override
    public void setIsCheckLrc(boolean b) throws RemoteException {

    }

    private int readTrackData() {
        byte[] rev = new byte[300];
        int len = magCard.GetAllStripInfo(rev);

        if (len != RspCode.RSPERR) {
            infoEntity = new MagCardInfoEntity();
            if (rev[1] > 0) {
                infoEntity.setTk1(HelpUtils.asciiToString(rev, 2, rev[1]));
            }

            if (rev[rev[1] + 3] > 0) {
                String track2 = HelpUtils.asciiToString(rev, rev[1] + 4, rev[rev[1] + 3]);
                infoEntity.setTk2(track2);
                if (track2.indexOf('=') > 0) {
                    infoEntity.setCardNo(track2.substring(0, track2.indexOf('=')));
                }
            }

            if (rev[rev[1] + rev[rev[1] + 3] + 5] > 0) {
                infoEntity.setTk3(HelpUtils.asciiToString(rev, rev[1] + rev[rev[1] + 3] + 6,
                        rev[rev[1] + rev[rev[1] + 3] + 5]));
            }
            return RspCode.RSPOK;
        } else {
            return RspCode.RSPERR;
        }
    }
}
