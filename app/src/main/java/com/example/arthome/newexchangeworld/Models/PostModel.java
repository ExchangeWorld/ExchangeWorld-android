package com.example.arthome.newexchangeworld.Models;

import java.io.Serializable;

/**
 * Created by SSD on 2016/8/14.
 */
public class PostModel implements Serializable {
    String name;
    String photo_path = "[\"\"]";
    String description;
    float position_x = 121;     //long
    float position_y = 24;      //lan
    String category = "Books";

    public PostModel() {
        photo_path = "[\"\"]";
        position_x = 121;     //long
        position_y = 24;      //lan
        category = "Books";
    }

    public PostModel(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
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
        if(this.photo_path.length()==4){
                    this.photo_path = new StringBuilder(this.photo_path).insert(this.photo_path.length()-2, photo_path).toString();
        }else {
            this.photo_path = new StringBuilder(this.photo_path).insert(this.photo_path.length()-2, "\",\""+photo_path).toString();
        }
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
