package com.example.arthome.newexchangeworld.Models;

/**
 * Created by arthome on 2016/12/6.
 */

public class StarModel {
    int sid;
    int goods_gid;
    int starring_user_uid;
    String updated_at;
    String created_at;
    GoodsModel goods;

    public GoodsModel getGoods() {
        return goods;
    }

    public void setGoods(GoodsModel goods) {
        this.goods = goods;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getGoods_gid() {
        return goods_gid;
    }

    public void setGoods_gid(int goods_gid) {
        this.goods_gid = goods_gid;
    }

    public int getStarring_user_uid() {
        return starring_user_uid;
    }

    public void setStarring_user_uid(int starring_user_uid) {
        this.starring_user_uid = starring_user_uid;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
