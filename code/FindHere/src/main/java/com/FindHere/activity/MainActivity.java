package com.FindHere.activity;

import com.unity3d.player.UnityPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends UnityPlayerActivity {
    private LinearLayout u3dLayout;
    private Button historyBtn,addBtn;
    private ImageButton scanBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_overlay);
        u3dLayout = (LinearLayout) findViewById(R.id.unity);
        u3dLayout.addView(mUnityPlayer);
        mUnityPlayer.requestFocus();
        scanBtn = (ImageButton) findViewById(R.id.scan_btn);
        historyBtn = (Button) findViewById(R.id.history_btn);
        addBtn = (Button) findViewById(R.id.add_com_btn);

        scanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UnityPlayer.UnitySendMessage("ForAndroid", "sayHello", "");
            }
        });
        historyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UnityPlayer.UnitySendMessage("Manager", "ZoomOut", "");
            }
        });
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UnityPlayer.UnitySendMessage("Manager", "ZoomOut", "");
            }
        });
    }

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
    /**
     * 按键点击事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onDestroy();
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //UnityPlayer.UnitySendMessage("Manager", "Unload", "");
        mUnityPlayer.quit();
    }
    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }
    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // mUnityPlayer.quit();
        // this.finish();
    }
}
