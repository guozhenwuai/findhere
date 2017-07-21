package com.FindHere.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.FindHere.control.Connect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends Activity {
    private Button commitBtn;
    private ImageButton backBtn;
    private String ip;
    private String email;
    private String password;
    private EditText mUserNameView;
    private Spinner mGenderView;
    private String userName;
    private String gender;
    private String jsonStr;
    private String returnStr;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        sp = getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUserNameView = findViewById(R.id.username);
        mGenderView = findViewById(R.id.gender);
        mGenderView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                gender = (String) mGenderView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ip = getString(R.string.register_ip);
        commitBtn = findViewById(R.id.register_button);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = mUserNameView.getText().toString();

                // Reset errors.
                mUserNameView.setError(null);

                boolean cancel = false;
                View focusView = null;

                // Check for a valid password, if the user_activity entered one.
                if (!TextUtils.isEmpty(userName) && !isUserNameValid(userName)) {
                    mUserNameView.setError(getString(R.string.error_invalid_username));
                    focusView = mUserNameView;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("email", email);
                        object.put("password",password);
                        object.put("userName", userName);
                        object.put("gender",gender);
                        jsonStr = object.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            Connect myConnect = new Connect(RegisterActivity.this);
                            returnStr=myConnect.sendHttpPost(ip,jsonStr);
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }}).start();
                }
            }
        });
    }

    //只能输入由数字、26个英文字母或者下划线组成的字符串
    private boolean isUserNameValid(String username) {
        String all = "^\\w+";
        Pattern pattern = Pattern.compile(all);
        return pattern.matches(all,username);
    }


    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what==1) {
                String strNew = returnStr.substring(returnStr.indexOf("{"),returnStr.indexOf("}")+1);
                String status = "";
                try {
                    JSONObject jsonObj = new JSONObject(strNew);
                    status = jsonObj.getString("status");
                }catch(JSONException e){
                    e.printStackTrace();
                }
                if(status.equals("success")){
                    msgbox1(returnStr);
                }
                else{
                    msgbox2(returnStr);
                }
            }
        }
    };

    public void msgbox1(String msg)
    {
        new AlertDialog.Builder(this).setTitle(getString(R.string.prompt)).setMessage(msg)
                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userName",userName);
                        editor.putString("gender",gender);
                        editor.putString("userID",email);
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this,UserActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }

    public void msgbox2(String msg)
    {
        new AlertDialog.Builder(this).setTitle(getString(R.string.prompt)).setMessage(msg)
                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Toast.makeText(RegisterActivity.this,getString(R.string.register_failed), Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}
