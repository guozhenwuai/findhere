package com.FindHere.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;


public class Comment {
    private String type;
    private int id;
    private String userId;
    private String userName;
    private String targetId;
    private String contentId;
    private String text;
    private byte[] image;
    private byte[] sounds;
    private String time;
    private byte[] targetImage;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String getUserId(){return userId;}

    public void setUserId(String userId){this.userId = userId;}

    public String getUserName(){return userName;}

    public void setUserName(String userName){this.userName = userName;}

    public String getTargetId(){return targetId;}

    public void setTargetId(String targetId){this.targetId = targetId;}

    public String getContentId(){return contentId;}

    public void setContentId(String contentId){this.contentId = contentId;}

    public String getText(){return text;}

    public void setText(String text){this.text = text;}


    public byte[] getImage(){
        return image;
    }
    public void setImage(Bitmap image){
            this.image= Bitmap2Bytes(image);
    }

    // target image
    public byte[] getTargetImage(){
        return targetImage;
    }
    public void setTargetImage(byte[] targetImage){
        this.targetImage= targetImage;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public Bitmap getImageBitmap(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        return bitmap;
    }

    // target image
    public Bitmap getTargetImageBitmap(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(targetImage,0,targetImage.length);
        return bitmap;
    }

    public byte[] getSounds() {
        return sounds;
    }

    public void setSounds( byte[] sound) {
        this.sounds = sound;
    }

    public String toJson(){
        JSONObject object = new JSONObject();
        String jsonStr=null;
        try {
            object.put("type", this.getType());
            object.put("userid",this.getUserId());
            object.put("targetID",this.getTargetId());
            object.put("contentID",this.getContentId());
            object.put("text", this.getText());
           // object.put("width", 34);
            //object.put("height", 23);
           /* if(this.getType().equals("image")){

                 String imagestring= android.util.Base64.encodeToString(this.getImage(), android.util.Base64.DEFAULT);

                //String imagestring = new String(this.getImage());
                 Log.d("OK", imagestring+imagestring.length());

                //imagestring=compress(imagestring);
                 object.put("file",imagestring);
            }
            else if(this.getType().equals("sound")){
             /*   String soundstring=new Base64().encodeToString( this.getSounds());*/

               /* object.put("file",this.getSounds());

            }*/
            jsonStr = object.toString();

            return jsonStr;
        } catch (JSONException e) {
            e.printStackTrace();
            return jsonStr;
        }
    }

    private String compress(String data){
            String finalData=null;
           try{
                     //打开字节输出流
               ByteArrayOutputStream bout=new ByteArrayOutputStream();
                    //打开压缩用的输出流,压缩后的结果放在bout中
               GZIPOutputStream gout=new GZIPOutputStream(bout);
                     //写入待压缩的字节数组
               gout.write(data.getBytes("ISO-8859-1"));
                     //完成压缩写入
               gout.finish();
                     //关闭输出流
               gout.close();
               finalData=bout.toString("ISO-8859-1");
                }catch(Exception e){
                     e.printStackTrace();
                 }
             return finalData;
         }

    public boolean hasText(){return !(text==null);}

    public boolean hasMusic(){return !(sounds==null);}

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

}
