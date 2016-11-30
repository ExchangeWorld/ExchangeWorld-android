package com.example.arthome.newexchangeworld.Models;

/**
 * Created by art on 2016/11/30.
 */

public class CreateExchangeModel {
    private int goods_one_gid;
    private int goods_two_gid;

    public CreateExchangeModel(int goods_one_gid, int goods_two_gid){
        this.goods_one_gid = goods_one_gid;
        this.goods_two_gid = goods_two_gid;
    }

    public int getGoods_two_gid() {
        return goods_two_gid;
    }

    public void setGoods_two_gid(int goods_two_gid) {
        this.goods_two_gid = goods_two_gid;
    }

    public int getGoods_one_gid() {
        return goods_one_gid;
    }

    public void setGoods_one_gid(int goods_one_gid) {
        this.goods_one_gid = goods_one_gid;
    }
}
