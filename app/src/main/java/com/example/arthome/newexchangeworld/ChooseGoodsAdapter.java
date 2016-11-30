package com.example.arthome.newexchangeworld;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.QueueOfGoodsModel;
import com.example.arthome.newexchangeworld.databinding.ItemChooseMyGoodsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthome on 2016/11/27.
 */

public class ChooseGoodsAdapter extends RecyclerView.Adapter<ChooseGoodsAdapter.ChooseGoodsViewHolder> {
    private List<GoodsModel> goodsModelList;
    private List<QueueOfGoodsModel> queueOfGoodsModelList;
    private int checkedPosition = -1;
    public ChooseGoodsAdapter(List<GoodsModel> goodsModelList){
        this.goodsModelList= goodsModelList;
    }

    //TODO 語法錯誤 待改
    public ChooseGoodsAdapter(List<QueueOfGoodsModel> queueOfGoodsModelList, int nothigh){
        this.queueOfGoodsModelList = queueOfGoodsModelList;
        goodsModelList = new ArrayList<>();
        for(int i = 0; i<queueOfGoodsModelList.size();i++){
            goodsModelList.add(queueOfGoodsModelList.get(i).getQueuer_goods());
        }
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
        if(checkedPosition==position)
            holder.getBinding().itemChooseMyGoodsRadioButton.setChecked(true);
        else
            holder.getBinding().itemChooseMyGoodsRadioButton.setChecked(false);
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
                    //TODO  爛Code 待改
                    int oldChecked= checkedPosition;
                    checkedPosition = getAdapterPosition();
                    notifyItemRangeChanged(oldChecked,1);
                    binding.itemChooseMyGoodsRadioButton.setChecked(true);
                }
            });
        }

        private ItemChooseMyGoodsBinding getBinding() {
            return binding;
        }
    }
    public int getChosenGid(){
        int gid = (checkedPosition==-1)?-1:goodsModelList.get(checkedPosition).getGid();
        return gid;
    }
}
