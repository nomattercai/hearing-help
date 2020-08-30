package com.example.administrator.hearinghelp;

import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.PopupWindow;

public class Main2Activity extends TabActivity implements View.OnClickListener {
    private static final String TAG = "TabHostActivity";
    private Bundle mBundle = new Bundle();
    private TabHost tab_host;
    private LinearLayout ll_first, ll_second, ll_third;
    private String FIRST_TAG = "first";
    private String SECOND_TAG = "second";
    private String THIRD_TAG = "third";
    private TextView textView3;
    private ImageButton user_interface;
    private PopupWindow popupWindow;

    final private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    final private int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mBundle.putString("tag", TAG);
        ll_first = (LinearLayout) findViewById(R.id.ll_first);
        ll_second = (LinearLayout) findViewById(R.id.ll_second);
        ll_third = (LinearLayout) findViewById(R.id.ll_third);
        ll_first.setOnClickListener(this);
        ll_second.setOnClickListener(this);
        ll_third.setOnClickListener(this);
        tab_host = getTabHost();
        tab_host.addTab(getNewTab(FIRST_TAG, R.string.menu_first,
                R.drawable.tab_first_selector, TabFirstActivity.class));
        tab_host.addTab(getNewTab(SECOND_TAG, R.string.menu_second,
                R.drawable.tab_second_selector, TabSecondActivity.class));
        tab_host.addTab(getNewTab(THIRD_TAG, R.string.menu_third,
                R.drawable.tab_third_selector, TabThirdActivity.class));
        tab_host.setCurrentTabByTag(FIRST_TAG);
        changeContainerView(ll_first);
        user_interface = (ImageButton) findViewById(R.id.user_interface);
        user_interface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPopupWindow();
                popupWindow.showAsDropDown(v);
            }
        });



        }

    private TabHost.TabSpec getNewTab(String spec, int label, int icon, Class<?> cls) {
        Intent intent = new Intent(this, cls).putExtras(mBundle);
        return tab_host.newTabSpec(spec).setContent(intent)
                .setIndicator(getString(label), getResources().getDrawable(icon));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_first || v.getId() == R.id.ll_second || v.getId() == R.id.ll_third) {
            changeContainerView(v);
        }
    }

    private void changeContainerView(View v) {
        ll_first.setSelected(false);
        ll_second.setSelected(false);
        ll_third.setSelected(false);
        v.setSelected(true);
        if (v == ll_first) {
            textView3 = (TextView) findViewById(R.id.textView3);
            textView3.setText("声音放大");
            tab_host.setCurrentTabByTag(FIRST_TAG);
            int permissionCheck_1 = ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(permissionCheck_1== PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else if (v == ll_second) {
            textView3 = (TextView) findViewById(R.id.textView3);
            textView3.setText("语音翻译");
            tab_host.setCurrentTabByTag(SECOND_TAG);
            int permissionCheck_3 = ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.RECORD_AUDIO);
            if(permissionCheck_3== PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            }
        } else if (v == ll_third) {
            textView3 = (TextView) findViewById(R.id.textView3);
            textView3.setText("环境音识别");
            tab_host.setCurrentTabByTag(THIRD_TAG);
            int permissionCheck_3 = ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.RECORD_AUDIO);
            if(permissionCheck_3== PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            }
        }
    }

    protected void initPopuptWindow() {
// TODO Auto-generated method stub
// 获取自定义布局文件pop.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.pop_layout, null, false);
// 创建PopupWindow实例,200,150分别是宽度和高度
        popupWindow = new PopupWindow(popupWindow_view, 300, 1000, true);
// 设置动画效果
        popupWindow.setAnimationStyle(R.style.AnimationFade);
//点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
// TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
// pop.xml视图里面的控件
        Button enter = (Button) popupWindow_view.findViewById(R.id.user_enter);
        Button login = (Button) popupWindow_view.findViewById(R.id.user_login);
        Button quit = (Button) popupWindow_view.findViewById(R.id.user_quit);
// pop.xml视图里面的控件触发的事件
// 打开
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
// 这里可以执行相关操作
// 对话框消失
                Intent mainIntent = new Intent(Main2Activity.this, Enter.class);
                Main2Activity.this.startActivity(mainIntent);
                popupWindow.dismiss();
            }
        });
// 保存
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
// 这里可以执行相关操作
                Intent mainIntent = new Intent(Main2Activity.this, Login.class);
                Main2Activity.this.startActivity(mainIntent);
                popupWindow.dismiss();
            }
        });
// 关闭
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
// 这里可以执行相关操作
                Main2Activity.this.finish();
                popupWindow.dismiss();
            }
        });
    }

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }


}
