package com.example.arthome.newexchangeworld.Models;

/**
 * Created by arthome on 2016/12/6.
 */

public class StarGoodsModel {
    int starring_user_uid;
    int goods_gid;

    public StarGoodsModel(int starring_user_uid, int goods_gid){
        this.starring_user_uid = starring_user_uid;
        this.goods_gid = goods_gid;
    }

    public int getStarring_user_uid() {
        return starring_user_uid;
    }

    public void setStarring_user_uid(int starring_user_uid) {
        this.starring_user_uid = starring_user_uid;
    }

    public int getGoods_gid() {
        return goods_gid;
    }

    public void setGoods_gid(int goods_gid) {
        this.goods_gid = goods_gid;
    }
}
