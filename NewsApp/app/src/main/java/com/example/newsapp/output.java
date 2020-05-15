package com.example.newsapp;

public class output {

    private String Title;
    private String Tag;
    private String Img;
    private String Id;
    private String Web;
    private String Date;

    public output() {
    }




    public output(String title, String tag, String img, String id, String web, String date) {
        Title = title;
        Tag = tag;
        Img = img;
        Id = id;
        Web = web;
        Date = date;



    }


    public String getTitle() {
        return Title;
    }

    public String getTag() {
        return Tag;
    }

    public String getImg() {
        return Img;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setWeb(String web) {
        Web = web;
    }

    public String getWeb() {
        return Web;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public void setImg(String img) {
        Img = img;
    }
}
