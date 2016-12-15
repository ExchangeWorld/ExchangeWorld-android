package com.example.arthome.newexchangeworld.UserPage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.StarModel;
import com.example.arthome.newexchangeworld.Models.UserModel;
import com.example.arthome.newexchangeworld.MyPage.WishListFragment;
import com.example.arthome.newexchangeworld.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arthome on 2016/4/10.
 */
public class UserWishListFragment extends WishListFragment {


    public UserWishListFragment() {
    }

    public static UserWishListFragment newInstance() {
        UserWishListFragment fragment = new UserWishListFragment();
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
    }


    public void refreshGoodsModelList(List<GoodsModel> goodsModelList) {
        this.goodsModelList.clear();
        for (int i = 0; i < goodsModelList.size(); i++) {
            GoodsModel goodsModel = goodsModelList.get(i);
            goodsModel.setStarredByUser(true);  //回傳的沒有這項 手動加入
            this.goodsModelList.add(goodsModel);
        }
        mAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}
