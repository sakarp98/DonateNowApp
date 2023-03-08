package com.example.gofundmenepal;

public class Posts {
   private String title ;
   private String desc ;
   private String image ;
   private String date ;
   private String Time ;
   private String location ;




    public Posts(String title, String desc, String image , String time , String date) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.date = date;


        this.location = location ;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Posts(){

    }
}
