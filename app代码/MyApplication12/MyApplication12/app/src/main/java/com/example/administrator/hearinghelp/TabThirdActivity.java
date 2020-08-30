package com.example.administrator.hearinghelp;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.example.administrator.hearinghelp.bean.GoodsInfo;
import com.example.administrator.hearinghelp.RecyclerExtras.OnItemClickListener;
import com.example.administrator.hearinghelp.RecyclerExtras.OnItemDeleteClickListener;
import com.example.administrator.hearinghelp.RecyclerExtras.OnItemLongClickListener;
public class TabThirdActivity extends AppCompatActivity implements OnItemClickListener, OnItemLongClickListener, OnItemDeleteClickListener{
    // 按钮声明
    private RecyclerView rv_linear;
    private LinearDynamicAdapter mAdapter;
    private ArrayList<GoodsInfo> mPublicArray;
    private ArrayList<GoodsInfo> mAllArray;

    private long mExitTime;
    Button btnStart;
    Button btnStop;
    Switch mSwitch3;
    TextView mtext3;

    // 录音相关控件声明
    MediaRecorder mediaRecorder;
    boolean isRecording;
    String fileName;
    String filePath;
    String audioSaveDir;
    final String[] res = new String[5];
    //private MediaPlayer mediaPlayer=new MediaPlayer();

    public boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_third);

        rv_linear = (RecyclerView) findViewById(R.id.rv_linear);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.VERTICAL);
        rv_linear.setLayoutManager(manager);

        mAllArray = GoodsInfo.getDefaultList();
        mPublicArray = GoodsInfo.getDefaultList1();
        mAdapter = new LinearDynamicAdapter(this, mPublicArray);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.setOnItemDeleteClickListener(this);

        rv_linear.setAdapter(mAdapter);
        rv_linear.setItemAnimator(new DefaultItemAnimator());
        rv_linear.addItemDecoration(new SpacesItemDecoration(1));

        // 控件初始化
        mSwitch3 = (Switch) findViewById(R.id.switch3);
        mtext3 = (TextView) findViewById(R.id.text3);

        audioSaveDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        isRecording = false;
        mediaRecorder = null;
        fileName = "";
        filePath = "";

        mSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    mtext3.setText("环境音识别关闭");
                    exit = false;
                    f();
                }
                else
                {
                    mtext3.setText("环境音识别开启");
                    exit = true;
                };
            }
        });
    }

    public void f()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!exit) {
                    startRecord();
                    while (!exit) {
                        int ratio =mediaRecorder.getMaxAmplitude()/600;
                        int db = 0;
                        if (ratio > 1) {
                            db = (int) (20 * Math.log10(ratio));
                            if(db>=10)
                                break;
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    stopRecord();
                    if(!exit) {
                        f1();
                    }
                }
            }
        }).start();
    }

    public void f1() {
        isRecording = true;
        startRecord();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isRecording = false;
        stopRecord();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openBaidu();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                if(res[0].equals("cat"))
                    position = 2;
                if(res[0].equals("vehicle"))
                    position = 3;
                if(res[0].equals("knock"))
                    position = 4;
                if(res[0].equals("Token_error"))
                    position = 1;
                if(res[0].equals("NoTagFind"))
                    position = 0;
                SimpleDateFormat formatter   =   new   SimpleDateFormat   ("HH:mm:ss");
                Date curDate =  new Date(System.currentTimeMillis());//获取当前时间
                String   str   =   formatter.format(curDate);
                GoodsInfo old_item = mAllArray.get(position);
                GoodsInfo new_item = new GoodsInfo(old_item.pic_id, old_item.title, str);
                mPublicArray.add(0, new_item);
                mAdapter.notifyItemInserted(0);
                rv_linear.scrollToPosition(0);
            }
        });
    }
    /* 开始录音 */
    public void startRecord(){
        if (mediaRecorder == null)
            mediaRecorder = new MediaRecorder();

        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  // 设置麦克风
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 设置输出格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);  // 设置音频文件编码
            fileName = DateFormat.format("yyyyMMdd_HHmmss", java.util.Calendar.getInstance(Locale.CHINA)) + ".mp3";
            filePath = audioSaveDir + '/'+fileName;
            File file= new File(filePath);
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 停止录音 */
    public void stopRecord(){
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }catch (RuntimeException e){
            e.printStackTrace();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
    public void openBaidu(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                //第一步建立httpurlconnection实例
                try {
                    URL url = new URL("https://www.baidu.com");
                    main m = new main();
                    try
                    {
                        res[0] =m.soundDetectTest(filePath);
                    }catch(Exception e)
                    {
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                TabThirdActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(View view, int position) {
        GoodsInfo item = mPublicArray.get(position);
        item.bPressed = !item.bPressed;
        mPublicArray.set(position, item);
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        String desc = String.format("您点击了第%d项，标题是%s", position + 1,
                mPublicArray.get(position).title);
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDeleteClick(View view, int position) {
        mPublicArray.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

}




