package com.danian.hardware;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.jxnx.smartpos.api.emv.EmvAidPara;
import com.jxnx.smartpos.api.emv.EmvCapk;
import com.jxnx.smartpos.api.emv.EmvHandler;
import com.jxnx.smartpos.api.emv.OnEmvProcessListener;

import java.util.List;

public class EmvDriver extends EmvHandler.Stub {

    @Override
    public int emvProcess(Bundle bundle, OnEmvProcessListener onEmvProcessListener) throws RemoteException {
        return 0;
    }

    @Override
    public int initTermConfig(Bundle bundle) throws RemoteException {
        return 0;
    }

    @Override
    public int setAidParaList(List<EmvAidPara> list) throws RemoteException {
        return 0;
    }

    @Override
    public int setAidParaTlvList(List<String> list) throws RemoteException {
        return 0;
    }

    @Override
    public int setCAPKList(List<EmvCapk> list) throws RemoteException {
        return 0;
    }

    @Override
    public int setCAPKTlvList(List<String> list) throws RemoteException {
        return 0;
    }

    @Override
    public void delAidPara() throws RemoteException {

    }

    @Override
    public void delCapkPara() throws RemoteException {

    }

    @Override
    public List<EmvCapk> getCapkList() throws RemoteException {
        return null;
    }

    @Override
    public List<EmvAidPara> getAidParaList() throws RemoteException {
        return null;
    }

    @Override
    public byte[] getTlvs(byte[] bytes, int i) throws RemoteException {
        return new byte[0];
    }

    @Override
    public int setTlv(byte[] bytes, byte[] bytes1) throws RemoteException {
        return 0;
    }

    @Override
    public int getEmvCardLog(int i, OnEmvProcessListener onEmvProcessListener) throws RemoteException {
        return 0;
    }

    @Override
    public int clearLog() throws RemoteException {
        return 0;
    }

    @Override
    public int emvGetEcBalance(OnEmvProcessListener onEmvProcessListener, int i) throws RemoteException {
        return 0;
    }

    @Override
    public void onSetSelAppResponse(int i) throws RemoteException {

    }

    @Override
    public void onSetConfirmCardNoResponse(boolean b) throws RemoteException {

    }

    @Override
    public void onSetPinInputResponse(boolean b, boolean b1) throws RemoteException {

    }

    @Override
    public void onSetCertVerifyResponse(boolean b) throws RemoteException {

    }

    @Override
    public void onSetOnlineProcResponse(int i, Bundle bundle) throws RemoteException {

    }

    @Override
    public void onSetAIDParameterResponse(EmvAidPara emvAidPara) throws RemoteException {

    }

    @Override
    public void onSetCAPubkeyResponse(EmvCapk emvCapk) throws RemoteException {

    }

    @Override
    public void onSetTRiskManageResponse(String s) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
