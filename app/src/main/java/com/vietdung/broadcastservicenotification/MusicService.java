package com.vietdung.broadcastservicenotification;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    private MediaPlayer mMediaPlayer;
    private final IBinder mIBinder = new MusicBinder();
    private List<Song> mSongs;
    private int pos = 0;
    private Activity mActivity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextSong(mActivity);
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMusicPlay();
    }

    private void initMusicPlay() {
        addSong();
    }

    public void playMusic(Activity context) {
        mMediaPlayer = MediaPlayer.create(context, mSongs.get(pos).getResID());
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(this);
    }

    public void addSong() {
        mSongs = new ArrayList<>();
        mSongs.add(new Song(getString(R.string.danhchoem), R.raw.danhchoem));
        mSongs.add(new Song(getString(R.string.emselacodau), R.raw.emselacodau));
    }

    public String getNameMusic() {
        return mSongs.get(pos).getSongName();
    }

    public void backSong(Activity context) {
        if (pos > 0) {
            pos--;
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            playMusic(context);
        } else {
            pos = mSongs.size() - 1;
            playMusic(context);
        }
    }

    public void nextSong(Activity activity) {
        if (pos < mSongs.size() - 1) {
            pos++;
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            playMusic(activity);
        } else {
            pos = 0;
            playMusic(activity);
        }
    }

    public void pauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getTimeToTal() {
        return mMediaPlayer.getDuration();
    }

    public void seekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }
}
