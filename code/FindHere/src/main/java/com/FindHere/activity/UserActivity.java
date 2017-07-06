package com.FindHere.activity;

import android.app.Activity;
<<<<<<< HEAD
<<<<<<< HEAD
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class UserActivity extends Activity {
    private SharedPreferences sp;
    private ImageButton backBtn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        sp = getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        String email = sp.getString("email","");

        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

=======
import android.content.Intent;
=======
import android.content.Context;
import android.content.SharedPreferences;
>>>>>>> 7b58409da72ac5278f14fca7bf2e7629dc777aad
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class UserActivity extends Activity {
    private SharedPreferences sp;
    private ImageButton backBtn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        sp = getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        String email = sp.getString("email","");

        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

<<<<<<< HEAD
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
>>>>>>> ca52eed5bc47122568a2bd22ed6800a01317010b
=======
>>>>>>> 7b58409da72ac5278f14fca7bf2e7629dc777aad
}
