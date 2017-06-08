package com.example.lihui20.testhttp.model;

import org.json.JSONObject;

/**
 * Created by lihui20 on 2016/12/6.
 */
public class Data {
    String title;
    String date;
    String author_name;
    String thumbnail_pic_s, thumbnail_pic_s02, thumbnail_pic_s03, url;
    String uniquekey;


    public Data(JSONObject jsonObject) throws  Exception{
        this.title=jsonObject.getString("title");
        this.date=jsonObject.getString("date");
        this.author_name=jsonObject.getString("author_name");
        this.thumbnail_pic_s=jsonObject.getString("thumbnail_pic_s");
        //this.thumbnail_pic_s02=jsonObject.getString("thumbnail_pic_s02");
        //this.thumbnail_pic_s03=jsonObject.getString("thumbnail_pic_s03");
        this.url=jsonObject.getString("url");
   //     this.uniquekey=jsonObject.getString("uniquekey");

    }
//image string, title string, date string,author string,linkaddress string
    public Data(String thumbnail_pic_s,String title, String date, String author_name,  String url) {
        this.title = title;
        this.date = date;
        this.author_name = author_name;
        this.thumbnail_pic_s = thumbnail_pic_s;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public String getThumbnail_pic_s02() {
        return thumbnail_pic_s02;
    }

    public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }

    public String getThumbnail_pic_s03() {
        return thumbnail_pic_s03;
    }

    public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }
    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", author_name='" + author_name + '\'' +
                ", thumbnail_pic_s='" + thumbnail_pic_s + '\'' +
                ", thumbnail_pic_s02='" + thumbnail_pic_s02 + '\'' +
                ", thumbnail_pic_s03='" + thumbnail_pic_s03 + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
