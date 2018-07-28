package com.danian.postest;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.danian.hardware.DeviceService;

import com.jxnx.smartpos.api.device.led.LEDLightConstrants;

public class TestLedActivity extends AppCompatActivity implements View.OnClickListener {
    private final DeviceService mDevice = DeviceService.getInstance();
    private final int ALL_ON = 1;
    private final int ALL_OFF = 2;
    private int testFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_led);

        findViewById(R.id.bt_all_on).setOnClickListener(this);
        findViewById(R.id.bt_all_off).setOnClickListener(this);
        findViewById(R.id.bt_success).setOnClickListener(this);
        findViewById(R.id.bt_fail).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (MainActivity.testLed != MainActivity.RESULT_NON && testFlag != ALL_ON) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "请继续完成LED灯测试！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_all_on:
                try {
                    mDevice.getLEDDriver().setLed(LEDLightConstrants.RED, true);
                    mDevice.getLEDDriver().setLed(LEDLightConstrants.GREEN, true);
                    mDevice.getLEDDriver().setLed(LEDLightConstrants.YELLOW, true);
                    mDevice.getLEDDriver().setLed(LEDLightConstrants.BLUE, true);
                    testFlag = ALL_ON;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_all_off:
                if (testFlag == ALL_ON) {
                    try {
                        mDevice.getLEDDriver().setLed(LEDLightConstrants.RED, false);
                        mDevice.getLEDDriver().setLed(LEDLightConstrants.GREEN, false);
                        mDevice.getLEDDriver().setLed(LEDLightConstrants.YELLOW, false);
                        mDevice.getLEDDriver().setLed(LEDLightConstrants.BLUE, false);
                        testFlag = ALL_OFF;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "请先选择“全部灯亮”！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_success:
                if (testFlag == ALL_OFF) {
                    MainActivity.testLed = MainActivity.RESULT_SUCCESS;
                    this.finish();
                } else {
                    Toast.makeText(this, "请继续完成LED灯测试！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_fail:
                if (testFlag == ALL_OFF) {
                    MainActivity.testLed = MainActivity.RESULT_FAIL;
                    this.finish();
                } else {
                    Toast.makeText(this, "请继续完成LED灯测试！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
