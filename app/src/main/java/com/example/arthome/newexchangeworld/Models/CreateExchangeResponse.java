package com.example.arthome.newexchangeworld.Models;

/**
 * Created by art on 2016/11/30.
 */

public class CreateExchangeResponse {
    private String created_at;
    private int eid;
    private boolean goods_one_agree;
    private int goods_one_gid;
    private boolean goods_two_agree;
    private int goods_two_gid;
    private String status;
    private String updated_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public boolean isGoods_one_agree() {
        return goods_one_agree;
    }

    public void setGoods_one_agree(boolean goods_one_agree) {
        this.goods_one_agree = goods_one_agree;
    }

    public int getGoods_one_gid() {
        return goods_one_gid;
    }

    public void setGoods_one_gid(int goods_one_gid) {
        this.goods_one_gid = goods_one_gid;
    }

    public boolean isGoods_two_agree() {
        return goods_two_agree;
    }

    public void setGoods_two_agree(boolean goods_two_agree) {
        this.goods_two_agree = goods_two_agree;
    }

    public int getGoods_two_gid() {
        return goods_two_gid;
    }

    public void setGoods_two_gid(int goods_two_gid) {
        this.goods_two_gid = goods_two_gid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
