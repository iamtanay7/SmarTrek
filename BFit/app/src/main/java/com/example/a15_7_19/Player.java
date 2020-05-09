package com.example.a15_7_19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {
    Button btn_next,btn_prev,btn_pause;
    TextView songtextlabel;
    SeekBar songseekbar;
    static MediaPlayer mymediaPlayer;
    int position;
    String sname;

    Thread updateseekbar;

    ArrayList<File>mysongs;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next=(Button)findViewById(R.id.next);
        btn_prev=(Button)findViewById(R.id.previous);
        btn_pause=(Button)findViewById(R.id.pause);

        songtextlabel=(TextView)findViewById(R.id.songlabel);
        songseekbar=(SeekBar)findViewById(R.id.seekbar);
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        updateseekbar = new Thread(){
            @Override
            public void run(){
                int totalduration = mymediaPlayer.getDuration();
                int currentposition = 0;

                while(currentposition<totalduration)
                {
                    try{
                        sleep(500);
                        currentposition=mymediaPlayer.getCurrentPosition();
                        songseekbar.setProgress(currentposition);

                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    catch (IllegalStateException e) {
                        mymediaPlayer.release();
                    }
                }

            }



        };
        if(mymediaPlayer!=null)
        {
            mymediaPlayer.stop();
            mymediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mysongs=(ArrayList) bundle.getParcelableArrayList("songs");
        sname = mysongs.get(position).getName().toString();
        String songname = i.getStringExtra("songname");
        songtextlabel.setText(songname);
        songtextlabel.setSelected(true);
        position= bundle.getInt("pos",0);
        Uri u= Uri.parse(mysongs.get(position).toString());
        mymediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        mymediaPlayer.start();
        songseekbar.setMax(mymediaPlayer.getDuration());

        updateseekbar.start();

        songseekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songseekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);
        songseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mymediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songseekbar.setMax(mymediaPlayer.getDuration());
                if(mymediaPlayer.isPlaying())
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    mymediaPlayer.pause();
                }
                else
                {
                    btn_pause.setBackgroundResource(R.drawable.pause);
                    mymediaPlayer.start();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_pause.setBackgroundResource(R.drawable.pause);
                mymediaPlayer.stop();
                mymediaPlayer.release();
                position=((position+1)%mysongs.size());
                Uri u = Uri.parse(mysongs.get(position).toString());
                mymediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mysongs.get(position).getName().toString();
                songtextlabel.setText(sname);
                mymediaPlayer.start();
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_pause.setBackgroundResource(R.drawable.pause);
                mymediaPlayer.stop();
                mymediaPlayer.release();
                position=((position-1)<0)?(mysongs.size()-1):(position-1);
                Uri u = Uri.parse(mysongs.get(position).toString());
                mymediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mysongs.get(position).getName().toString();
                songtextlabel.setText(sname);
                mymediaPlayer.start();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}