package com.example.arthome.newexchangeworld.Models;

import java.util.List;

/**
 * Created by arthome on 2016/11/30.
 */

public class ExchangeRequestModel {
    private int gid;
    private String name;
    private String photo_path;
    private String category;
    private List<QueueOfGoodsModel> queue;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
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

    public List<QueueOfGoodsModel> getQueue() {
        return queue;
    }

    public void setQueue(List<QueueOfGoodsModel> queue) {
        this.queue = queue;
    }
}
