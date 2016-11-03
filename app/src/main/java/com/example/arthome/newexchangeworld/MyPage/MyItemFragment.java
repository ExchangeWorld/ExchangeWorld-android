package com.example.arthome.newexchangeworld.MyPage;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arthome on 2016/4/10.
 */
public class MyItemFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog;
    private User user;

    public MyItemFragment() {

    }

    public static MyItemFragment newInstance() {
        MyItemFragment fragment = new MyItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = RealmManager.INSTANCE.retrieveUser().get(0);
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

        progressDialog = new ProgressDialog(getContext());
        progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait", true);

        Call<List<GoodsModel>> downloadOwnerGoods = new RestClient().getExchangeService().downloadMyGoods(user.getUid());
        downloadOwnerGoods.enqueue(new Callback<List<GoodsModel>>() {
            @Override
            public void onResponse(Call<List<GoodsModel>> call, final Response<List<GoodsModel>> response) {
                if (response.code() == 200) {
                    //TODO 可能要把已經交換的排除? body裡有一個delete欄位
                    mAdapter = new MyItemAdapter(response.body());
                    mAdapter.setMyViewHolderClicks(new MyItemAdapter.MyViewHolderClicks() {
                        @Override
                        public void onGoodsClick(View itemView, int position) {
                            GoodsModel goodsModel = response.body().get(position);
                            Intent intent = new Intent(getActivity(), MyItemDetailActivity.class);
                            intent.putExtra("goodModel", new Gson().toJson(goodsModel));
                            startActivity(intent);
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "下載物品失敗 status code錯誤", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<GoodsModel>> call, Throwable t) {
                Toast.makeText(getContext(), "下載物品失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
