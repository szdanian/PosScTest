package com.danian.postest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.danian.btmanage.BluetoothManage;
import com.danian.hardware.SystemDriver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static int RESULT_NON = 0;
    public static int RESULT_SUCCESS = 1;
    public static int RESULT_FAIL = 2;

    public static int testLed, testBeep, testVer, testIcc;
    public static int testPicc, testMagcard, testSensor;
    public static int testKey, testPrint;

    private Button btTestLed, btTestBeep, btTestVer, btTestIcc;
    private Button btTestPicc, btTestMagcard, btTestSensor;
    private Button btTestKey, btTestPrint;
    private Intent bluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemDriver.systemPowerOn();
        bluetoothService = new Intent(this, BluetoothManage.class);
        startService(bluetoothService);

        findViewById(R.id.bt_magcard_test).setOnClickListener(this);
        findViewById(R.id.bt_sensor_test).setOnClickListener(this);
        findViewById(R.id.bt_key_test).setOnClickListener(this);

        btTestLed = (Button) findViewById(R.id.bt_led_test);
        btTestLed.setOnClickListener(this);
        btTestBeep = (Button) findViewById(R.id.bt_beep_test);
        btTestBeep.setOnClickListener(this);
        btTestVer = (Button) findViewById(R.id.bt_ver_test);
        btTestVer.setOnClickListener(this);
        btTestIcc = (Button) findViewById(R.id.bt_icc_test);
        btTestIcc.setOnClickListener(this);
        btTestPicc = (Button) findViewById(R.id.bt_picc_test);
        btTestPicc.setOnClickListener(this);
        btTestMagcard = (Button) findViewById(R.id.bt_magcard_test);
        btTestMagcard.setOnClickListener(this);
        btTestSensor = (Button) findViewById(R.id.bt_sensor_test);
        btTestSensor.setOnClickListener(this);
        btTestKey = (Button) findViewById(R.id.bt_key_test);
        btTestKey.setOnClickListener(this);
        btTestPrint = (Button) findViewById(R.id.bt_print_test);
        btTestPrint.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (testLed == RESULT_SUCCESS) {
            btTestLed.setBackgroundResource(R.drawable.button_success);
        } else if (testLed == RESULT_FAIL) {
            btTestLed.setBackgroundResource(R.drawable.button_fail);
        }

        if (testBeep == RESULT_SUCCESS) {
            btTestBeep.setBackgroundResource(R.drawable.button_success);
        } else if (testBeep == RESULT_FAIL) {
            btTestBeep.setBackgroundResource(R.drawable.button_fail);
        }

        if (testVer == RESULT_SUCCESS) {
            btTestVer.setBackgroundResource(R.drawable.button_success);
        } else if (testVer == RESULT_FAIL) {
            btTestVer.setBackgroundResource(R.drawable.button_fail);
        }

        if (testIcc == RESULT_SUCCESS) {
            btTestIcc.setBackgroundResource(R.drawable.button_success);
        } else if (testIcc == RESULT_FAIL) {
            btTestIcc.setBackgroundResource(R.drawable.button_fail);
        }

        if (testPicc == RESULT_SUCCESS) {
            btTestPicc.setBackgroundResource(R.drawable.button_success);
        } else if (testPicc == RESULT_FAIL) {
            btTestPicc.setBackgroundResource(R.drawable.button_fail);
        }

        if (testMagcard == RESULT_SUCCESS) {
            btTestMagcard.setBackgroundResource(R.drawable.button_success);
        } else if (testMagcard == RESULT_FAIL) {
            btTestMagcard.setBackgroundResource(R.drawable.button_fail);
        }

        if (testSensor == RESULT_SUCCESS) {
            btTestSensor.setBackgroundResource(R.drawable.button_success);
        } else if (testSensor == RESULT_FAIL) {
            btTestSensor.setBackgroundResource(R.drawable.button_fail);
        }

        if (testKey == RESULT_SUCCESS) {
            btTestKey.setBackgroundResource(R.drawable.button_success);
        } else if (testKey == RESULT_FAIL) {
            btTestKey.setBackgroundResource(R.drawable.button_fail);
        }

        if (testPrint == RESULT_SUCCESS) {
            btTestPrint.setBackgroundResource(R.drawable.button_success);
        } else if (testPrint == RESULT_FAIL) {
            btTestPrint.setBackgroundResource(R.drawable.button_fail);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(bluetoothService);
        SystemDriver.systemPowerOff();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("请确定是否要退出？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_beep_test:
                startActivity(new Intent(this, TestBeepActivity.class));
                break;
            case R.id.bt_led_test:
                startActivity(new Intent(this, TestLedActivity.class));
                break;
            case R.id.bt_ver_test:
                startActivity(new Intent(this, TestVerActivity.class));
                break;
            case R.id.bt_icc_test:
                startActivity(new Intent(this, TestIccCardActivity.class));
                break;
            case R.id.bt_picc_test:
                startActivity(new Intent(this, TestPiccCardActivity.class));
                break;
            case R.id.bt_magcard_test:
                startActivity(new Intent(this, TestMagCardActivity.class));
                break;
            case R.id.bt_sensor_test:
                startActivity(new Intent(this, TestSensorActivity.class));
                break;
            case R.id.bt_key_test:
                startActivity(new Intent(this, TestPinActivity.class));
                break;
            case R.id.bt_print_test:
                startActivity(new Intent(this, TestPrintActivity.class));
                break;
        }
    }
}
