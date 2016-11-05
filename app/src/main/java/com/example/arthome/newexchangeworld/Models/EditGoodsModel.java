package com.example.arthome.newexchangeworld.Models;

/**
 * Created by arthome on 2016/11/5.
 */

public class EditGoodsModel {
    private int gid;
    private double position_x;
    private double position_y;
    private String name;
    private String photo_path;
    private String category;
    private String description;

    public EditGoodsModel(int gid, double position_x, double position_y, String photo_path, String category){
        this.gid = gid;
        this.position_x = position_x;
        this.position_y = position_y;
        this.photo_path = photo_path;
        this.category = category;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public double getPosition_x() {
        return position_x;
    }

    public void setPosition_x(double position_x) {
        this.position_x = position_x;
    }

    public double getPosition_y() {
        return position_y;
    }

    public void setPosition_y(double position_y) {
        this.position_y = position_y;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
