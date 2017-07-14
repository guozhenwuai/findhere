package com.FindHere.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.FindHere.model.Comment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;
import static com.FindHere.activity.R.drawable.comment;

public class Connect  {
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
        Log.d(TAG, "sendHttpPost: ");
        try {
            url = new URL(getUrl);
            Log.d(TAG, "sendHttpPost: 1");
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "sendHttpPost: 2");
            if(isConnected()) {
                urlConnection.setRequestProperty("Cookie",sessionid);
            }
            urlConnection.setConnectTimeout(30000);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(30000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            Log.d(TAG, "sendHttpPost: 13");
            urlConnection.connect();
            Log.d(TAG, "sendHttpPost: 11");
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
            e.printStackTrace();

        } finally{
            urlConnection.disconnect();
            return str;
        }
    }




    public String sendFile(String urlHost, Comment com){
       /* String endString = "\r\n";
        String twoHyphen = "--";
        String boundary = "*****";*/
        try {
            URL url = new URL(urlHost);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if(isConnected()) {
                urlConnection.setRequestProperty("cookie", sessionid);
            }
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setConnectTimeout(3000);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");


            if(!isConnected()) {
                String cookieval = urlConnection.getHeaderField("set-cookie");
                if (cookieval != null) {
                    sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("sessionId", sessionid);
                    editor.commit();
                }
            }
            DataOutputStream dsDataOutputStream = new DataOutputStream(urlConnection.getOutputStream());

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dsDataOutputStream));
            bw.write(com.toJson());
            Log.d(TAG, "send:"+com.toJson());
            bw.flush();
            if(com.getType()=="image"){
            Log.d(TAG, "sendImage:");
            dsDataOutputStream.write(com.getImage(), 0, com.getImage().length);
            }
            else if(com.getType()=="sound"){
                Log.d(TAG, "sendImage:"+com.getSounds().length);
                dsDataOutputStream.write(com.getSounds(), 0, com.getSounds().length);
            }
            dsDataOutputStream.close();
            bw.close();
          /*  dsDataOutputStream.writeBytes(endString);
            dsDataOutputStream.writeBytes(twoHyphen + boundary + twoHyphen + endString);*/

            dsDataOutputStream.close();

            int cah = urlConnection.getResponseCode();
            if (cah == 200) {
                Log.d(TAG, "send: "+cah);
                InputStream isInputStream = urlConnection.getInputStream();
                int ch;
                StringBuffer buffer = new StringBuffer();
                while ((ch = isInputStream.read()) != -1) {
                    buffer.append((char) ch);
                }
                return buffer.toString();
            } else {
                Log.d(TAG, "send: "+cah);
                return "false";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "true";
    }



}
