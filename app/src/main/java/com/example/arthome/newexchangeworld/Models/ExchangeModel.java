package com.example.arthome.newexchangeworld.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

/**
 * Created by art on 2016/11/22.
 */

public class ExchangeModel extends BaseObservable{
    private int eid;
    private String status;
    private GoodsModel owner_goods;
    private GoodsModel other_goods;
    private boolean owner_agree;
    private boolean other_agree;

    @Bindable
    public int getEid() {
        return eid;
    }


    public void setEid(int eid) {
        this.eid = eid;
        notifyPropertyChanged(BR.eid);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GoodsModel getOwner_goods() {
        return owner_goods;
    }

    public void setOwner_goods(GoodsModel owner_goods) {
        this.owner_goods = owner_goods;
    }

    public GoodsModel getOther_goods() {
        return other_goods;
    }

    public void setOther_goods(GoodsModel other_goods) {
        this.other_goods = other_goods;
    }

    public boolean isOwner_agree() {
        return owner_agree;
    }

    public void setOwner_agree(boolean owner_agree) {
        this.owner_agree = owner_agree;
    }

    public boolean isOther_agree() {
        return other_agree;
    }

    public void setOther_agree(boolean other_agree) {
        this.other_agree = other_agree;
    }

}
