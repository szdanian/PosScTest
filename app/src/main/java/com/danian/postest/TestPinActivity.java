package com.danian.postest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.danian.hardware.DeviceService;
import com.danian.hardware.DriverInterface.KeyCallback;
import com.danian.hardware.DriverUtils.DesUtils;
import com.danian.hardware.DriverUtils.HelpUtils;
import com.danian.hardware.PinDriver;

import com.jxnx.smartpos.api.device.beeper.BeepModeConstrants;

public class TestPinActivity extends AppCompatActivity {
    private final byte[] transmitKey = HelpUtils.stringToHex("11111111111111112222222222222222");
    private final byte[] masterKey = HelpUtils.stringToHex("33333333333333334444444444444444");
    private final byte[] pinKey = HelpUtils.stringToHex("55555555555555556666666666666666");
    private final byte[] macKey = HelpUtils.stringToHex("77777777777777778888888888888888");
    private final byte[] trackKey = HelpUtils.stringToHex("9999999999999999AAAAAAAAAAAAAAAA");
    private final byte[] checkCode = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};

    private final DeviceService mDevice = DeviceService.getInstance();
    private final PinDriver pinDriver = PinDriver.getInstance();
    private ProgressBar progressBar;
    private TextView textView;
    private Button btTestKey;
    private String strText;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressBar.setVisibility(View.GONE);
            switch (msg.what) {
                case PinDriver.KEY_TRANSMIT:
                    strText += "成功！\n正在写主密钥......";
                    textView.setText(strText);
                    testWriteKey();
                    break;
                default:
                    strText += "失败！";
                    textView.setText(strText);
                    btTestKey.setBackgroundResource(R.drawable.button_fail);
                    btTestKey.setText("测试失败");
                    btTestKey.setEnabled(true);
                    MainActivity.testKey = MainActivity.RESULT_FAIL;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_key);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.tv_key_test);
        btTestKey = (Button) findViewById(R.id.bt_key_exit);
        btTestKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        strText = "正在写传输密钥......";
        textView.setText(strText);
        btTestKey.setText("请等待...");
        progressBar.setVisibility(View.VISIBLE);
        setTransmitKey();
    }

    @Override
    public void onBackPressed() {
        if (btTestKey.isEnabled()) {
            super.onBackPressed();
        }
    }

    private void setTransmitKey() {
        pinDriver.setTransmitKey(transmitKey, new KeyCallback() {
            @Override
            public void callKeyResult(boolean result) {
                if (result) {
                    mHandler.sendEmptyMessage(PinDriver.KEY_TRANSMIT);
                } else {
                    mHandler.sendEmptyMessage(-1);
                }
            }
        });
    }

    private void testWriteKey() {
        byte[] encryptKey, check;

        encryptKey = DesUtils.tripleDes(masterKey, transmitKey, DesUtils.ENCRYPT_MODE);
        check = DesUtils.tripleDes(checkCode, masterKey, DesUtils.ENCRYPT_MODE);
        if (pinDriver.setMasterKey(encryptKey, check)) {
            strText += "成功！\n正在写加密密钥......";
            textView.setText(strText);

            encryptKey = DesUtils.tripleDes(masterKey, masterKey, DesUtils.ENCRYPT_MODE);
            if (pinDriver.setWorkKey(PinDriver.KEY_ENCRYPT, encryptKey, check)) {
                strText += "成功！\n正在写PIN密钥......";
                textView.setText(strText);

                encryptKey = pinDriver.calcDes(PinDriver.KEY_ENCRYPT, pinKey, PinDriver.MODE_ENCRYPT);
                check = DesUtils.tripleDes(checkCode, pinKey, DesUtils.ENCRYPT_MODE);
                if (pinDriver.setWorkKey(PinDriver.KEY_PIN, encryptKey, check)) {
                    strText += "成功！\n正在写MAC密钥......";
                    textView.setText(strText);

                    encryptKey = pinDriver.calcDes(PinDriver.KEY_ENCRYPT, macKey, PinDriver.MODE_ENCRYPT);
                    check = DesUtils.tripleDes(checkCode, macKey, DesUtils.ENCRYPT_MODE);
                    if (pinDriver.setWorkKey(PinDriver.KEY_MAC, encryptKey, check)) {
                        strText += "成功！\n正在写Track密钥......";
                        textView.setText(strText);

                        encryptKey = pinDriver.calcDes(PinDriver.KEY_ENCRYPT, trackKey, PinDriver.MODE_ENCRYPT);
                        check = DesUtils.tripleDes(checkCode, trackKey, DesUtils.ENCRYPT_MODE);
                        if (pinDriver.setWorkKey(PinDriver.KEY_TRACK, encryptKey, check)) {
                            strText += "成功！";
                            textView.setText(strText);

                            try {
                                mDevice.getBeeper().beep(BeepModeConstrants.SUCCESS);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            btTestKey.setBackgroundResource(R.drawable.button_success);
                            btTestKey.setText("测试成功");
                            btTestKey.setEnabled(true);
                            MainActivity.testKey = MainActivity.RESULT_SUCCESS;
                            return;
                        }
                    }
                }
            }
        }

        strText += "失败！";
        textView.setText(strText);
        btTestKey.setBackgroundResource(R.drawable.button_fail);
        btTestKey.setText("测试失败");
        btTestKey.setEnabled(true);
        MainActivity.testKey = MainActivity.RESULT_FAIL;
    }
}
