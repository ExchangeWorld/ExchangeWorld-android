package com.example.arthome.newexchangeworld.MyPage;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.BR;
import com.example.arthome.newexchangeworld.ChooseGoodsAdapter;
import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.CreateExchangeModel;
import com.example.arthome.newexchangeworld.Models.CreateExchangeResponse;
import com.example.arthome.newexchangeworld.Models.ExchangeRequestModel;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;
import com.example.arthome.newexchangeworld.databinding.ItemExchangeRequestBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arthome on 2016/11/27.
 */

public class ExchangeRequestAdapter extends RecyclerView.Adapter<ExchangeRequestAdapter.ExchangeRequestViewHolder> {
    private List<ExchangeRequestModel> exchangeRequestModelList;
    private Context context;
    private User user;
    private ExchangeRequestAdapterListener listener;

    public ExchangeRequestAdapter(Context context, List<ExchangeRequestModel> exchangeRequestModelList) {
        this.exchangeRequestModelList = exchangeRequestModelList;
        this.context = context;
        user = RealmManager.INSTANCE.retrieveUser().get(0);
    }

    @Override
    public ExchangeRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exchange_request, parent, false);
        return new ExchangeRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExchangeRequestViewHolder holder, int position) {
        ExchangeRequestModel exchangeRequestModel = exchangeRequestModelList.get(position);
        holder.getBinding().setVariable(BR.exchangeRequest, exchangeRequestModel);
        holder.getBinding().executePendingBindings();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.getBinding().itemExhcangeRequestRecyclerView.setLayoutManager(mLayoutManager);


        holder.adapter = new ChooseGoodsAdapter(exchangeRequestModel.getQueue(), 0);
        holder.getBinding().itemExhcangeRequestRecyclerView.setAdapter(holder.adapter);
    }

    @Override
    public int getItemCount() {
        return exchangeRequestModelList.size();
    }

    class ExchangeRequestViewHolder extends RecyclerView.ViewHolder {
        private ItemExchangeRequestBinding binding;
        private ChooseGoodsAdapter adapter;

        public ExchangeRequestViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            binding.itemExchangeRequestConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (adapter.getChosenGid() == -1) {
                        Toast.makeText(context, "請選擇", Toast.LENGTH_SHORT).show();
                    } else {
                        Call<CreateExchangeResponse> call = new RestClient().getExchangeService().createExchange(user.getExToken(), new CreateExchangeModel(exchangeRequestModelList.get(getAdapterPosition()).getGid(), adapter.getChosenGid()));
                        call.enqueue(new Callback<CreateExchangeResponse>() {
                            @Override
                            public void onResponse(Call<CreateExchangeResponse> call, Response<CreateExchangeResponse> response) {
                                if (response.code()==201 || response.code()==200){
                                    Toast.makeText(context, "成功接受請求", Toast.LENGTH_SHORT).show();
                                    listener.refreshAdapter();
                                }else {
                                    Toast.makeText(context, "接受請求失敗 response code錯誤", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<CreateExchangeResponse> call, Throwable t) {
                                Toast.makeText(context, "接受請求失敗 onFailure", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        }

        private ItemExchangeRequestBinding getBinding() {
            return binding;
        }

    }
    public interface ExchangeRequestAdapterListener{
        void refreshAdapter();
    }

    public void setExchangeRequestAdapterListener(ExchangeRequestAdapterListener listener){
        this.listener = listener;
    }
}
