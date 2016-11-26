package com.example.arthome.newexchangeworld.ItemPage;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.Constant;
import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.MainActivity;
import com.example.arthome.newexchangeworld.MyPage.MyItemDetailActivity;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;
import com.google.android.gms.maps.MapFragment;
import com.google.gson.Gson;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class ItemFragment extends Fragment {
    private User user;
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> items = new ArrayList<>();
    private ProgressDialog progressDialog;

    public ItemFragment() {

    }

    ;

    public static ItemFragment newInstance() {
        ItemFragment fragment = new ItemFragment();
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

        progressDialog = new ProgressDialog(getContext());
        progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait", true);

        //get which url to download
        Bundle bundle = getArguments();
        String category = bundle.getString(Constant.INTENT_CATEGORY);

        Call<List<GoodsModel>> downloadCategoryGoods = new RestClient().getExchangeService().downloadCategoryGoods(category);
        downloadCategoryGoods.enqueue(new Callback<List<GoodsModel>>() {
            @Override
            public void onResponse(Call<List<GoodsModel>> call, final Response<List<GoodsModel>> response) {
                if (response.code() == 200) {
                    mAdapter = new ItemAdapter(response.body());
                    mAdapter.setMyViewHolderClicks(new ItemAdapter.MyViewHolderClicks() {
                        @Override
                        public void onGoodsClick(View itemView, int position) {
                            GoodsModel goodsModel = response.body().get(position);
                            if (user != null && user.getUid() == goodsModel.getOwner().getUid()) {
                                Intent intent = new Intent(getActivity(), MyItemDetailActivity.class);
                                intent.putExtra(Constant.INTENT_GOODS, new Gson().toJson(goodsModel));
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                                intent.putExtra(Constant.INTENT_GOODS, new Gson().toJson(goodsModel));
                                startActivity(intent);
                            }
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                    progressDialog.dismiss();
                } else
                    Toast.makeText(getContext(), "下載物品失敗 status code錯誤", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<GoodsModel>> call, Throwable t) {
                Toast.makeText(getContext(), "下載物品失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        user = null;
        if (RealmManager.INSTANCE.retrieveUser().size() != 0)
            user = RealmManager.INSTANCE.retrieveUser().get(0);
    }
}
