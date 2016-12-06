package com.example.arthome.newexchangeworld.MyPage;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthome.newexchangeworld.BR;
import com.example.arthome.newexchangeworld.Models.ExchangeModel;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.databinding.ItemExchangingBinding;

import java.util.List;

/**
 * Created by arthome on 2016/12/6.
 */

public class ExchangingAdapter extends RecyclerView.Adapter<ExchangingAdapter.ExchangeViewHolder> {
    private List<ExchangeModel> exchangeModels;
    private ExchangeListener listener;

    public ExchangingAdapter(List<ExchangeModel> exchangeModels){
        this.exchangeModels = exchangeModels;
    }

    @Override
    public ExchangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exchanging, parent, false);
        return new ExchangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExchangeViewHolder holder, int position) {
        GoodsModel otherGoods = exchangeModels.get(position).getOther_goods();
        holder.getBinding().setVariable(BR.other_goods,otherGoods);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return exchangeModels.size();
    }

    public class ExchangeViewHolder extends RecyclerView.ViewHolder{
        private ItemExchangingBinding binding;
        public ExchangeViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.itemExchangeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.layoutClicked(exchangeModels.get(getAdapterPosition()));
                }
            });
        }

        public ItemExchangingBinding getBinding(){
            return binding;
        }
    }

    public void setExchangeListener(ExchangeListener listener){
        this.listener = listener;
    }

    interface ExchangeListener{
        public void layoutClicked(ExchangeModel exchangeModel);
    }
}
