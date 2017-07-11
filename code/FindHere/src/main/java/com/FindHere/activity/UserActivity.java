package com.FindHere.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;

public class UserActivity extends Activity {
    private SharedPreferences sp;
    private ImageButton backBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        sp = getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        String sessionid = sp.getString("sessionId","");
        msgbox(sessionid);

        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void msgbox(String msg)
    {
        new AlertDialog.Builder(this).setTitle(getString(R.string.prompt)).setMessage(msg)
                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}