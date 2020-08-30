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

import static com.example.administrator.hearinghelp.adduser_login.getlevel;
import static com.example.administrator.hearinghelp.adduser_login.login;

public class Enter extends AppCompatActivity {
    private Button mBtnLogin;
    private ImageButton mBtnBack;
    private EditText mEtUserName;
    private EditText mEtUserCode;
    private Button mBtnzhuce;
    private String UserName;
    private String UserCode;
    String choose="";
    String level_text = "";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnzhuce = (Button) findViewById(R.id.btn_jinruzhuce);
        mEtUserName = (EditText) findViewById(R.id.et_1);
        mEtUserCode = (EditText) findViewById(R.id.et_2);
        mBtnBack = (ImageButton) findViewById(R.id.btn_back1);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enter();
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
                    Intent intent = new Intent(Enter.this, Main2Activity.class);
                    Enter.this.startActivity(intent);
                    Enter.this.finish();
                    Toast.makeText(Enter.this,"登入成功",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Enter.this,"请重试",Toast.LENGTH_LONG).show();
                }
                if(!level_text.equals("server getlevel error"))
                {
                    if(!level_text.equals("server error"))
                    {
                        if(!level_text.equals("connect error"))
                        {
                            String[] le = level_text.split(" ");
                            int left_level=Integer.parseInt(le[0]);
                            int right_level=Integer.parseInt(le[1]);
                            System.out.println(left_level+"!!!"+right_level);
                            editor.putInt("Left_level",left_level);
                            editor.putInt("Right_level",right_level);
                            editor.commit();
                        }
                    }}
               /* Intent intent = new Intent(Enter.this, Main2Activity.class);
                Enter.this.startActivity(intent);
                Enter.this.finish();*/
            }
        });

        mBtnzhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Enter.this,Login.class);
                Enter.this.startActivity(intent);
            }
        });
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enter.this.finish();
            }
        });
    }
    public void Enter()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserName = mEtUserName.getText().toString();
                UserCode = mEtUserCode.getText().toString();
                choose = login(UserName,UserCode);
                level_text = getlevel(UserName);

        }}).start();
    }
}
