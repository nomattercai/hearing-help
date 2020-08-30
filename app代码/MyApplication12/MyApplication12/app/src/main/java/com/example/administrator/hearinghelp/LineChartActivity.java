package com.example.administrator.hearinghelp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.administrator.hearinghelp.adduser_login.setlevel;
/**
 * Created by John.Doe on 2020/3/3.
 */

public class LineChartActivity extends Activity {

    private ChartView chartView1;
    private ChartView chartView2;


    private String[] leftResult;
    private String[] rightResult;

    private Button exitBtn;
    private Button saveBtn;

    private int left = 0;
    private int right = 0;
    String choose="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);
        exitBtn = (Button) findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn = (Button) findViewById(R.id.save_btn);
        f();
        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
           public void onClick(View v) {
                SetLevel();
                if (choose.equals("setlevel error"))
                {
                    Toast.makeText(LineChartActivity.this,"设置错误",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(LineChartActivity.this,"设置成功",Toast.LENGTH_LONG).show();
                }
            }
        });
        init();
    }

    private void f()
    {
        for (int i = 0; i < hearing_testActivity.LeftResult.length; i++)
        {
            left +=  hearing_testActivity.LeftResult[i];
        }
        left = left/8;
        for (int i = 0; i < hearing_testActivity.RightResult.length; i++)
        {
            right +=  hearing_testActivity.RightResult[i];
        }
        right = right/8;
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putInt("Left_level", left);
        editor.putInt("Right_level", right);
        editor.commit();//提交修改
    }

    private void init() {
        chartView1 = (ChartView) findViewById(R.id.chart_view1);
        chartView2 = (ChartView) findViewById(R.id.chart_view2);


        leftResult = new String[8];
        rightResult = new String[8];
        setData();
    }

    private void setData() {
        for (int i = 0; i < hearing_testActivity.LeftResult.length; i++)
            leftResult[i] = Integer.toString(hearing_testActivity.LeftResult[i]);
        chartView1.setTitle("左耳听力检测结果");
        chartView1.setData(leftResult);
        chartView1.fresh();

        for (int i = 0; i < hearing_testActivity.RightResult.length; i++)
            rightResult[i] = Integer.toString(hearing_testActivity.RightResult[i]);
        chartView2.setTitle("右耳听力检测结果");
        chartView2.setData(rightResult);
        chartView2.fresh();
    }

    private void SetLevel()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("userInfo", LineChartActivity.MODE_PRIVATE);
                int account = preferences.getInt("account",0);
                choose = setlevel(String.valueOf(account),left,right);
            }
        }).start();
    }

}
