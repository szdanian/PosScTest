package com.danian.hardware;

import com.bw.spdev.RspCode;

public interface DriverInterface {

    interface RespCode {
        int RESPOK = RspCode.RSPOK;
        int RESPERR = RspCode.RSPERR;
    }

    interface KeyCallback {
        void callKeyResult(boolean result);
    }

    interface ApduCallback {
        void callApduResult(int result);
    }
}
