package com.example.administrator.hearinghelp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;

public class TabSecondActivity extends Activity {
    private long mExitTime;
    private Button BtnStart;
    private TextView Text0;
    private final String APP_ID = "=5dc7e01a";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_second);
        BtnStart = (Button)findViewById(R.id.btn_start);
        Text0 = (TextView) findViewById(R.id.text_0);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + APP_ID);

        BtnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initSpeech(TabSecondActivity.this);
            }
        });


    }
    public void initSpeech(final Context context) {
        // 创建RecognizerDialog对象
        final RecognizerDialog mDialog = new RecognizerDialog(context, null);
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        final StringBuilder mStringBuilder = new StringBuilder();
        // 设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                String resultString = recognizerResult.getResultString();
                System.out.println("讯飞识别的结果 " + resultString);
                System.out.println("b参数是什么 " + b);
                String content = parseData(resultString);
                System.out.println("解析后的数据 " + content);
                mStringBuilder.append(content);
                if (b) {
                    String result = mStringBuilder.toString();
                    System.out.println(result);
                    //String answer = "不好意思,你说的我没有听清楚！！！";
                    String answer = result;
                    Text0.setText(answer);
                    //show(answer);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                System.out.println("错误码 " + speechError);
            }
        });
        mDialog.show();
    }

    public void show(String result) {
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端,这些功能用到了讯飞服务器,所以要有网络
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        mTts.startSpeaking(result, null);
    }

    private String parseData(String resultString) {
        Gson gson = new Gson();
        XFBean xfbean = gson.fromJson(resultString, XFBean.class);
        ArrayList<XFBean.WS> ws = xfbean.ws;
        StringBuilder stringBuilder = new StringBuilder();
        for (XFBean.WS w : ws) {
            String text = w.cw.get(0).w;
            stringBuilder.append(text);
        }
        return stringBuilder.toString();

    }public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                TabSecondActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
class XFBean{
    public ArrayList<WS> ws;
    public class WS{
        public ArrayList<CW> cw;
    }
    public class CW{
        public String w;
    }
}
