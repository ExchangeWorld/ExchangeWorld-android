package com.example.arthome.newexchangeworld.Models;

/**
 * Created by arthome on 2016/11/11.
 */

public class QueueOfGoodsModel {
    int qid;
    private String created_at;
    private String updated_at;
    int host_goods_gid;
    int queuer_goods_gid;
    GoodsModel queuer_goods;

    public GoodsModel getQueuer_goods() {
        return queuer_goods;
    }

    public void setQueuer_goods(GoodsModel queuer_goods) {
        this.queuer_goods = queuer_goods;
    }

    public int getQueuer_goods_gid() {
        return queuer_goods_gid;
    }

    public void setQueuer_goods_gid(int queuer_goods_gid) {
        this.queuer_goods_gid = queuer_goods_gid;
    }

    public int getHost_goods_gid() {
        return host_goods_gid;
    }

    public void setHost_goods_gid(int host_goods_gid) {
        this.host_goods_gid = host_goods_gid;
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

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

}
