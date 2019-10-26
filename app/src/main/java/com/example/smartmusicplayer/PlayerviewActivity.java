package com.example.smartmusicplayer;

import android.app.Activity;
import android.content.Intent;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;



import java.io.File;
import java.util.ArrayList;

public class PlayerviewActivity extends Activity {
    Button btn_puase,btn_next,btn_previous;
    TextView songTextLabel;
    SeekBar songSeekBar;
    static MediaPlayer myMediaplayer;
    int position;
    String sname;
    ArrayList<File> mySongs;
    Thread updatSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btn_next=(Button)findViewById(R.id.next);
        btn_puase=(Button)findViewById(R.id.pause);
        btn_previous=(Button)findViewById(R.id.previous);

        songTextLabel=(TextView)findViewById(R.id.songLabel);
        songSeekBar=(SeekBar)findViewById(R.id.seekBar);




        updatSeekBar=new Thread(){
            @Override
            public void run() {
                int totalDuration=myMediaplayer.getDuration();
                int currentPosition =0;
                 while(currentPosition <totalDuration){
                     try {
                         sleep(500);
                         currentPosition =myMediaplayer.getCurrentPosition();
                         songSeekBar.setProgress(currentPosition);

                     }catch (InterruptedException e){
                            e.printStackTrace();
                     }
                 }


            }
        };

        if (myMediaplayer!=null){
            myMediaplayer.stop();
            myMediaplayer.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
        sname= mySongs.get(position).getName();
        String songName=i.getStringExtra("songName");

        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);

        position=bundle.getInt("pos",0);
        Uri u= Uri.parse(mySongs.get(position).toString());

        myMediaplayer=MediaPlayer.create(getApplicationContext(),u);
        myMediaplayer.start();
        songSeekBar.setMax(myMediaplayer.getDuration());
        updatSeekBar.start();


        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaplayer.seekTo(seekBar.getProgress());
            }
        });
        btn_puase.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                songSeekBar.setMax((myMediaplayer.getDuration()));
                if (myMediaplayer.isPlaying()) {
                    btn_puase.setBackgroundResource(R.drawable.icon_play);
                    myMediaplayer.pause();
                }
                else
                {
                    btn_puase.setBackgroundResource(R.drawable.icon_puse);
                    myMediaplayer.start();

                }
                }


        });
        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myMediaplayer.stop();
                myMediaplayer.release();
                position=((position+1)%mySongs.size());
                Uri u=Uri.parse(mySongs.get(position).toString());
                myMediaplayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName();
                songTextLabel.setText(sname);
                myMediaplayer.start();
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaplayer.stop();
                myMediaplayer.release();
                position=((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u=Uri.parse(mySongs.get(position).toString());
                myMediaplayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName();
                songTextLabel.setText(sname);
                myMediaplayer.start();
            }
        });

        



    }
}
