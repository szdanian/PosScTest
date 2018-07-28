package com.danian.postest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danian.hardware.SystemDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestVerActivity extends AppCompatActivity {
    private TextView textView;
    private Button btTestVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ver);

        textView = (TextView) findViewById(R.id.tv_ver_test);
        textView.setText(readSnAndVer());

        btTestVer = (Button) findViewById(R.id.bt_ver_exit);
        btTestVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (MainActivity.testVer == MainActivity.RESULT_SUCCESS) {
            btTestVer.setBackgroundResource(R.drawable.button_success);
            btTestVer.setText("成功");
        } else {
            btTestVer.setBackgroundResource(R.drawable.button_fail);
            btTestVer.setText("失败");
        }
    }

    private String readSnAndVer() {
        String snAndVer = SystemDriver.sysGetSpVersion();
        String temp = getDeviceSn();

        if (snAndVer != null && temp != null) {
            MainActivity.testVer = MainActivity.RESULT_SUCCESS;
        } else {
            MainActivity.testVer = MainActivity.RESULT_FAIL;
        }

        snAndVer = "应用版本号：\n" + snAndVer + "\n\n\n";
        snAndVer += "设备SN号：\n" + temp;
        return snAndVer;
    }

    private String getDeviceSn() {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method getProp = clazz.getMethod("get", String.class);
            return (String) getProp.invoke(clazz, "gsm.serialno1");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
