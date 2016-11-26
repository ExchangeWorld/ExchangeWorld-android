package com.example.arthome.newexchangeworld.Models;


/**
 * Created by arthome on 2016/11/26.
 */

public class QueueResponseModel {
    int qid;
    String created_at;
    String updated_at;
    int host_goods_gid;
    int queuer_goods_gid;

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

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
