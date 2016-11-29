package com.example.arthome.newexchangeworld.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ChooseGoodsAdapter;
import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.User;
import com.example.arthome.newexchangeworld.databinding.DialogMyGoodsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arthome on 2016/11/27.
 */

public class CustomMyGoodsDialog extends Dialog {
    private DialogMyGoodsBinding binding;
    private ChooseGoodsAdapter chooseGoodsAdapter;
    private MyGoodsDialogListener listener;

    public CustomMyGoodsDialog(final Context context, List<GoodsModel> goodsModels) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_my_goods, null, false);
        setContentView(binding.getRoot());

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("想要拿來交換的物品");

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.dialogMyGoodsRecyclerView.setLayoutManager(layoutManager);
        chooseGoodsAdapter = new ChooseGoodsAdapter(goodsModels);
        binding.dialogMyGoodsRecyclerView.setAdapter(chooseGoodsAdapter);

        binding.dialogMyGoodsCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.dialogWhoQueueAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.quequeClicked(chooseGoodsAdapter.getChosenGid());     // return -1 if not chosen
            }
        });
    }

    public void setMyGoodsDialogListener(MyGoodsDialogListener listener){
        this.listener = listener;
    }

    public interface MyGoodsDialogListener{
        void quequeClicked(int gid);
    }
}
