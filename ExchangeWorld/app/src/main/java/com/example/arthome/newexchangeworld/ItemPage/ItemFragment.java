package com.example.arthome.newexchangeworld.ItemPage;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;

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

                List<GoodsModel> goodsModelList = new ArrayList<>();

                GoodsModel goodsModel = new GoodsModel();
                goodsModel.setName(mjson.getString("name"));
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

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder> {
        //private ArrayList<String> items = new ArrayList<>();
        private List<GoodsModel> goodsModel ;

        public ItemAdapter(List<GoodsModel> goodsModel) {
            this.goodsModel = goodsModel;
        }




        @Override
        public viewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods, viewGroup, false);
            return new viewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(viewHolder viewHolder, int position) {
            //  String info = items.get(position);
            View view = viewHolder.itemView;
            TextView goods_textView = (TextView) view.findViewById(R.id.id_goods_name);
            goods_textView.setText(goodsModel.get(0).getName()); //TODO change to array
            TextView user_textView = (TextView) view.findViewById(R.id.id_user_name);
            user_textView.setText("Art");
        }

        @Override
        public int getItemCount() {
            return goodsModel.size();
        }

        class viewHolder extends RecyclerView.ViewHolder {
            public viewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
