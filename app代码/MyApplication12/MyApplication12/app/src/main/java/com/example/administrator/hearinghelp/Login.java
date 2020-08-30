package com.example.administrator.hearinghelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.example.administrator.hearinghelp.adduser_login.adduser;

public class Login extends AppCompatActivity {
    private Button mBtnzhuce;
    private ImageButton mBtnBack;
    private EditText mEtUserName;
    private EditText mEtUserCode;
    private String UserName;
    private String UserCode;
    String choose="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mBtnzhuce = (Button) findViewById(R.id.btn_zhuce);
        mEtUserName = (EditText) findViewById(R.id.et_3);
        mEtUserCode = (EditText) findViewById(R.id.et_4);
        mBtnBack = (ImageButton) findViewById(R.id.btn_back2);

        //mBtnzhuce.setEnabled(false);
        mBtnzhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUser();
                UserName = mEtUserName.getText().toString();
                UserCode = mEtUserCode.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putInt("account", Integer.parseInt(UserName));
                editor.putInt("password", Integer.parseInt(UserCode));
                editor.commit();//提交修改
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(choose.equals("accept")) {
                    Intent intent = new Intent(Login.this, Main2Activity.class);
                    Login.this.startActivity(intent);
                    Login.this.finish();
                    Toast.makeText(Login.this,"注册成功",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Login.this,"请重试",Toast.LENGTH_LONG).show();
                }
            }
        });
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.this.finish();
            }
        });

    }

    public void AddUser()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserName = mEtUserName.getText().toString();
                UserCode = mEtUserCode.getText().toString();
                choose = adduser(UserName,UserCode);
            }
        }).start();
    }
}
