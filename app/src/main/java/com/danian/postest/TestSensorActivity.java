package com.danian.postest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danian.hardware.DriverInterface.RespCode;
import com.danian.hardware.DriverUtils.HelpUtils;
import com.danian.hardware.SystemDriver;

public class TestSensorActivity extends AppCompatActivity {
    private TextView tvTestSensor;
    private Button btTestSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sensor);

        tvTestSensor = (TextView) findViewById(R.id.tv_sensor_test);
        btTestSensor = (Button) findViewById(R.id.bt_sensor_exit);
        btTestSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String str = "返回码：\n";
        byte[] rev = new byte[30];
        byte[] send = HelpUtils.stringToHex("020008011371040000000E0362");
        int len = SystemDriver.comTtyTrims(send, rev, 1000);
        if (len != RespCode.RESPERR) {
            tvTestSensor.setText(str + rev[6]);
            switch (rev[6]) {
                case 0:
                    btTestSensor.setText("激活成功");
                    break;
                case -1:
                    btTestSensor.setText("已激活");
                    break;
                case -2:
                    btTestSensor.setText("SENSOR有开路");
                    break;
                case -6:
                    btTestSensor.setText("模块已锁定");
                    break;
            }
            btTestSensor.setBackgroundResource(R.drawable.button_success);
            MainActivity.testSensor = MainActivity.RESULT_SUCCESS;
        } else {
            btTestSensor.setBackgroundResource(R.drawable.button_fail);
            btTestSensor.setText("检查失败");
            MainActivity.testSensor = MainActivity.RESULT_FAIL;
        }
    }
}
