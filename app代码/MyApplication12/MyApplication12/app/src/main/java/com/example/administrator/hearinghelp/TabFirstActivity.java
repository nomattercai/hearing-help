package com.example.administrator.hearinghelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class TabFirstActivity extends AppCompatActivity{

    /**
     * Called when the activity is first created.
     */
    private long mExitTime;
    private TextView sound_text1;
    private TextView sound_text2;

    int Left = 0;
    int Right = 0;
    Button translate;

    Button btnRecord, btnStop;
    boolean isRecording = false;//是否录放的标记
    private Button mBtnceshi;
    static final int frequency = 44100;
    static final int channelInConfiguration = AudioFormat.CHANNEL_IN_STEREO;
    static final int channelOutConfiguration = AudioFormat.CHANNEL_OUT_STEREO;


    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;//PCM 16位编码
    //主要适用于所有设备 实际参数上应该提高保证质量
    int recBufSize, playBufSize;
    AudioRecord audioRecord;
    AudioTrack audioTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_first);
        //setTitle("助听器");
        recBufSize = AudioRecord.getMinBufferSize(frequency,
                channelInConfiguration, audioEncoding);

        playBufSize = AudioTrack.getMinBufferSize(frequency,
                channelOutConfiguration, audioEncoding);
        // -----------------------------------------

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                channelInConfiguration, audioEncoding, recBufSize);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
                channelOutConfiguration, audioEncoding,
                playBufSize, AudioTrack.MODE_STREAM);
        mBtnceshi = (Button) findViewById(R.id.ceshi);
        translate = (Button) findViewById(R.id.translate);
        //------------------------------------------

        btnRecord = (Button) this.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new ClickEvent());
        btnStop = (Button) this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new ClickEvent());
        audioTrack.setStereoVolume(1f, 1f);//设置当前音量大小

        sound_text1 = (TextView) findViewById(R.id.sound_text1);
        sound_text2 = (TextView) findViewById(R.id.sound_text2);

        mBtnceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabFirstActivity.this, hearing_testActivity.class);
                startActivity(intent);
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("userInfo", TabFirstActivity.MODE_PRIVATE);
                Left = preferences.getInt("Left_level",0);
                Right = preferences.getInt("Right_level",0);
                sound_text1.setText("左耳的分贝为："+ Left);
                sound_text2.setText("右耳的分贝为："+ Right);
                float vol_left = Left/100;
                float vol_right = Right/100;
                vol_left = 2f;
                vol_right = 2f;
                audioTrack.setStereoVolume(vol_left, vol_right);//设置音量
            }
        });
    }
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Object mHelperUtils;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    TabFirstActivity.this.finish();
                }
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == btnRecord) {
                isRecording = true;
                Toast.makeText(getApplicationContext(),"正在进行声音放大",Toast.LENGTH_SHORT).show();
                try {
                    new RecordPlayThread().start();// 开一条线程边录边放
                }catch(Throwable t)
                {
                    Toast.makeText(TabFirstActivity.this, t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            } else if (v == btnStop) {
                isRecording = false;
                Toast.makeText(getApplicationContext(),"结束声音放大",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class RecordPlayThread extends Thread {
        public void run() {

            byte[] buffer = new byte[recBufSize];
            audioRecord.startRecording();//开始录制
            audioTrack.play();//开始播放

            while (isRecording) {
                //从MIC保存数据到缓冲区
                int bufferReadResult = audioRecord.read(buffer, 0, recBufSize);
                byte[] tmpBuf = new byte[bufferReadResult];
                System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
                //写入数据即播放
                audioTrack.write(tmpBuf, 0, tmpBuf.length);
                audioTrack.flush();
            }
            audioTrack.stop();
            audioRecord.stop();

        }
    }
}
