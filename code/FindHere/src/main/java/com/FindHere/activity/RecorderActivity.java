package com.FindHere.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.FindHere.model.RecordButton;

import static android.content.ContentValues.TAG;
import static android.view.MotionEvent.ACTION_BUTTON_PRESS;
import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by msi on 2017/7/11.
 */

public class RecorderActivity extends Activity {

    private boolean isRecording = false;


    private Context context=getBaseContext();
    RecordButton Record;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        final Intent data=getIntent();
        Log.d(TAG, "onCreate: Record");
        Record=(RecordButton) findViewById(R.id.begin_record);

        Log.d(TAG, "onCreate: after setview"+RESULT_OK);
        Log.d(TAG, "??"+getCallingActivity());
        this.Record.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int time) {
                Toast.makeText(getApplicationContext(), "录音路径：" + audioPath + "  duration：" + time, Toast.LENGTH_SHORT).show();

                data.putExtra("path",audioPath);
                RecorderActivity.this.setResult(RESULT_OK,data);
                Log.d(TAG, "onFinishedRecord: "+audioPath);
                finish();
                RecorderActivity.this.finish();
                Log.d(TAG, "??? "+getCallingActivity());
                Log.d(TAG, "onFinishedRecord: ");

            }
        });

        /*record_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case ACTION_DOWN:
                        //调用手机震动器短震
                        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
                        vibrator.vibrate(100);
                        record();
                        break;
                    case ACTION_UP:
                        finishRecord();
                        break;
                    case ACTION_CANCEL:
                        break;
                    case ACTION_BUTTON_PRESS:
                        break;

                }
                return false;
            }
        });*/



    }



}
