package com.example.a15_7_19;

public class Blogzone {
    private String title, desc, imageUrl, uname;
    int index;
    // generate their respective constructors
    public Blogzone(String title, String desc, String imageUrl, String uid) {
        this.title = title;
        this.desc = desc;
        this.imageUrl=imageUrl;
        index = uid.indexOf('@');
        if(index>=0)
        this.uname = uid.substring(0,index);
    }
    // create an empty constructor
    public Blogzone() {
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setUID(String uid)
    {
        this.uname = uid;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getTitle() {
        return title;
    }
    public String getDesc() {
        return desc;
    }
    public String getUID() {
        return uname;
    }
}