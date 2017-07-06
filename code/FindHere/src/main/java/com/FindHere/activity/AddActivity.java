package com.FindHere.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.FindHere.control.Connect;
import com.FindHere.model.Comment;

import org.json.JSONException;
import org.json.JSONObject;

public class AddActivity extends Activity {
    private ImageButton commitBtn;
    public static Comment thiscomment;
    public String ip;
    private String jsonStr;
    private String returnStr;
    private ImageButton backBtn;

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
    }

    public  Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what==1) {
                ((EditText)findViewById(R.id.upload)).setText("");
                nmsgbox(returnStr);
            }
        }
    };

    public void nmsgbox(String msg)
    {
        new AlertDialog.Builder(this).setTitle(getString(R.string.prompt)).setMessage(msg)
                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        ip = getString(R.string.add_ip);
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commitBtn=findViewById(R.id.commit_btn);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=((EditText)findViewById(R.id.upload)).getText().toString();
                if(text.equals(""))
                {
                    msgbox(getString(R.string.not_empty));
                    return;
                }
                //type text targetID
                JSONObject object = new JSONObject();
                try {
                    object.put("type", "text");
                    object.put("text", text);
                    object.put("targetID","1");
                    jsonStr = object.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Connect myConnect = new Connect(AddActivity.this);
                        returnStr=myConnect.sendHttpPost(ip,jsonStr);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }}).start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
