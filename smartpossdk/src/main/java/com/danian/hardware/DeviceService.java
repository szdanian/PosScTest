package com.danian.hardware;

import android.os.Bundle;
import android.os.RemoteException;

import com.bw.spdev.IccReader;
import com.bw.spdev.PiccReader;
import com.bw.spdev.RspCode;

import com.jxnx.smartpos.api.card.cpu.CPUCardHandler;
import com.jxnx.smartpos.api.card.industry.IndustryCardHandler;
import com.jxnx.smartpos.api.card.mifare.M1CardHandler;
import com.jxnx.smartpos.api.device.beeper.Beeper;
import com.jxnx.smartpos.api.device.cashbox.CashBoxDriver;
import com.jxnx.smartpos.api.device.hsm.HsmDevice;
import com.jxnx.smartpos.api.device.led.LEDDriver;
import com.jxnx.smartpos.api.device.modem.ModemDriver;
import com.jxnx.smartpos.api.device.pinpad.PinPad;
import com.jxnx.smartpos.api.device.printer.Printer;
import com.jxnx.smartpos.api.device.reader.icc.IccCardReader;
import com.jxnx.smartpos.api.device.reader.icc.IccReaderSlot;
import com.jxnx.smartpos.api.device.reader.mag.MagCardReader;
import com.jxnx.smartpos.api.device.scanner.InnerScanner;
import com.jxnx.smartpos.api.device.serialport.SerialPortDriver;
import com.jxnx.smartpos.api.emv.EmvHandler;
import com.jxnx.smartpos.api.engine.DeviceServiceEngine;
import com.jxnx.smartpos.api.engine.OnInstallAppListener;
import com.jxnx.smartpos.api.engine.OnUninstallAppListener;
import com.jxnx.smartpos.api.network.NetWorkHandler;

public class DeviceService extends DeviceServiceEngine.Stub {
    private static DeviceService mInstance;

    public static DeviceService getInstance() {
        if (mInstance == null) {
            synchronized (DeviceService.class) {
                if (mInstance == null) {
                    mInstance = new DeviceService();
                }
            }
        }
        return mInstance;
    }

    @Override
    public int login(Bundle bundle, String s) throws RemoteException {
        return 0;
    }

    @Override
    public void logout() throws RemoteException {

    }

    @Override
    public Bundle getDevInfo() throws RemoteException {
        return null;
    }

    @Override
    public Beeper getBeeper() throws RemoteException {
        return new BeepDriver();
    }

    @Override
    public LEDDriver getLEDDriver() throws RemoteException {
        return new LedDriver();
    }

    @Override
    public Printer getPrinter() throws RemoteException {
        return new PrintDriver();
    }

    @Override
    public InnerScanner getInnerScanner() throws RemoteException {
        return null;
    }

    @Override
    public ModemDriver getModemDriver() throws RemoteException {
        return null;
    }

    @Override
    public SerialPortDriver getSerialPortDriver(int i) throws RemoteException {
        return null;
    }

    @Override
    public MagCardReader getMagCardReader() throws RemoteException {
        return new MagCardDriver();
    }

    @Override
    public IccCardReader getIccCardReader(int slotNo) throws RemoteException {
        byte slot;

        switch (slotNo) {
            case IccReaderSlot.ICSlOT1:
                slot = 0;                               // SLOT_USER
                break;
            case IccReaderSlot.PSAMSlOT1:
                slot = 1;                               // SLOT_PSAM
                break;
            case IccReaderSlot.RFSlOT:
                if (PiccReader.getInstance().Open() == RspCode.RSPOK) {
                    return new IccCardDriver(IccCardDriver.TYPE_PICC);
                }
            default:
                return null;
        }

        if (IccReader.getInstance().SelectSlot(slot) == RspCode.RSPOK) {
            return new IccCardDriver(IccCardDriver.TYPE_ICC);
        } else {
            return null;
        }
    }

    @Override
    public CPUCardHandler getCPUCardHandler(IccCardReader iccCardReader) throws RemoteException {
        return new CpuCardDriver(iccCardReader);
    }

    @Override
    public M1CardHandler getM1CardHandler(IccCardReader iccCardReader) throws RemoteException {
        return new M1CardDriver(iccCardReader);
    }

    @Override
    public PinPad getPinPad() throws RemoteException {
        return new PinPadDriver();
    }

    @Override
    public EmvHandler getEmvHandler() throws RemoteException {
        return new EmvDriver();
    }

    @Override
    public NetWorkHandler getNetWorkHandler() throws RemoteException {
        return null;
    }

    @Override
    public HsmDevice getHsmDevice() throws RemoteException {
        return null;
    }

    @Override
    public void setSystemClock(String s) throws RemoteException {

    }

    @Override
    public void setIMEStatus(int i) throws RemoteException {

    }

    @Override
    public void installApp(String s, OnInstallAppListener onInstallAppListener) throws RemoteException {

    }

    @Override
    public Bundle authDevice(String s) throws RemoteException {
        return null;
    }

    @Override
    public CashBoxDriver getCashBoxDriver() throws RemoteException {
        return null;
    }

    @Override
    public IndustryCardHandler getIndustryCardHandler(IccCardReader iccCardReader) throws RemoteException {
        return null;
    }

    @Override
    public void uninstallApp(String s, OnUninstallAppListener onUninstallAppListener) throws RemoteException {

    }

    @Override
    public void updateSystem(String s, int i) throws RemoteException {

    }

    @Override
    public void reboot() throws RemoteException {

    }
}
