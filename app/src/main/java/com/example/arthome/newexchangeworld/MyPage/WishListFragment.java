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
public class WishListFragment extends Fragment {
    private User user;
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<GoodsModel> goodsModelList = new ArrayList<>();
    private ProgressDialog progressDialog;

    public WishListFragment() {

    }

    public static WishListFragment newInstance() {
        WishListFragment fragment = new WishListFragment();
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

        //原本的initView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //列數為2
        int spanCount = 2;
        mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ItemAdapter(goodsModelList);
        mAdapter.setMyViewHolderClicks(new ItemAdapter.MyViewHolderClicks() {
            @Override
            public void onGoodsClick(View itemView, int position) {
                GoodsModel goodsModel = goodsModelList.get(position);
                Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                intent.putExtra(Constant.INTENT_GOODS, new Gson().toJson(goodsModel));
                startActivity(intent);

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait", true);

    }

    @Override
    public void onResume() {
        super.onResume();
        user = null;
        if (RealmManager.INSTANCE.retrieveUser().size() != 0)
            user = RealmManager.INSTANCE.retrieveUser().get(0);

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
