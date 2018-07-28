package com.danian.postest;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danian.hardware.DeviceService;

import com.jxnx.smartpos.api.device.beeper.BeepModeConstrants;
import com.jxnx.smartpos.api.device.reader.mag.MagCardInfoEntity;
import com.jxnx.smartpos.api.device.reader.mag.OnSearchMagCardListener;
import com.jxnx.smartpos.api.ServiceResult;

public class TestMagCardActivity extends AppCompatActivity {
    private final DeviceService mDevice = DeviceService.getInstance();
    private TextView tvTestMagcard;
    private Button btTestMagcard;

    private final OnSearchMagCardListener mListener = new OnSearchMagCardListener.Stub() {
        @Override
        public void onSearchResult(int result, MagCardInfoEntity infoEntity) throws RemoteException {
            switch (result) {
                case ServiceResult.Success:
                    mDevice.getBeeper().beep(BeepModeConstrants.SUCCESS);
                    String str = "I 磁道：\n" + infoEntity.getTk1() + "\n\nII 磁道：\n"
                            + infoEntity.getTk2() + "\n\nIII 磁道：\n" + infoEntity.getTk3();
                    tvTestMagcard.setText(str);

                    btTestMagcard.setBackgroundResource(R.drawable.button_success);
                    btTestMagcard.setText("读卡成功");
                    MainActivity.testMagcard = MainActivity.RESULT_SUCCESS;
                    break;
                case ServiceResult.MagCardReader_Base_Error:
                    btTestMagcard.setBackgroundResource(R.drawable.button_fail);
                    btTestMagcard.setText("读卡失败");
                    MainActivity.testMagcard = MainActivity.RESULT_FAIL;
                    break;
            }
            btTestMagcard.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_magcard);

        tvTestMagcard = (TextView) findViewById(R.id.tv_magcard_test);
        tvTestMagcard.setMovementMethod(ScrollingMovementMethod.getInstance());

        btTestMagcard = (Button) findViewById(R.id.bt_magcard_exit);
        btTestMagcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            if (mDevice.getMagCardReader().searchCard(mListener, 0) == ServiceResult.Success) {
                btTestMagcard.setText("请刷磁条卡");
            } else {
                btTestMagcard.setBackgroundResource(R.drawable.button_fail);
                btTestMagcard.setText("初始化失败");
                btTestMagcard.setEnabled(true);
                MainActivity.testMagcard = MainActivity.RESULT_FAIL;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!btTestMagcard.isEnabled()) {
            try {
                mDevice.getMagCardReader().stopSearch();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onBackPressed();
    }
}
