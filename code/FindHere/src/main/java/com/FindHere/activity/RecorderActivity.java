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

    public void play() {

        // Get the file we want to playback.

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/reverseme.pcm");

        // Get the length of the audio stored in the file (16 bit so 2 bytes per short)

        // and create a short array to store the recorded audio.

        int musicLength = (int)(file.length()/2);

        short[] music = new short[musicLength];





        try {

            // Create a DataInputStream to read the audio data back from the saved file.

            InputStream is = new FileInputStream(file);

            BufferedInputStream bis = new BufferedInputStream(is);

            DataInputStream dis = new DataInputStream(bis);



            // Read the file into the music array.

            int i = 0;

            while (dis.available() > 0) {

                music[i] = dis.readShort();

                i++;

            }





            // Close the input streams.

            dis.close();





            // Create a new AudioTrack object using the same parameters as the AudioRecord

            // object used to create the file.

            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,

                    11025,

                    AudioFormat.CHANNEL_CONFIGURATION_MONO,

                    AudioFormat.ENCODING_PCM_16BIT,

                    musicLength*2,

                    AudioTrack.MODE_STREAM);

            // Start playback

            audioTrack.play();



            // Write the music buffer to the AudioTrack object

            audioTrack.write(music, 0, musicLength);



            audioTrack.stop() ;



        } catch (Throwable t) {

            Log.e("AudioTrack","Playback Failed");

        }

    }



    public void record() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(100);

        int frequency = 11025;

        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;

        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/reverseme.pcm");



        // Delete any previous recording.

        if (file.exists())

            file.delete();





        // Create the new file.

        try {

            file.createNewFile();

        } catch (IOException e) {

            throw new IllegalStateException("Failed to create " + file.toString());

        }



        try {

            // Create a DataOuputStream to write the audio data into the saved file.

            OutputStream os = new FileOutputStream(file);

            BufferedOutputStream bos = new BufferedOutputStream(os);

            DataOutputStream dos = new DataOutputStream(bos);



            // Create a new AudioRecord object to record the audio.

            int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration,  audioEncoding);

            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,

                    frequency, channelConfiguration,

                    audioEncoding, bufferSize);



            short[] buffer = new short[bufferSize];

            audioRecord.startRecording();



            isRecording = true ;

            while (isRecording) {

                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);

                for (int i = 0; i < bufferReadResult; i++)

                    dos.writeShort(buffer[i]);

            }
            audioRecord.stop();

            dos.close();



        } catch (Throwable t) {

            Log.e("AudioRecord","Recording Failed");

        }

    }


    private void cancelRecord(){

    }
    private  void finishRecord(){

    }
    private  boolean upload(){
            return true;
    }

}
