package com.FindHere.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.unity3d.player.UnityPlayer;

public class MainActivity extends Activity{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private LinearLayout u3dLayout,addMenu;
    private ImageButton userBtn,cameraBtn,addBtn,seekBtn,setBtn,textBtn,musicBtn,voiceBtn,imageBtn;
    private RelativeLayout loadLayout;
    private View scanLine;
    private ImageView cameraClose;

    private boolean camera_on = false;
    private boolean addflag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mUnityPlayer = new UnityPlayer(this);
        cameraClose = new ImageView(this);
        cameraClose.setImageResource(R.drawable.baoman);
        u3dLayout =  findViewById(R.id.unity3d);
        u3dLayout.addView(cameraClose);
        userBtn =  findViewById(R.id.user_btn);
        cameraBtn =  findViewById(R.id.camera_btn);
        addBtn =  findViewById(R.id.add_btn);
        seekBtn = findViewById(R.id.showcom_btn);
        setBtn = findViewById(R.id.set_btn);
        loadLayout =  findViewById(R.id.loading_layout);
        scanLine = findViewById(R.id.scan_line);

        addMenu=findViewById(R.id.add_menu);
        textBtn=findViewById(R.id.text);
        musicBtn=findViewById(R.id.music);
        voiceBtn=findViewById(R.id.sound);
        imageBtn=findViewById(R.id.image);


        userBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        cameraBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camera_on){
                    mUnityPlayer.pause();
                    u3dLayout.removeAllViews();
                    u3dLayout.addView(cameraClose);
                    camera_on = false;
                }else{
                    u3dLayout.removeAllViews();
                    u3dLayout.addView(mUnityPlayer);
                    mUnityPlayer.resume();
                    mUnityPlayer.requestFocus();
                    camera_on = true;
                }
            }
        });
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addMenu.getVisibility() == View.GONE){
                    addMenu.setVisibility(View.VISIBLE);
                }
                else{
                    addMenu.setVisibility(View.GONE);
                }
               // UnityPlayer.UnitySendMessage("ForAndroid", "sayHello", "");
            }
        });
        textBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddActivity.class);
                startActivity(intent);
                // UnityPlayer.UnitySendMessage("ForAndroid", "sayHello", "");
            }
        });
        seekBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SeekActivity.class);
                startActivity(intent);
            }
        });
        setBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

     protected void kill(){}

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onDestroy();
            return true;}
        else{return mUnityPlayer.injectEvent(event);} }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }

    /**
     * 3D调用此方法，用于退出3D
     */
    public void makePauseUnity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mUnityPlayer != null) {
                    try {
                        mUnityPlayer.quit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MainActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**  @Override
      public void onWindowFocusChanged(boolean hasFocus) {
          super.onWindowFocusChanged(hasFocus);

          int[] location = new int[2];

          scanLine.getLocationInWindow(location);

          int left = scanLine.getLeft();
          int top = scanLine.getTop();
          int bottom = scanLine.getBottom();

          // 从上到下的平移动画
          Animation verticalAnimation = new TranslateAnimation(left, left, top, bottom);
          verticalAnimation.setDuration(3000); // 动画持续时间
          verticalAnimation.setRepeatCount(Animation.INFINITE); // 无限循环

          // 播放动画
          scanLine.setAnimation(verticalAnimation);
          verticalAnimation.startNow();
      }*/
}
