package com.FindHere.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.FindHere.activity.R;
import com.FindHere.model.Comment;

import java.util.LinkedList;

public class seekAdapter extends BaseAdapter {
    private LinkedList<Comment> mData;
    private Context mContext;
    private AudioTrack trackplayer = null;
    private byte[] bytes;

    public seekAdapter(LinkedList<Comment> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.seek_list,parent,false);

        TextView userID = convertView.findViewById(R.id.userID);

        View content = convertView.findViewById(R.id.content);
        TextView textView = convertView.findViewById(R.id.text_content);
        ImageButton imageButton = convertView.findViewById(R.id.image_content);
        ImageButton soundButton = convertView.findViewById(R.id.sound_content);

        TextView time_view =  convertView.findViewById(R.id.time);

        userID.setText(mData.get(position).getUserId());
        time_view.setText(mData.get(position).getTime());
        String type = mData.get(position).getType();
        if(type.equals("text")){
            textView.setVisibility(View.VISIBLE);
            textView.setText(mData.get(position).getText());
        }else if(type.equals("picture")){
            imageButton.setVisibility(View.VISIBLE);
            String contentID = mData.get(position).getContentId();
            final String ip = mContext.getString(R.string.getcomment_ip)+"?commentID="+contentID;
            //System.out.println(ip);
            // send commentID return picture byte
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Connect myConnect = new Connect(mContext);
                            bytes = myConnect.sendHttpPostMedia(ip);
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
            });
            }else if(type.equals("sound")){
                    soundButton.setVisibility(View.VISIBLE);
                    String contentID = mData.get(position).getContentId();
                    final String ip = mContext.getString(R.string.getcomment_ip)+"?commentID="+contentID;
                    //System.out.println(ip);
                    // send commentID and return sound byte
                    soundButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(trackplayer==null||trackplayer.getState() == AudioTrack.STATE_UNINITIALIZED){
                                int bufsize = AudioTrack.getMinBufferSize(8000,
                                        AudioFormat.CHANNEL_OUT_MONO,
                                        AudioFormat.ENCODING_PCM_16BIT);
                                trackplayer = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                                        AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufsize * 2,
                                        AudioTrack.MODE_STREAM);
                            }
                            if(trackplayer.getState() == AudioTrack.STATE_NO_STATIC_DATA||
                                    trackplayer.getState() == AudioTrack.STATE_INITIALIZED||
                                    trackplayer.getState() == AudioTrack.PLAYSTATE_STOPPED){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Connect myConnect = new Connect(mContext);
                                        bytes = myConnect.sendHttpPostMedia(ip);
                                        Message msg = mHandler.obtainMessage();
                                        msg.what = 2;
                                        mHandler.sendMessage(msg);
                                    }
                                }).start();
                            }
                            if (trackplayer.getState() == AudioTrack.PLAYSTATE_PLAYING) {
                                trackplayer.stop();
                                trackplayer.release();
                            }
                        }
                    });
        }
        return convertView;
    }

    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what==1) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                View popup = LayoutInflater.from(mContext).inflate(R.layout.image_view, null);
                ImageView imageView = popup.findViewById(R.id.image);
                imageView.setImageBitmap(bitmap);

                PopupWindow window = new PopupWindow(mContext);
                window.setContentView(popup);
                window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setFocusable(true);
                window.setOutsideTouchable(true);
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

                View convertView = LayoutInflater.from(mContext).inflate(R.layout.mycomment_activity,null);
                window.showAtLocation(convertView, Gravity.CENTER,0,0);
            }else if(msg.what==2) {
                trackplayer.play() ;
                trackplayer.write(bytes, 0, bytes.length);
                trackplayer.stop();
                trackplayer.release();
            }
        }
    };
}