package com.example.arthome.newexchangeworld.Models;

/**
 * Created by SSD on 2016/8/14.
 */
public class PostModel {
    String name;
    String photo_path = "[\"http://exwd.csie.org/images/87ea5379d1d295841e1b34c0227524a5e56e61e2e993aa16617d47346e4f14ee.jpeg\"]";
    String description;
    float position_x = 121;
    float position_y =24;
    String category = "Books";

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPosition_x() {
        return position_x;
    }

    public void setPosition_x(float position_x) {
        this.position_x = position_x;
    }

    public float getPosition_y() {
        return position_y;
    }

    public void setPosition_y(float position_y) {
        this.position_y = position_y;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
