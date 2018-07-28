package com.danian.postest;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danian.hardware.CpuCardDriver;
import com.danian.hardware.DeviceService;
import com.danian.hardware.DriverUtils.HelpUtils;

import com.jxnx.smartpos.api.device.beeper.BeepModeConstrants;
import com.jxnx.smartpos.api.device.reader.icc.IccCardReader;
import com.jxnx.smartpos.api.device.reader.icc.IccCardType;
import com.jxnx.smartpos.api.device.reader.icc.IccReaderSlot;
import com.jxnx.smartpos.api.device.reader.icc.OnSearchIccCardListener;
import com.jxnx.smartpos.api.ServiceResult;

import java.util.Arrays;

public class TestIccCardActivity extends AppCompatActivity {
    private final DeviceService mDevice = DeviceService.getInstance();
    private IccCardReader iccReader;
    private CpuCardDriver cpuDriver;
    private TextView tvTestIcc;
    private Button btTestIcc;

    private final OnSearchIccCardListener mListener = new OnSearchIccCardListener.Stub() {
        @Override
        public void onSearchResult(int result, Bundle bundle) throws RemoteException {
            switch (result) {
                case ServiceResult.Success:
                    cpuDriver = (CpuCardDriver) mDevice.getCPUCardHandler(iccReader);
                    testCpuCard();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_icc);

        tvTestIcc = (TextView) findViewById(R.id.tv_card_test);
        btTestIcc = (Button) findViewById(R.id.bt_card_exit);
        btTestIcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            iccReader = mDevice.getIccCardReader(IccReaderSlot.ICSlOT1);
            if (iccReader != null) {
                String[] cardType = new String[]{IccCardType.CPUCARD};
                iccReader.searchCard(mListener, 0, cardType);
                btTestIcc.setText("请插入IC卡");
            } else {
                btTestIcc.setBackgroundResource(R.drawable.button_fail);
                btTestIcc.setText("初始化失败");
                btTestIcc.setEnabled(true);
                MainActivity.testIcc = MainActivity.RESULT_FAIL;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            iccReader.stopSearch();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    private void testCpuCard() {
        try {
            byte[] rev = new byte[50];
            if (cpuDriver.setPowerOn(rev)) {
                String str = "复位应答：\n" + new String(rev) + "\n\n\n随机数：\n";
                tvTestIcc.setText(str);

                Arrays.fill(rev, (byte) 0);
                if (cpuDriver.getIccChallenge(8, rev)) {
                    cpuDriver.setPowerOff();
                    mDevice.getBeeper().beep(BeepModeConstrants.SUCCESS);
                    str += HelpUtils.asciiToString(rev, 0, 16);
                    tvTestIcc.setText(str);

                    btTestIcc.setBackgroundResource(R.drawable.button_success);
                    btTestIcc.setText("读卡成功");
                    btTestIcc.setEnabled(true);
                    MainActivity.testIcc = MainActivity.RESULT_SUCCESS;
                    return;
                }
            }

            cpuDriver.setPowerOff();
            btTestIcc.setBackgroundResource(R.drawable.button_fail);
            btTestIcc.setText("读卡失败");
            btTestIcc.setEnabled(true);
            MainActivity.testIcc = MainActivity.RESULT_FAIL;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
