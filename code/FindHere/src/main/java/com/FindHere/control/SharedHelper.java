package com.FindHere.control;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SharedHelper {
    private Context mContext;

    public SharedHelper() {
    }

    public SharedHelper(Context mContext) {
        this.mContext = mContext;
    }


    //定义一个保存数据的方法
    public void save(String email, String passwd) {
        SharedPreferences sp = mContext.getSharedPreferences("userInfo",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email);
        editor.putString("passwd", passwd);
        editor.commit();
        //Toast.makeText(mContext, "信息已写入SharedPreference中", Toast.LENGTH_SHORT).show();
    }

    //定义一个读取SP文件的方法
    public Map<String, String> read() {
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = mContext.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        data.put("email", sp.getString("email", ""));
        data.put("passwd", sp.getString("passwd", ""));
        return data;
    }
}
