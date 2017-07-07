package com.FindHere.control;

import android.content.Context;
import android.content.SharedPreferences;

import com.FindHere.activity.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class TargetControl {
    private SharedPreferences sp;
    private String sessionid;
    Context myContext;
    String returnStr;

    public void TargetControl(Context mContext){
        sp = mContext.getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        myContext = mContext;
    }

    // Unity uses this function to set sp:targetID
    public void setTargetID(String targetID){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("targetID",targetID);
        editor.commit();
    }

    // Unity uses this function to get {commentID:commentType}
    public String getCommentType(final String targetID,final int pageIndex){
        new Thread(new Runnable(){
            @Override
            public void run() {
                String ip = myContext.getResources().getString(R.string.comment_type)+"?targetID="+targetID+"&pageNum=20&pageIndex="+pageIndex;
                returnStr=sendHttpPost(ip,"");
            }}).start();
        return returnStr;
    }

    // Unity uses this function to get comment content
    public String getCommentContent(final String commentID){
        new Thread(new Runnable(){
            @Override
            public void run() {
                String ip = myContext.getResources().getString(R.string.comment_content)+"?commentID="+commentID;
                returnStr=sendHttpPost(ip,"");
            }}).start();
        return returnStr;
    }

    public boolean isConnected(){
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
                urlConnection.setRequestProperty("cookie", sessionid);
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
            // 取得sessionid.
            if(!isConnected()) {
                String cookieval = urlConnection.getHeaderField("set-cookie");
                if (cookieval != null) {
                    sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("sessionId", sessionid);
                    editor.commit();
                }
            }
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
