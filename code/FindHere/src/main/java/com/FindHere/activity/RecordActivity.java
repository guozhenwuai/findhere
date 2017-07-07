package com.FindHere.activity;

import android.app.Activity;
import android.media.MediaRecorder;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import java.io.File;

/**
 * Created by msi on 2017/7/7.
 */

public class RecordActivity extends Activity {
    private void oncreate(){
    MediaRecorder mediaRecorder = new MediaRecorder();
    // 第1步：设置音频来源（MIC表示麦克风）
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    //第2步：设置音频输出格式（默认的输出格式）
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
    //第3步：设置音频编码方式（默认的编码方式）
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
    //创建一个临时的音频输出文件
    try{
        File  audioFile = File.createTempFile("record_", ".amr");
        //第4步：指定音频输出文件
        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
        //第5步：调用prepare方法
        mediaRecorder.prepare();
        //第6步：调用start方法开始录音
        mediaRecorder.start();
    }
    catch(java.io.IOException e){

    }

}
}
