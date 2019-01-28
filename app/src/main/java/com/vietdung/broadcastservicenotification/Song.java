package com.vietdung.broadcastservicenotification;

public class Song {
    private String songName;
    private int resID;

    public Song(String songName, int resID) {
        this.songName = songName;
        this.resID = resID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }
}
