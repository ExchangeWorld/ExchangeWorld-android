package com.example.arthome.newexchangeworld;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.util.StringTool;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthome on 2016/10/30.
 */

public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.DetailViewHolder> {

    private Context context;
    private List<String> urlString = new ArrayList<>();

    public ItemDetailAdapter(String photoPath, Context context){
        urlString = StringTool.INSTANCE.getAllPhotoURL(photoPath);
        this.context = context;
    }
    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_good, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        Picasso.with(context).load(urlString.get(position)).into(holder.goodsImageView);
    }

    @Override
    public int getItemCount() {
        return urlString.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private ImageView goodsImageView;
        public DetailViewHolder(View itemView) {
            super(itemView);
            goodsImageView = (ImageView) itemView.findViewById(R.id.item_detail_image_view);
        }
    }
}
