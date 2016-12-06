package com.example.arthome.newexchangeworld.Models;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.CircleTransform;
import com.example.arthome.newexchangeworld.util.CategoryTool;
import com.example.arthome.newexchangeworld.util.StringTool;
import com.squareup.picasso.Picasso;

/**
 * Created by arthome on 2016/5/14.
 */
public class GoodsModel {
    private int gid;
    private int rate;
    private int exchanged;
    private int deleted;
    private double position_x;
    private double position_y;
    private String name;
    private String photo_path;
    private String category;
    private String description;
    private String created_at;
    private String updated_at;
    private Owner owner;
    private boolean starredByUser = false;  //帶token才會回傳這項

    public GoodsModel() {
    }

    public boolean isStarredByUser() {
        return starredByUser;
    }

    public void setStarredByUser(boolean starredByUser) {
        this.starredByUser = starredByUser;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String error;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getExchanged() {
        return exchanged;
    }

    public void setExchanged(int exchanged) {
        this.exchanged = exchanged;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public double getPosition_x() {
        return position_x;
    }

    public void setPosition_x(double position_x) {
        this.position_x = position_x;
    }

    public double getPosition_y() {
        return position_y;
    }

    public void setPosition_y(double position_y) {
        this.position_y = position_y;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }


    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if(imageUrl!=null) {
            imageUrl = StringTool.INSTANCE.getFirstPhotoURL(imageUrl);
            Picasso.with(view.getContext())
                    .load(imageUrl)
                    .into(view);
        }
    }

    @BindingAdapter({"bind:userImage"})
    public static void loadUserImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .transform(new CircleTransform())
                .into(view);
    }

    @BindingAdapter({"bind:categoryIcon"})
    public static void setCategoryImage(ImageView view, String category) {
        if(category!=null) {
            int resID = CategoryTool.INSTANCE.getCategoryDrawableID(category);
            view.setImageResource(resID);
        }
    }

    @BindingAdapter({"bind:categoryName"})
    public static void setCategoryName(TextView view, String category) {
        String categoryName = CategoryTool.INSTANCE.getCategoryName(category);
        view.setText(categoryName);
    }
    /*
    {
            "extra_json":{},
            "owner":{
            "uid": 2,
                "name": "鄭哲亞",
                "photo_path": "https://scontent.xx.fbcdn.net/v/t1.0-1/c0.0.320.320/p320x320/12924437_1017212271691557_7929897522438387146_n.jpg?oh=489178208e672a6feefb9d036f6bea47&oe=57A4CA66"
    },
        "comments":[],
        "star_goods":[
        {"sid": 1, "created_at": "2016-05-12T11:34:15.876Z", "updated_at": "2016-05-12T11:34:15.876Z", "goods_gid": 1,…}
        ]
    }
*/

}
