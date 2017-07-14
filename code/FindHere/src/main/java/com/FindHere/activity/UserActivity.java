package com.FindHere.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class UserActivity extends Activity {
    private SharedPreferences sp;
    private ImageButton backBtn;
    private View logOut;
    private String userName;
    private String gender;
    private String image;
    private Bitmap bitmap;

    private ImageView userhead;
    private TextView username;
    private ImageView gender_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        userhead = findViewById(R.id.userhead);

        sp = getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        userName = sp.getString("userName","");
        gender = sp.getString("gender","");
        image = sp.getString("image","");
        if(!image.equals("")){
            byte[] bytes = Base64.decode(image,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            userhead.setImageBitmap(bitmap);
        }
        username = findViewById(R.id.username);
        username.setText(userName);
        gender_view = findViewById(R.id.gender);
        if(gender.equals("male")){
            gender_view.setImageResource(R.drawable.male);
        }else if(gender.equals("female")) {
            gender_view.setImageResource(R.drawable.female);
        }else{
            gender_view.setVisibility(View.GONE);
        }
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logOut = findViewById(R.id.log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                finish();
                Intent intent = new Intent();
                intent.setClass(UserActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
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