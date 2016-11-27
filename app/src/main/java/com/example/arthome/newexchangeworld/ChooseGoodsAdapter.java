package com.example.arthome.newexchangeworld;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.databinding.ItemChooseMyGoodsBinding;

import java.util.List;

/**
 * Created by arthome on 2016/11/27.
 */

public class ChooseGoodsAdapter extends RecyclerView.Adapter<ChooseGoodsAdapter.ChooseGoodsViewHolder> {
    List<GoodsModel> goodsModelList;
    public ChooseGoodsAdapter(List<GoodsModel> goodsModelList){
        this.goodsModelList= goodsModelList;
    }

    @Override
    public ChooseGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_my_goods,parent,false);
        return new ChooseGoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseGoodsViewHolder holder, int position) {
        GoodsModel goodsModel = goodsModelList.get(position);
        holder.getBinding().setVariable(BR.goods, goodsModel);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return goodsModelList.size();
    }

    class ChooseGoodsViewHolder extends RecyclerView.ViewHolder {
        private ItemChooseMyGoodsBinding binding;

        public ChooseGoodsViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(">>>>"+getAdapterPosition());
                }
            });
        }

        private ItemChooseMyGoodsBinding getBinding() {
            return binding;
        }
    }
}
