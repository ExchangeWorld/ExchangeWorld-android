package com.example.arthome.newexchangeworld.Models;

/**
 * Created by SSD_win8 on 2016/5/20.
 */
public class Owner {
    private int uid;
    private String name;
    private String photo_path;//maybe not string

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }
}
