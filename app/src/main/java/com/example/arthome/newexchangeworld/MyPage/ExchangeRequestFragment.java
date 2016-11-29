package com.example.arthome.newexchangeworld.MyPage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.ExchangeRequestModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arthome on 2016/11/30.
 */

public class ExchangeRequestFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ExchangeRequestAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog;
    private List<ExchangeRequestModel> exchangeRequestModels = new ArrayList<>();
    private User user;

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
        mAdapter = new ExchangeRequestAdapter(getContext(), exchangeRequestModels);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        progressDialog = new ProgressDialog(getActivity());
    }

    private void downloadExchangeRequest() {
        progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait", true);
//
        Call<List<ExchangeRequestModel>> call = new RestClient().getExchangeService().getMyExchangeRequest(user.getExToken());
        call.enqueue(new Callback<List<ExchangeRequestModel>>() {
            @Override
            public void onResponse(Call<List<ExchangeRequestModel>> call, Response<List<ExchangeRequestModel>> response) {
                if (response.code() == 200) {
                    exchangeRequestModels.clear();
                    exchangeRequestModels.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<ExchangeRequestModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadExchangeRequest();
    }

    public static ExchangeRequestFragment newInstance() {
        ExchangeRequestFragment fragment = new ExchangeRequestFragment();
        return fragment;
    }
}
