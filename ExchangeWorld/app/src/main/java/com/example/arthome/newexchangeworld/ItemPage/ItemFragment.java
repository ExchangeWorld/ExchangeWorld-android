package com.example.arthome.newexchangeworld.ItemPage;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.google.android.gms.location.places.AutocompletePrediction;

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

/**
 * Created by arthome on 2016/4/10.
 */
public class ItemFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> items = new ArrayList<>();

    public ItemFragment(){};

    public static ItemFragment newInstance(){
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
        new downloadAPI().execute("http://exwd.csie.org:43002/api/goods/1");
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

    public class downloadAPI extends AsyncTask<String,String,List<GoodsModel>> {

        @Override
        protected List<GoodsModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect(); // it still works without this line, don't know why

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine())!= null){
                    buffer.append(line);
                }
                String sJson = buffer.toString();
                JSONObject mjson = new JSONObject(sJson);
                JSONObject onwerjson;
                onwerjson = mjson.getJSONObject("owner");
                List<GoodsModel> goodsModelList = new ArrayList<>();

                GoodsModel goodsModel = new GoodsModel();
                goodsModel.setOnwer_name(onwerjson.getString("name"));
                goodsModel.setName(mjson.getString("name"));
                goodsModel.setCategory(mjson.getString("category"));
                String goods_imageUrl = mjson.getString("photo_path");
                goods_imageUrl = goods_imageUrl.substring(2,goods_imageUrl.length()-2);
                goodsModel.setPhoto_path(goods_imageUrl);

                goodsModelList.add(goodsModel);
                goodsModelList.add(goodsModel);
                goodsModelList.add(goodsModel);
                goodsModelList.add(goodsModel);

                String itemName = mjson.getString("name");
                double pos_x = mjson.getDouble("position_x");
                String description = mjson.getString("description");
               return goodsModelList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                if(reader != null){
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
        protected void onPostExecute(List<GoodsModel> result) {
            super.onPostExecute(result);
            //mText.setText(result.toString());
            //TODO need to set data to list
            if(result != null){
                //列數為2
                int spanCount = 2;
                mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new ItemAdapter(result);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

}
