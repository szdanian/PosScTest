package com.danian.postest;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.danian.hardware.DeviceService;

import com.jxnx.smartpos.api.device.beeper.BeepModeConstrants;

public class TestBeepActivity extends AppCompatActivity implements View.OnClickListener  {
    private final DeviceService mDevice = DeviceService.getInstance();
    private int testFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_beep);

        findViewById(R.id.bt_beep_on).setOnClickListener(this);
        findViewById(R.id.bt_beep_success).setOnClickListener(this);
        findViewById(R.id.bt_beep_fail).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (MainActivity.testBeep != 0) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "请完成蜂鸣器测试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_beep_on:
                try {
                    mDevice.getBeeper().beep(BeepModeConstrants.NORMAL);
                    testFlag = 1;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_beep_success:
                if (testFlag == 1) {
                    MainActivity.testBeep = 1;
                    this.finish();
                } else {
                    Toast.makeText(this, "请完成蜂鸣器测试", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_beep_fail:
                if (testFlag == 1) {
                    MainActivity.testBeep = 2;
                    this.finish();
                } else {
                    Toast.makeText(this, "请完成蜂鸣器测试", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
