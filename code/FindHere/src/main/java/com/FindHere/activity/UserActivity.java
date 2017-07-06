package com.FindHere.activity;

import android.app.Activity;
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

}
