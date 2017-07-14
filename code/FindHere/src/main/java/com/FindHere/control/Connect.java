package com.FindHere.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

    // get string and return
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

    // get jsonStr+bytes and save them in sharedpreferences,return boolean
    public boolean sendHttpPostByte(String getUrl, String jsonstr) {
        HttpURLConnection urlConnection = null;
        URL url = null;
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
                InputStream inStream = urlConnection.getInputStream();
                String rawJson = "";
                int ch = 0;
                if((char)(ch = inStream.read()) != '{') {
                    return false;
                }
                rawJson += (char)ch;//'{'

                while((ch = inStream.read()) != -1 && (char)ch != '}') {
                    rawJson += (char)ch;
                }
                rawJson += (char)ch;//'}'

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buff = new byte[4096];
                int rc = 0;
                while ((rc = inStream.read(buff, 0, buff.length)) > 0) {
                    baos.write(buff, 0, rc);
                }
                byte[] bytes = baos.toByteArray();
                String strImage = Base64.encodeToString(bytes, Base64.DEFAULT);

                JSONObject jsonObj = new JSONObject(rawJson);
                String status = jsonObj.getString("status");
                if (status.equals("success")){
                    String userName = jsonObj.getString("userName");
                    String gender = jsonObj.getString("gender");
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userName",userName);
                    editor.putString("gender",gender);
                    editor.putString("image",strImage);
                    editor.commit();
                }
                else{return false;}
                // 取得sessionid.
                String cookieval = urlConnection.getHeaderField("Set-Cookie");
                if (!cookieval.equals("")) {
                    sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("sessionId", sessionid);
                    editor.commit();
                }
                inStream.close();
                baos.close();
            }else{
                return false;
            }
        } catch (Exception e) {

        } finally{
            urlConnection.disconnect();
            return true;
        }
    }
}
