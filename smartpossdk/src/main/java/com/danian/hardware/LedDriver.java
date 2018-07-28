package com.danian.hardware;

import android.os.RemoteException;

import com.bw.spdev.SysCmd;

import com.jxnx.smartpos.api.device.led.LEDDriver;
import com.jxnx.smartpos.api.device.led.LEDLightConstrants;

public class LedDriver extends LEDDriver.Stub {
    private final SysCmd sysCmd = SysCmd.getInstance();

    @Override
    public void setLed(int light, boolean isOn) throws RemoteException {
        switch (light) {
            case LEDLightConstrants.RED:
                if (isOn) {
                    sysCmd.SysLedOn(SysCmd.LED_RED);
                } else {
                    sysCmd.SysLedOff(SysCmd.LED_RED);
                }
                break;
            case LEDLightConstrants.GREEN:
                if (isOn) {
                    sysCmd.SysLedOn(SysCmd.LED_GREEN);
                } else {
                    sysCmd.SysLedOff(SysCmd.LED_GREEN);
                }
                break;
            case LEDLightConstrants.YELLOW:
                if (isOn) {
                    sysCmd.SysLedOn(SysCmd.LED_YELLOW);
                } else {
                    sysCmd.SysLedOff(SysCmd.LED_YELLOW);
                }
                break;
            case LEDLightConstrants.BLUE:
                if (isOn) {
                    sysCmd.SysLedOn(SysCmd.LED_BLUE);
                } else {
                    sysCmd.SysLedOff(SysCmd.LED_BLUE);
                }
                break;
        }
    }
}
