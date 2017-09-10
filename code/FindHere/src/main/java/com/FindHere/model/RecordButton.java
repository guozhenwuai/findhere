package com.FindHere.model;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import com.FindHere.activity.R;

import java.io.File;

import static android.content.ContentValues.TAG;


@SuppressLint("NewApi")
public class RecordButton extends Button  {

    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setSavePath(String path) {
        File filePath = new File(path);
        if (!filePath.exists()) {
            File file2 = new File(path.substring(0, path.lastIndexOf("/") + 1));
            file2.mkdirs();
        }
        mFileName = path;
    }

    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }

    private String mFileName = null;
    private OnFinishedRecordListener finishedListener;
    private static long startTime;
    private Dialog recordDialog;
    private static int[] res = { R.drawable.record0, R.drawable.record1, R.drawable.record0,
            R.drawable.record1, R.drawable.record0};
    private static ImageView view;
    private MediaRecorder recorder;
    private ObtainDecibelThread thread;
    private Handler volumeHandler;
    private static final int MIN_INTERVAL_TIME = 1*1000;// 1s 最短
    public final static int MAX_TIME = 60*1000 + 500;// 1分钟，最长
    public static String File_Voice = Environment.getExternalStorageDirectory()
            .getPath() + "/acoe/demo/voice";// 录音全部放在这个目录下
    private final String  SAVE_PATH = File_Voice;

    private float y ;

    @SuppressLint("HandlerLeak")
    private void init() {
        volumeHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "handleMessage: ");
                if(msg.what == -100){
                    stopRecording();
                    recordDialog.dismiss();
                }else if(msg.what != -1){
                    Log.d(TAG, "handleMessage: "+msg.what);

                    view.setImageResource(res[msg.what]);
                }
            }
        };
    }

    private AnimationDrawable anim;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        y = event.getY();
        Log.d(TAG, ""+(view==null));
        if(view!=null && y<0){
            Log.d(TAG, "onTouchEvent: <0"+ R.drawable.cancel);
            view.setImageResource(R.drawable.cancel);
            anim.stop();
        }else if(view != null){
            Log.d(TAG, "onTouchEvent: set anim_mic"+ R.drawable.anim_mic);
            view.setImageResource(R.drawable.anim_mic);
            anim = (AnimationDrawable) view.getDrawable();
            anim.start();
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setText("松开发送");
                Log.d(TAG, "onTouchEvent: start record");
                initDialogAndStartRecord();
                anim = (AnimationDrawable) view.getDrawable();
                anim.start();
                break;
            case MotionEvent.ACTION_UP:
                this.setText("按住录音");
                startTimer.cancel(); // 主动松开时取消计时
                recordTimer.cancel(); // 主动松开时取消计时
                if(y>=0 && (System.currentTimeMillis() - startTime <= MAX_TIME)){
                    finishRecord();
                }else if(y<0){  //当手指向上滑，会cancel
                    cancelRecord();
                }
                break;
            case MotionEvent.ACTION_CANCEL: // 异常
                cancelRecord();
                break;
        }

        return true;
    }
    public  void setview(ImageView nview){
        view=nview;
    }



    /**
     * 初始化录音对话框 并 开始录音
     */
    private void initDialogAndStartRecord() {
        startTime = System.currentTimeMillis();
        recordDialog = new Dialog(getContext(), R.style.like_toast_dialog_style);
        //final RelativeLayout recordLay=(RelativeLayout)getLayoutInflater().inflate(R.layout.record);

       if(view!=null) {
           Log.d(TAG, "initDialogAndStartRecord: "+view.getId());
       }
           else{
               view = new ImageView(getContext());
               Log.d(TAG, "0initDialogAndStartRecord: "+view.getId());


        }//
        view.setImageResource(R.drawable.anim_mic);

        ViewGroup parent = (ViewGroup) view.getParent();

        if (parent != null) {

            parent.removeAllViews();

        }
        recordDialog.setContentView(view/*, new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)*/);
        recordDialog.setOnDismissListener(onDismiss);
        LayoutParams lp = recordDialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;

        startRecording();
        recordDialog.show();
    }

    /**
     * 放开手指，结束录音处理
     */
    private void finishRecord() {
        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            volumeHandler.sendEmptyMessageDelayed(-100, 1000);
            view.setImageResource(R.drawable.short1);
            anim.stop();
            File file = new File(mFileName);
            file.delete();
            return;
        }else{
            Log.d(TAG, "finishRecord: ");
            stopRecording();
            recordDialog.dismiss();
        }
        //如果有回调，则发送录音结束回调
        if (finishedListener != null)
            finishedListener.onFinishedRecord(mFileName,(int) (intervalTime/1000));
        Log.d(TAG, "finishRecord: end");
    }

    /**
     * 取消录音对话框和停止录音
     */
    public void cancelRecord() {
        stopRecording();
        recordDialog.dismiss();
        //MyToast.makeText(getContext(), "取消录音！", Toast.LENGTH_SHORT);
        File file = new File(mFileName);
        file.delete();
    }

    //获取类的实例
    ExtAudioRecorder extAudioRecorder; //压缩的录音（WAV）
    /**
     * 执行录音操作
     */
    private void startRecording() {
        // save path
        StringBuilder path = new StringBuilder(SAVE_PATH).append("/tmp_sound_").append(System.currentTimeMillis()).append(".wav");
        setSavePath(path.toString());
        Log.d(TAG, "startRecording: "+path);
        extAudioRecorder = ExtAudioRecorder.getInstanse(false); //未压缩的录音（WAV）
        //设置输出文件
        extAudioRecorder.setOutputFile(mFileName);
        extAudioRecorder.prepare();
        //开始录音
        extAudioRecorder.start();
        startTimer.start();
        //震动提醒
        Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(100);
    }

    /**
     * 录音开始计时器，允许的最大时长倒数10秒时进入倒计时
     */
    private CountDownTimer startTimer = new CountDownTimer(MAX_TIME - 500 - 10000, 1000) { // 50秒后开始倒计时
        @Override
        public void onFinish() {
            recordTimer.start();
        }
        @Override
        public void onTick(long millisUntilFinished) {
        }};


    /**
     * 录音最后10秒倒计时器，倒计时结束发送录音
     */
    private CountDownTimer recordTimer = new CountDownTimer(10000, 1000){
        @Override
        public void onFinish() {
            finishRecord();
        }
        @Override
        public void onTick(long millisUntilFinished) { // 显示倒计时动画
            switch ((int)millisUntilFinished / 1000) {

                case 3:
                    view.setBackgroundResource(R.drawable.time);
                    break;
                case 2:
                    view.setBackgroundResource(R.drawable.time);
                    break;
                case 1:
                    view.setBackgroundResource(R.drawable.time);
                    break;
            }
        }};

    private void stopRecording() {
        if (thread != null) {
            thread.exit();
            thread = null;
        }
        /*if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }*/
        if(extAudioRecorder != null){
            Log.d(TAG, "stopRecording: .1");
            extAudioRecorder.stop();
            extAudioRecorder.release();
            Log.d(TAG, "stopRecording: .2");
        }
    }

    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                if (recorder == null || !running) {
                    break;
                }
                int x = recorder.getMaxAmplitude(); //振幅
                if (x != 0 && y>=0) {
                    int f = (int) (10 * Math.log(x) / Math.log(10));
                    if (f < 15)
                        volumeHandler.sendEmptyMessage(0);
                    else if (f < 22)
                        volumeHandler.sendEmptyMessage(1);
                    else if (f < 29)
                        volumeHandler.sendEmptyMessage(2);
                    else if (f < 38)
                        volumeHandler.sendEmptyMessage(3);
                    else
                        volumeHandler.sendEmptyMessage(4);
                }

                volumeHandler.sendEmptyMessage(-1);
                if(System.currentTimeMillis() - startTime > MAX_TIME){
                    finishRecord();
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private OnDismissListener onDismiss = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            stopRecording();
        }
    };

    public interface OnFinishedRecordListener {
        public void onFinishedRecord(String audioPath, int time);
    }

    class CountDown extends CountDownTimer {

        /**
         * @param millisInFuture
         * @param countDownInterval
         */
        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

    }
}