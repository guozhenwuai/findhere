package com.FindHere.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.FindHere.control.Connect;
import com.FindHere.imageUtil;
import com.FindHere.model.Comment;
import com.unity3d.player.UnityPlayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.content.ContentValues.TAG;
import static com.FindHere.activity.R.id.visible;

public class MainActivity extends Activity{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private LinearLayout u3dLayout,addMenu;
    private ImageButton userBtn,cameraBtn,addBtn,setBtn,textBtn,voiceBtn,imageBtn;
    private RelativeLayout loadLayout;
    private SharedPreferences sp;
    private static String targetID;
    private String returnStr="";
    private String sessionid;
    private  String restr;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_RECORD = 2;

    private ImageButton showcom;

    private SeekBar com_num;
    private  LinearLayout set_menu;
    private TextView seekBtn,location;
    private boolean ComShow=false;
    private  static int comment_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        sp = getSharedPreferences("userInfo", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);

        mUnityPlayer = new UnityPlayer(this);
        u3dLayout =  findViewById(R.id.unity3d);
        //u3dLayout.addView(cameraClose);
        u3dLayout.addView(mUnityPlayer);
        mUnityPlayer.resume();
        mUnityPlayer.requestFocus();
        userBtn =  findViewById(R.id.user_btn);
        cameraBtn =  findViewById(R.id.camera_btn);
        addBtn =  findViewById(R.id.add_btn);
        seekBtn = findViewById(R.id.showAllcom_btn);
        showcom=findViewById(R.id.showcom_btn);
        setBtn = findViewById(R.id.set_btn);
        loadLayout =  findViewById(R.id.loading_layout);

        addMenu=findViewById(R.id.add_menu);
        textBtn=findViewById(R.id.text);
        voiceBtn=findViewById(R.id.sound);
        imageBtn=findViewById(R.id.image);
        com_num=findViewById(R.id.com_num);
        location=findViewById(R.id.location);
        set_menu=findViewById(R.id.set_menu);
        location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,LocationActivity.class);
                startActivity(intent);
            }
        });
        userBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        /**cameraBtn.setOnClickListener(new OnClickListener() {
          *  @Override
          *  public void onClick(View v) {
          *      if(camera_on){
          *          mUnityPlayer.pause();
          *          u3dLayout.removeAllViews();
          *          u3dLayout.addView(cameraClose);
          *          camera_on = false;
          *      }else{
          *          u3dLayout.removeAllViews();
          *          u3dLayout.addView(mUnityPlayer);
          *          mUnityPlayer.resume();
          *          mUnityPlayer.requestFocus();
          *          camera_on = true;
          *      }
          *  }
        *});
         **/
        cameraBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UnityPlayer.UnitySendMessage("CloudRecognition","RestartScanning","");
                addBtn.setVisibility(View.GONE);
                addMenu.setVisibility(View.GONE);
            }
        });
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addMenu.getVisibility() == View.GONE){
                    addMenu.setVisibility(View.VISIBLE);
                }
                else{
                    addMenu.setVisibility(View.GONE);
                }
            }
        });
        textBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //UnityPlayer.UnitySendMessage("ContentManager","GetTargetId","");
                targetID = sp.getString("targetID","");
                sessionid = sp.getString("sessionId","");
                if(sessionid.equals("")){
                    Toast.makeText(MainActivity.this,getString(R.string.no_login), Toast.LENGTH_SHORT).show();
                }
                if(targetID.equals("")){
                    Toast.makeText(MainActivity.this,getString(R.string.no_target), Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                }
            }
        });
        imageBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                targetID = sp.getString("targetID","");
                sessionid = sp.getString("sessionId","");
                if(sessionid.equals("")){
                    Toast.makeText(MainActivity.this,getString(R.string.no_login), Toast.LENGTH_SHORT).show();
                }
                if(targetID.equals("")) {
                    Toast.makeText(MainActivity.this, getString(R.string.no_target), Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);}
            }
        });
        voiceBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                targetID = sp.getString("targetID","");
                sessionid = sp.getString("sessionId","");
                if(sessionid.equals("")){
                    Toast.makeText(MainActivity.this,getString(R.string.no_login), Toast.LENGTH_SHORT).show();
                }
                if(targetID.equals("")){
                    Toast.makeText(MainActivity.this,getString(R.string.no_target), Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, RecorderActivity.class);
                    startActivityForResult(intent, RESULT_RECORD);}
            }
        });

        seekBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                targetID = sp.getString("targetID","");
                if(targetID.equals("")) {
                    Toast.makeText(MainActivity.this, getString(R.string.no_target), Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SeekActivity.class);
                    startActivity(intent);
                }
            }
        });

        setBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(set_menu.getVisibility() == View.GONE){
                    set_menu.setVisibility(View.VISIBLE);
                }
                else{
                    set_menu.setVisibility(View.GONE);
                }
                /*Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                startActivity(intent);*/
            }
        });
        showcom.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View view) {
                targetID = sp.getString("targetID","");
                Log.d(TAG, "onClick: "+targetID);
                if(targetID.equals("")) {
                    Toast.makeText(MainActivity.this,getString(R.string.no_target), Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG, "onClick: showcom"+targetID);

                    if(ComShow==true){ ComShow=false;}
                    else {ComShow=true;}
                    Log.d(TAG, "onClick: showcom1"+ComShow);
                    if(ComShow){showcom.setImageResource(R.drawable.arscan2);}
                    else {showcom.setImageResource(R.drawable.comments);}
                    UnityPlayer.UnitySendMessage("ContentManager", "SwitchModelStatus", "");

                }
            }
        });
        com_num.setMax(20);
        com_num.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.d(TAG, "seekid:"+seekBar.getId()+", progess"+progress);
                SharedPreferences.Editor editor =sp.edit();
                Log.d(TAG, "...onProgressChanged: "+comment_num);
                comment_num=progress;
                Log.d(TAG, "...onProgressChanged: "+comment_num);
                UnityPlayer.UnitySendMessage("ContentManager","ResetInfo",targetID);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

            final Bitmap image= imageUtil.decodeSampledBitmapFromFilePath(picturePath,1024,1024);

            new Thread(new Runnable(){
                @Override
                public void run() {
                    Connect myConnect = new Connect(MainActivity.this);
                    String key =null;
                    Comment newcom=new Comment();
                    newcom.setImage(image);
                    newcom.setType("image");
                    newcom.setTargetId(targetID);



                    Log.d("OK",restr+newcom.toJson());

                    restr=myConnect.sendFile(getString(R.string.addfile_url),newcom);
                    //  restr=myConnect.sendImage(getString(R.string.addimage_url),newcom);
                    Log.d("OK", restr);
                    loadInfoPoint();
                  /*  Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    mHandler.sendMessage(msg);*/
                }}).start();

            // String picturePath contains the path of selected Image
        }

        else if(requestCode==RESULT_RECORD &&resultCode==RESULT_OK && null != data){

            String path=data.getStringExtra("path");
            Log.d(TAG, "onActivityResult0000"+path);

            File file=new File(path);
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1)
                {
                    bos.write(b, 0, n);
                }
                final byte[] wav=bos.toByteArray();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Connect myConnect = new Connect(MainActivity.this);
                        Comment newcom = new Comment();
                        Log.d(TAG, "run:"+wav.length);
                        newcom.setSounds(wav);
                        newcom.setType("sound");
                        newcom.setTargetId(targetID);
                        restr = myConnect.sendFile(getString(R.string.addfile_url), newcom);
                        Log.d(TAG, "run: "+restr);
                        loadInfoPoint();
                    } }).start();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }





        }
    }


    // Unity uses this function to load infoPoint
    protected static void loadInfoPoint(){
        UnityPlayer.UnitySendMessage("ContentManager","LoadInfo",targetID);
    }

    // Unity uses this function to set sp:targetID
    public void setTargetID(String targetID){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("targetID",targetID);
        editor.commit();
        addBtn.setVisibility(View.VISIBLE);
    }

    // Unity uses this function to get {commentID:commentType}
    public String getCommentType(final String targetID,final int pageIndex){
        String ip = getString(R.string.comment_type)+"?targetID="+targetID+"&pageNum="+comment_num+"&pageIndex="+pageIndex;
        Connect myConnect = new Connect(MainActivity.this);
        returnStr=myConnect.sendHttpPost(ip,"");

        return returnStr;
    }

    public void setToast(String message){
        Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show();
    }

    // Unity uses this function to get comment content
    public String getCommentContent(final String commentID){
        new Thread(new Runnable(){
            @Override
            public void run() {
                String ip = getString(R.string.comment_content)+"?commentID="+commentID;
                Connect myConnect = new Connect(MainActivity.this);
                returnStr=myConnect.sendHttpPost(ip,"");
            }
        }).start();
        return returnStr;
    }
    @Override
    protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        //mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        //mUnityPlayer.resume();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   {
     /**   if (keyCode == KeyEvent.KEYCODE_BACK) {
       *     onDestroy();
       *     return true;}
       * else{return mUnityPlayer.injectEvent(event);}
       */
     return mUnityPlayer.injectEvent(event);
      }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }

    /**
     * 3D调用此方法，用于退出3D
     */
    public void makePauseUnity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mUnityPlayer != null) {
                    try {
                        mUnityPlayer.quit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MainActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
