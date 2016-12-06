package com.example.arthome.newexchangeworld.MyPage;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.example.arthome.newexchangeworld.Models.ExchangeModel;
import com.example.arthome.newexchangeworld.R;

import java.util.List;

/**
 * Created by art on 2016/11/22.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<ExchangeModel> exchangeModels;

    public HistoryAdapter(List<ExchangeModel> exchangeModels){
        this.exchangeModels = exchangeModels;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        ExchangeModel exchangeModel = exchangeModels.get(position);
        holder.getBinding().setVariable(BR.exchangeModel, exchangeModel);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return exchangeModels.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{
        private ViewDataBinding binding;

        public HistoryViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

    }
}
