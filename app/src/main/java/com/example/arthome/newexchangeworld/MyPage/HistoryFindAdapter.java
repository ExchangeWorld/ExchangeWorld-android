package com.example.arthome.newexchangeworld.MyPage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.Models.ExchangeModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.util.CategoryTool;
import com.example.arthome.newexchangeworld.util.StringTool;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by art on 2016/11/22.
 */

public class HistoryFindAdapter extends RecyclerView.Adapter<HistoryFindAdapter.HistoryViewHolder> {
    private static List<ExchangeModel> exchangeModels;
    private Context context;

    public HistoryFindAdapter(List<ExchangeModel> exchangeModels, Context context) {
        this.exchangeModels = exchangeModels;
        this.context = context;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.otherGoodsTextView.setText(exchangeModels.get(position).getOther_goods().getName());
        Picasso.with(context).load(StringTool.INSTANCE.getFirstPhotoURL(exchangeModels.get(position).getOther_goods().getPhoto_path()))
                .into(holder.otherGoodsimage);
        holder.otherCategoryimage.setImageResource(
                CategoryTool.INSTANCE.getCategoryDrawableID(exchangeModels.get(position).getOther_goods().getCategory()));
        holder.otherUserTextView.setText(exchangeModels.get(position).getOther_goods().getOwner().getName());

        holder.ownerGoodsTextView.setText(exchangeModels.get(position).getOwner_goods().getName());
        Picasso.with(context).load(StringTool.INSTANCE.getFirstPhotoURL(exchangeModels.get(position).getOwner_goods().getPhoto_path()))
                .into(holder.ownerGoodsimage);
        holder.ownerCategoryimage.setImageResource(
                CategoryTool.INSTANCE.getCategoryDrawableID(exchangeModels.get(position).getOwner_goods().getCategory()));
    }

    @Override
    public int getItemCount() {
        return exchangeModels.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView otherGoodsTextView;
        private TextView otherUserTextView;
        private ImageView otherCategoryimage;
        private ImageView otherGoodsimage;
        private TextView ownerGoodsTextView;
        private ImageView ownerCategoryimage;
        private ImageView ownerGoodsimage;

        public HistoryViewHolder(View view) {
            super(view);

            otherGoodsTextView = (TextView) view.findViewById(R.id.item_history_other_goods_name);
            otherUserTextView = (TextView) view.findViewById(R.id.item_history_other_user_name);
            otherCategoryimage = (ImageView) view.findViewById(R.id.item_history_other_category_image);
            otherGoodsimage = (ImageView) view.findViewById(R.id.item_history_other_goods_image);
            ownerGoodsTextView = (TextView) view.findViewById(R.id.item_history_owner_goods_name);
            ownerCategoryimage = (ImageView) view.findViewById(R.id.item_history_owner_category_image);
            ownerGoodsimage = (ImageView) view.findViewById(R.id.item_history_owner_goods_image);
        }

    }
}
