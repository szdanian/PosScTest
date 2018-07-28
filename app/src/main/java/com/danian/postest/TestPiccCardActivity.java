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

public class TestPiccCardActivity extends AppCompatActivity {
    private final DeviceService mDevice = DeviceService.getInstance();
    private IccCardReader piccReader;
    private CpuCardDriver cpuDriver;
    private TextView tvTestPicc;
    private Button btTestPicc;

    private final OnSearchIccCardListener mListener = new OnSearchIccCardListener.Stub() {
        @Override
        public void onSearchResult(int result, Bundle bundle) throws RemoteException {
            switch (result) {
                case ServiceResult.Success:
                    cpuDriver = (CpuCardDriver) mDevice.getCPUCardHandler(piccReader);
                    testPiccCard();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_picc);

        tvTestPicc = (TextView) findViewById(R.id.tv_picc_test);
        btTestPicc = (Button) findViewById(R.id.bt_picc_exit);
        btTestPicc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            piccReader = mDevice.getIccCardReader(IccReaderSlot.RFSlOT);
            if (piccReader != null) {
                String[] cardType = new String[]{IccCardType.CPUCARD};
                piccReader.searchCard(mListener, 0, cardType);
                btTestPicc.setText("请放置卡片");
            } else {
                btTestPicc.setBackgroundResource(R.drawable.button_fail);
                btTestPicc.setText("初始化失败");
                btTestPicc.setEnabled(true);
                MainActivity.testIcc = MainActivity.RESULT_FAIL;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            piccReader.stopSearch();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    private void testPiccCard() {
        try {
            byte[] rev = new byte[50];
            rev[0] = cpuDriver.getPiccCardTypeFunction();
            String str = "类型：\n" + new String(rev) + "\n\n\n";
            int len = cpuDriver.getPiccCardSNFunction(rev);
            str += "SN号：\n" + HelpUtils.hexToString(rev, len);
            tvTestPicc.setText(str);

            cpuDriver.setPowerOff();
            mDevice.getBeeper().beep(BeepModeConstrants.SUCCESS);

            btTestPicc.setBackgroundResource(R.drawable.button_success);
            btTestPicc.setText("读卡成功");
            btTestPicc.setEnabled(true);
            MainActivity.testPicc = MainActivity.RESULT_SUCCESS;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
