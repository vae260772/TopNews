package com.example.lihui20.testhttp.model;

import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by lihui20 on 2017/3/22.
 */

public class Music implements Serializable {
    String TITLE;// 标题
    String DURATION;// 持续时间
    String ARTIST;// 艺术家
    String _ID;// id
    String DISPLAY_NAME;// 显示名称
    String DATA;// 数据
    String ALBUM_ID;// 专辑名称ID
    String ALBUM;// 专辑
    String SIZE;
    //格式
    String MIME_TYPE;
    String YEAR;
    Music music;

    public String getMIME_TYPE() {
        return MIME_TYPE;
    }

    public void setMIME_TYPE(String MIME_TYPE) {
        this.MIME_TYPE = MIME_TYPE;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public Music(Music music) {
        this.music = music;
    }

    public Music(String TITLE, String DURATION, String ARTIST, String _ID, String DISPLAY_NAME, String DATA,
                 String ALBUM_ID, String ALBUM, String SIZE, String MIME_TYPE, String YEAR) {
        this.TITLE = TITLE;
        this.DURATION = DURATION;
        this.ARTIST = ARTIST;
        this._ID = _ID;
        this.DISPLAY_NAME = DISPLAY_NAME;
        this.DATA = DATA;
        this.ALBUM_ID = ALBUM_ID;
        this.ALBUM = ALBUM;
        this.SIZE = SIZE;
        this.MIME_TYPE = MIME_TYPE;
        this.YEAR = YEAR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Music music = (Music) o;

        if (TITLE != null ? !TITLE.equals(music.TITLE) : music.TITLE != null) return false;
        if (DURATION != null ? !DURATION.equals(music.DURATION) : music.DURATION != null)
            return false;
        if (ARTIST != null ? !ARTIST.equals(music.ARTIST) : music.ARTIST != null) return false;
        if (_ID != null ? !_ID.equals(music._ID) : music._ID != null) return false;
        if (DISPLAY_NAME != null ? !DISPLAY_NAME.equals(music.DISPLAY_NAME) : music.DISPLAY_NAME != null)
            return false;
        if (DATA != null ? !DATA.equals(music.DATA) : music.DATA != null) return false;
        if (ALBUM_ID != null ? !ALBUM_ID.equals(music.ALBUM_ID) : music.ALBUM_ID != null)
            return false;
        if (ALBUM != null ? !ALBUM.equals(music.ALBUM) : music.ALBUM != null) return false;
        if (SIZE != null ? !SIZE.equals(music.SIZE) : music.SIZE != null) return false;
        if (MIME_TYPE != null ? !MIME_TYPE.equals(music.MIME_TYPE) : music.MIME_TYPE != null)
            return false;
        return YEAR != null ? YEAR.equals(music.YEAR) : music.YEAR == null;

    }

    @Override
    public int hashCode() {
        int result = TITLE != null ? TITLE.hashCode() : 0;
        result = 31 * result + (DURATION != null ? DURATION.hashCode() : 0);
        result = 31 * result + (ARTIST != null ? ARTIST.hashCode() : 0);
        result = 31 * result + (_ID != null ? _ID.hashCode() : 0);
        result = 31 * result + (DISPLAY_NAME != null ? DISPLAY_NAME.hashCode() : 0);
        result = 31 * result + (DATA != null ? DATA.hashCode() : 0);
        result = 31 * result + (ALBUM_ID != null ? ALBUM_ID.hashCode() : 0);
        result = 31 * result + (ALBUM != null ? ALBUM.hashCode() : 0);
        result = 31 * result + (SIZE != null ? SIZE.hashCode() : 0);
        result = 31 * result + (MIME_TYPE != null ? MIME_TYPE.hashCode() : 0);
        result = 31 * result + (YEAR != null ? YEAR.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Music{" +
                "TITLE='" + TITLE + '\'' +
                ", DURATION='" + DURATION + '\'' +
                ", ARTIST='" + ARTIST + '\'' +
                ", _ID='" + _ID + '\'' +
                ", DISPLAY_NAME='" + DISPLAY_NAME + '\'' +
                ", DATA='" + DATA + '\'' +
                ", ALBUM_ID='" + ALBUM_ID + '\'' +
                ", ALBUM='" + ALBUM + '\'' +
                ", SIZE='" + SIZE + '\'' +
                ", MIME_TYPE='" + MIME_TYPE + '\'' +
                ", YEAR='" + YEAR + '\'' +
                '}';
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDURATION() {
        return DURATION;
    }

    public void setDURATION(String DURATION) {
        this.DURATION = DURATION;
    }

    public String getARTIST() {
        return ARTIST;
    }

    public void setARTIST(String ARTIST) {
        this.ARTIST = ARTIST;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getALBUM_ID() {
        return ALBUM_ID;
    }

    public void setALBUM_ID(String ALBUM_ID) {
        this.ALBUM_ID = ALBUM_ID;
    }

    public String getALBUM() {
        return ALBUM;
    }

    public void setALBUM(String ALBUM) {
        this.ALBUM = ALBUM;
    }

    public String getSIZE() {
        return SIZE;
    }

    public void setSIZE(String SIZE) {
        this.SIZE = SIZE;
    }

}
