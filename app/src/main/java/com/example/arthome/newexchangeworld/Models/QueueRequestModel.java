package com.example.arthome.newexchangeworld.Models;

/**
 * Created by arthome on 2016/11/26.
 */

public class QueueRequestModel {
    int host_goods_gid;
    int queuer_goods_gid;

    public int getHost_goods_gid() {
        return host_goods_gid;
    }

    public void setHost_goods_gid(int host_goods_gid) {
        this.host_goods_gid = host_goods_gid;
    }

    public int getQueuer_goods_gid() {
        return queuer_goods_gid;
    }

    public void setQueuer_goods_gid(int queuer_goods_gid) {
        this.queuer_goods_gid = queuer_goods_gid;
    }
}
