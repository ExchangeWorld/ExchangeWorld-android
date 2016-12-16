package com.example.arthome.newexchangeworld.MyPage;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.Constant;
import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.ItemPage.ItemAdapter;
import com.example.arthome.newexchangeworld.ItemPage.ItemDetailActivity;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.StarModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arthome on 2016/4/10.
 */
public class MyWishListFragment extends WishListFragment {

    public MyWishListFragment() {

    }

    public static MyWishListFragment newInstance() {
        MyWishListFragment fragment = new MyWishListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();

        downLoadGoods();
    }

    private void downLoadGoods() {
        Call<List<StarModel>> call = new RestClient().getExchangeService().getUserWishList(user.getUid());
        call.enqueue(new Callback<List<StarModel>>() {
            @Override
            public void onResponse(Call<List<StarModel>> call, Response<List<StarModel>> response) {
                if (response.code() == 200) {
                    goodsModelList.clear();
                    for (int i = 0; i < response.body().size(); i++) {
                        GoodsModel goodsModel = response.body().get(i).getGoods();
                        goodsModel.setStarredByUser(true);  //回傳的沒有這項 手動加入
                        goodsModelList.add(goodsModel);
                    }
                    mAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                } else
                    Toast.makeText(getContext(), "下載物品失敗 status code錯誤", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<StarModel>> call, Throwable t) {
                Toast.makeText(getContext(), "下載物品失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
