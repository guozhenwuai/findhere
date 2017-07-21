package com.FindHere.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.FindHere.control.Connect;
import com.FindHere.model.Comment;
import com.FindHere.model.User;

import static android.R.attr.bitmap;
import static android.content.ContentValues.TAG;
import static com.FindHere.activity.R.id.imageView;
import static com.FindHere.activity.R.id.userhead;

public class EditUserActivity extends Activity {
    private EditText username;
    private  ImageButton userheader;
    private  EditText gender;
    private  EditText QQ;
    private  EditText weixin;
    private ImageButton save;
    private  ImageButton back;
    private static int RESULT_LOAD_IMAGE = 1;
    private SharedPreferences sp;


    private String image;
    private Bitmap bitmap;
    private byte[] newuserhead;
    private  String newusername;
    private  String newweixin;
    private  String newQQ;
    private  String newgender;
    final User user=new  User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        sp = getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);

        username =(EditText) findViewById(R.id.username);
        userheader=(ImageButton)findViewById(userhead);
        gender=(EditText)findViewById(R.id.gender);
        weixin=(EditText)findViewById(R.id.weixin);
        QQ=(EditText)findViewById(R.id.QQ);
        save=(ImageButton)findViewById(R.id.save);
        back=(ImageButton)findViewById(R.id.back) ;
        Drawable username_drawable= getResources().getDrawable(R.drawable.user);
/// 这一步必须要做,否则不会显示.
        username_drawable.setBounds(0, 0, username_drawable.getMinimumWidth(), username_drawable.getMinimumHeight());
        username.setCompoundDrawables(username_drawable,null,null,null);
        image = sp.getString("image","");
        if(!image.equals("")){
            Log.d(TAG, "has head");
            byte[] bytes = Base64.decode(image,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            userheader.setImageBitmap(bitmap);

        }
        username.setText( sp.getString("username",""));
        weixin.setText(sp.getString("weixin",""));
        QQ.setText(sp.getString("QQ",""));
        gender.setText(sp.getString("gender",""));



        userheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(username.getText().toString()!=null)newusername=username.getText().toString();
                 if(weixin.getText().toString()!=null)newweixin=weixin.getText().toString();

                    Log.d(TAG, "onClick: "+QQ.getText().toString());
                    newQQ= QQ.getText().toString();
                 if(gender.getText()!=null)newgender=gender.getText().toString();

                user.setId(sp.getString("userID",""));
                user.setName(newusername);
                user.setUserhead(bitmap);
                user.setWeixin(newweixin);
                user.setQQ(newQQ);

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Connect myConnect = new Connect(EditUserActivity.this);
                        String key =null;


                        String restr=myConnect.sendFile(getString(R.string.updateuser_url),user);
                        //  restr=myConnect.sendImage(getString(R.string.addimage_url),newcom);
                        Log.d("OK", restr);
                  /*  Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    mHandler.sendMessage(msg);*/
                    }}).start();
            }
        });
    back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           finish();

        }
    });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:OK "+requestCode+" "+resultCode+" "+(data==null));
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            final Bitmap image= BitmapFactory.decodeFile(picturePath);
            bitmap=image;



            // String picturePath contains the path of selected Image
        }
    }
}
