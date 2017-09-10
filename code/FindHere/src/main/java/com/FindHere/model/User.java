package com.FindHere.model;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class User {
    private String id;
    private String name;

    private int pictureId;
    private String password;
    private byte[] userhead;
    private  String weixin;
    private String QQ;



    private String gender;


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }



    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
    public int getPictureId(){
        return pictureId;
    }

    public void setPictureId(int pictureId){
        this.pictureId=pictureId;
    }
    public byte[] getUserhead() {
        return userhead;
    }
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public void setUserhead(Bitmap userhead) {
        this.userhead = Bitmap2Bytes(userhead);
    }
    public String toJson(){
        JSONObject userjson=new JSONObject();
        String ret="";
        try {
            userjson.put("ID", id);
            userjson.put("userName", name);
            userjson.put("Password",password);
            userjson.put("weixin",weixin);
            userjson.put("QQ",QQ);
            userjson.put("gender",gender);
            ret=userjson.toString();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return ret;

    }
}
