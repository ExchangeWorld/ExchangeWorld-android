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
                            Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                            intent.putExtra("goodModel", new Gson().toJson(goodsModel));
                            startActivity(intent);
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
       /*
        //列數為2
        int spanCount = 2;
        mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //建構臨時數據源
        for (int i = 0; i < 10; i++) {
            items.add("item"+i);
        }
        mAdapter = new ItemAdapter(items);
        mRecyclerView.setAdapter(mAdapter);
    */
    }

    public class downloadAPI extends AsyncTask<String, String, List<GoodsModel>> {

        private ProgressDialog progressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait", true);
            super.onPreExecute();
        }

        @Override
        protected List<GoodsModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //int goods_count=1;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect(); // it still works without this line, don't know why
                List<GoodsModel> goodsModelList = new ArrayList<>();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String sJson;
                sJson = buffer.toString();

                JSONArray jsonArray = null; //try and catch?
                try {
                    jsonArray = new JSONArray(sJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                GoodsModel goodsModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    Gson gson = new Gson();
                    try {
                        goodsModel = gson.fromJson(jsonArray.get(i).toString(), GoodsModel.class);
                        String goods_imageUrl = goodsModel.getPhoto_path();
                        goods_imageUrl = goods_imageUrl.substring(2, goods_imageUrl.length() - 2);
                        goodsModel.setPhoto_path(goods_imageUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (goodsModel != null) {
                        //Log.i("oscart",Integer.toString(i)+goodsModel.getName());
                        goodsModelList.add(goodsModel);
                    }
                }

                return goodsModelList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(final List<GoodsModel> result) {
            super.onPostExecute(result);
            //mText.setText(result.toString());
            //TODO need to set data to list
            if (result != null) {
                //列數為2
                int spanCount = 2;
                mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new ItemAdapter(result);
                mAdapter.setMyViewHolderClicks(new ItemAdapter.MyViewHolderClicks() {
                    @Override
                    public void onGoodsClick(View itemView, int position) {
                        GoodsModel goodsModel = result.get(position);
                        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                        intent.putExtra("goodModel", new Gson().toJson(goodsModel));
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
                progressDialog.dismiss();
            }
        }
    }
}
