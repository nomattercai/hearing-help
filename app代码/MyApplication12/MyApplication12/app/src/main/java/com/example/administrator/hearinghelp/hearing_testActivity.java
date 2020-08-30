package com.example.administrator.hearinghelp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class hearing_testActivity extends AppCompatActivity implements OnClickListener {

    private TextView channelTv;
    private TextView hertzTv;
    private TextView decibelTv;
    private Button confirmBtn;

    private AudioManager audioManager;
    private MyAudioPlayer myAudioPlayer;
    private TestThread testThread;

    protected static int LeftResult[];
    protected static int RightResult[];

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing_test);

        init();
    }

    private void init() {
        channelTv = (TextView) findViewById(R.id.channel_tv);
        hertzTv = (TextView) findViewById(R.id.hertz_tv);
        decibelTv = (TextView) findViewById(R.id.decibel_tv);

        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(this);

        LeftResult = new int[8];
        RightResult = new int [8];

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        myAudioPlayer = new MyAudioPlayer();
        testThread = new TestThread();
        testThread.start();
    }

    private void showChart() {
        intent = new Intent(this, LineChartActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View view) {
        testThread.setState();
        myAudioPlayer.stopPureTone();
    }

    class TestThread extends  Thread implements Runnable {

        private final int HZ[] = {125, 250, 500, 1000, 2000, 4000, 6000, 8000};
        private final int DB[] = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90};
        private final int CHANNEL_LEEF = 0;
        private final int CHANNEL_RIGHT = 1;

        private int hz_index = 0;
        private int db_index = 0;

        private boolean state = false;

        @Override
        public void run() {
            while (hz_index != HZ.length) {
                showText(CHANNEL_LEEF);
                myAudioPlayer.playPureTone(CHANNEL_LEEF, HZ[hz_index], DB[db_index]);
                if (!state){
                    db_index++;
                } else {
                    LeftResult[hz_index] = DB[db_index];
                    hz_index++;
                    db_index = 0;
                    state = false;
                }
            }

            hz_index = 0;

            while (hz_index != HZ.length) {
                showText(CHANNEL_RIGHT);
                myAudioPlayer.playPureTone(CHANNEL_RIGHT, HZ[hz_index], DB[db_index]);
                if (!state){
                    db_index++;
                } else {
                    RightResult[hz_index] = DB[db_index];
                    hz_index++;
                    db_index = 0;
                    state = false;
                }
            }
            showChart();
        }

        private void showText(int channel) {
            if (channel == CHANNEL_LEEF)
                channelTv.setText("左耳");
            else
                channelTv.setText("右耳");

            hertzTv.setText(Integer.toString(HZ[hz_index]) + " " + "赫兹");
            decibelTv.setText(Integer.toString(DB[db_index]) + " " + "分贝");
        }

        public void setState() {
            state = true;
        }
    }
}
