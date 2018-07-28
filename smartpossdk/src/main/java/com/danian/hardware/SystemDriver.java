package com.danian.hardware;

import com.bw.spdev.SpDev;
import com.bw.spdev.SysCmd;

public class SystemDriver {
    private static final SpDev spDev = SpDev.getInstance();
    private static final SysCmd sysCmd = SysCmd.getInstance();

    public static int systemPowerOn() {
        spDev.SpDevPowerOn();
        return spDev.SpDevCreate();
    }

    public static int systemPowerOff() {
        spDev.SpDevPowerOff();
        return spDev.SpDevRelease();
    }

    public static int comTtyTrims(byte[] send, byte[] rev, int timeout) {
        return sysCmd.ComTtyTrims(send, send.length, rev, rev.length, timeout);
    }

    public static String sysGetSpVersion() {
        return sysCmd.SysGetSpVersion();
    }
}
