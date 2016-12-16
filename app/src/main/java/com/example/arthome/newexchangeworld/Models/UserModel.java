package com.example.arthome.newexchangeworld.Models;

import java.util.List;

/**
 * Created by SSD on 2016/10/15.
 */
public class UserModel {
    private int uid;
    private String identity;
    private String name;
    private String photo_path;
    private String introduction;
    private String[] wishlist;      //這是啥?
    private String created_at;
    private String updated_at;
    private GoodsModel[] goods;
    private FollowedModel[] follows_followed;
    private FollowerModel[] follows_follower;
    private List<StaringUserModel> star_starring_user;

    public List<StaringUserModel> getStar_starring_user() {
        return star_starring_user;
    }

    public void setStar_starring_user(List<StaringUserModel> star_starring_user) {
        this.star_starring_user = star_starring_user;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String[] getWishlist() {
        return wishlist;
    }

    public void setWishlist(String[] wishlist) {
        this.wishlist = wishlist;
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

    public GoodsModel[] getGoods() {
        return goods;
    }

    public void setGoods(GoodsModel[] goods) {
        this.goods = goods;
    }

    public FollowedModel[] getFollows_followed() {
        return follows_followed;
    }

    public void setFollows_followed(FollowedModel[] follows_followed) {
        this.follows_followed = follows_followed;
    }

    public FollowerModel[] getFollows_follower() {
        return follows_follower;
    }

    public void setFollows_follower(FollowerModel[] follows_follower) {
        this.follows_follower = follows_follower;
    }

}
