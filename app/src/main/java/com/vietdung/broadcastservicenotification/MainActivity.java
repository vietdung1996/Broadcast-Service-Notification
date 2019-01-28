package com.vietdung.broadcastservicenotification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private SeekBar mSeekMusic;
    private ImageView mImageNext, mImageBack, mImagePause;
    private TextView mTextTimeStart, mTextTimeEnd, mTextNameSong;
    MusicService musicService;
    Intent playIntent;
    boolean musicBound = false;
    private static final int DELAY = 100;
    private static final int DELAY_2 = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (playIntent == null) {
            playIntent = new Intent(MainActivity.this, MusicService.class);
            bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        initView();
        upDateTimeSong();
    }

    private void initView() {
        mSeekMusic = findViewById(R.id.seek_music);
        mImageNext = findViewById(R.id.image_next);
        mImageBack = findViewById(R.id.image_back);
        mImagePause = findViewById(R.id.image_pause);
        mTextTimeStart = findViewById(R.id.text_time_start);
        mTextTimeEnd = findViewById(R.id.text_time_end);
        mTextNameSong = findViewById(R.id.text_name_song);
        mImagePause.setOnClickListener(this);
        mImageBack.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mSeekMusic.setOnSeekBarChangeListener(this);
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
            if (musicService == null) {
                musicService = binder.getService();
            }
            musicService.playMusic(MainActivity.this);
            musicService.setActivity(MainActivity.this);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                if (musicService != null && musicBound) {
                    musicService.backSong(MainActivity.this);
                    mImagePause.setImageResource(R.drawable.pausetrack);
                }
                break;
            case R.id.image_next:
                if (musicService != null && musicBound) {
                    musicService.nextSong(MainActivity.this);
                    mImagePause.setImageResource(R.drawable.pausetrack);
                }
                break;
            case R.id.image_pause:
                if (musicService.isPlaying()) {
                    musicService.pauseMusic();
                    mImagePause.setImageResource(R.drawable.playtrack);
                } else {
                    musicService.pauseMusic();
                    mImagePause.setImageResource(R.drawable.pausetrack);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        stopService(playIntent);
    }

    private void upDateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                if (musicService != null && musicBound && musicService.isPlaying()) {
                    mSeekMusic.setProgress(musicService.getCurrentPosition());
                    mSeekMusic.setMax(musicService.getTimeToTal());
                    mTextTimeEnd.setText(dateFormat.format(musicService.getTimeToTal()));
                    mTextTimeStart.setText(dateFormat.format(musicService.getCurrentPosition()));
                    mTextNameSong.setText(musicService.getNameMusic());
                }
                handler.postDelayed(this, DELAY_2);
            }
        }, DELAY);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        musicService.seekTo(seekBar.getProgress());
    }
}
