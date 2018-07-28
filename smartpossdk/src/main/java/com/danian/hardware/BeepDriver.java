package com.danian.hardware;

import android.os.RemoteException;

import com.bw.spdev.SysCmd;

import com.jxnx.smartpos.api.device.beeper.Beeper;
import com.jxnx.smartpos.api.device.beeper.BeepModeConstrants;

public class BeepDriver extends Beeper.Stub {
    private final SysCmd sysCmd = SysCmd.getInstance();

    @Override
    public void beep(int mode) throws RemoteException {
        switch (mode) {
            case BeepModeConstrants.NORMAL:
                sysCmd.SysBeepOn(100, 150);
                break;
            case BeepModeConstrants.SUCCESS:
                sysCmd.SysBeepOn(500, 150);
                break;
            case BeepModeConstrants.FAIL:
                sysCmd.SysBeepOn(50, 150);
                break;
            case BeepModeConstrants.INTERVAL:
                sysCmd.SysBeepOn(100, 150);
                sysCmd.SysBeepOn(100, 150);
                sysCmd.SysBeepOn(100, 150);
                break;
            case BeepModeConstrants.ERROR:
                sysCmd.SysBeepOn(200, 150);
                break;
        }
    }
}
