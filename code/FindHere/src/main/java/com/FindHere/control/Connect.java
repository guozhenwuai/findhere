package com.FindHere.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

public class Connect {
    private SharedPreferences sp;
    private String sessionid;

    public Connect(Context mContext){
        sp = mContext.getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        sessionid = sp.getString("sessionId", "");
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
            Log.d("exception:",Log.getStackTraceString(e));
        } finally{
            urlConnection.disconnect();
            return str;
        }
    }



    public String sendImage(String urlHost, Comment com){
        String endString = "\r\n";
        String twoHyphen = "--";
        String boundary = "*****";
        try {
            URL url = new URL(urlHost);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if(isConnected()) {
                con.setRequestProperty("cookie", sessionid);
            }
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            con.setRequestMethod("POST");

            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);



            if(!isConnected()) {
                String cookieval = con.getHeaderField("set-cookie");
                if (cookieval != null) {
                    sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("sessionId", sessionid);
                    editor.commit();
                }
            }
            DataOutputStream dsDataOutputStream = new DataOutputStream(con.getOutputStream());
            dsDataOutputStream.writeBytes(twoHyphen + boundary + endString);
            dsDataOutputStream.writeBytes("Content-Disposition:form-data;"+endString);


            Log.d(TAG, "sendImage:"+endString);
            dsDataOutputStream.write(com.getImage(), 0, com.getImage().length);

            dsDataOutputStream.writeBytes(endString);
            dsDataOutputStream.writeBytes(twoHyphen + boundary + twoHyphen + endString);

            dsDataOutputStream.close();

            int cah = con.getResponseCode();
            if (cah == 200) {
                InputStream isInputStream = con.getInputStream();
                int ch;
                StringBuffer buffer = new StringBuffer();
                while ((ch = isInputStream.read()) != -1) {
                    buffer.append((char) ch);
                }
                return buffer.toString();
            } else {
                return "false";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }



}
