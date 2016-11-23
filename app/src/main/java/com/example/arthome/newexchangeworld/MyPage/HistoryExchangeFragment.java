package com.example.arthome.newexchangeworld.MyPage;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.ExchangeModel;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
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
public class HistoryExchangeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private HistoryFindAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog;
    private List<ExchangeModel> exchangeModels = new ArrayList<>();
    private User user;

    public HistoryExchangeFragment() {

    }

    public static HistoryExchangeFragment newInstance() {
        HistoryExchangeFragment fragment = new HistoryExchangeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = RealmManager.INSTANCE.retrieveUser().get(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //原本的initView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_history_recyclerview);

        //列數為2
        int spanCount = 1;
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HistoryFindAdapter(exchangeModels, getContext());
        mRecyclerView.setAdapter(mAdapter);

        progressDialog = new ProgressDialog(getContext());
    }

    private void downloadHistoryExchanges(){
        progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait", true);

        Call<List<ExchangeModel>> call = new RestClient().getExchangeService().historyExchange(user.getUid(), user.getExToken());
        call.enqueue(new Callback<List<ExchangeModel>>() {
            @Override
            public void onResponse(Call<List<ExchangeModel>> call, Response<List<ExchangeModel>> response) {
                if (response.code() == 200) {
//                    exchangeModels.clear();

                    exchangeModels = response.body();

                    mAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<ExchangeModel>> call, Throwable t) {
                Toast.makeText(getContext(), "下載物品失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadHistoryExchanges();
    }
}
