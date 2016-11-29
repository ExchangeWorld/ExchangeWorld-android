package com.example.arthome.newexchangeworld.MyPage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthome.newexchangeworld.BR;
import com.example.arthome.newexchangeworld.ChooseGoodsAdapter;
import com.example.arthome.newexchangeworld.Models.ExchangeRequestModel;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.databinding.ItemExchangeRequestBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthome on 2016/11/27.
 */

public class ExchangeRequestAdapter extends RecyclerView.Adapter<ExchangeRequestAdapter.ExchangeRequestViewHolder> {
    private List<ExchangeRequestModel> exchangeRequestModelList;
    private Context context;
    public ExchangeRequestAdapter(Context context, List<ExchangeRequestModel> exchangeRequestModelList){
        this.exchangeRequestModelList = exchangeRequestModelList;
        this.context = context;
    }

    @Override
    public ExchangeRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exchange_request,parent,false);
        return new ExchangeRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExchangeRequestViewHolder holder, int position) {
        ExchangeRequestModel exchangeRequestModel = exchangeRequestModelList.get(position);
        holder.getBinding().setVariable(BR.exchangeRequest, exchangeRequestModel);
        holder.getBinding().executePendingBindings();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context ,LinearLayoutManager.HORIZONTAL,false);
        holder.getBinding().itemExhcangeRequestRecyclerView.setLayoutManager(mLayoutManager);


        holder.getBinding().itemExhcangeRequestRecyclerView.setAdapter(new ChooseGoodsAdapter(exchangeRequestModel.getQueue(), 0));
    }

    @Override
    public int getItemCount() {
        return exchangeRequestModelList.size();
    }

    class ExchangeRequestViewHolder extends RecyclerView.ViewHolder {
        private ItemExchangeRequestBinding binding;

        public ExchangeRequestViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        private ItemExchangeRequestBinding getBinding() {
            return binding;
        }
    }
}
