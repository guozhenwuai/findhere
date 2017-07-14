package com.FindHere.control;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connect {
    private SharedPreferences sp;
    private String sessionid;

    public Connect(Context mContext){
        sp = mContext.getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        sessionid = sp.getString("sessionId", "");
    }

    private boolean isConnected(){
        return !(sessionid.equals(""));
    }

    public String sendHttpPost(String getUrl, String jsonstr) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        String str=null;
        try {
            url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            if(isConnected()) {
                urlConnection.setRequestProperty("Cookie",sessionid);
            }
            urlConnection.setConnectTimeout(3000);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();
            OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(jsonstr);
            bw.flush();
            out.close();
            bw.close();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                str=buffer.toString();
                // 取得sessionid.
                String cookieval = urlConnection.getHeaderField("Set-Cookie");
                if (!cookieval.equals("")) {
                    sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("sessionId", sessionid);
                    editor.commit();
                }
                in.close();
                br.close();
            }
        } catch (Exception e) {

        } finally{
            urlConnection.disconnect();
            return str;
        }
    }

}
