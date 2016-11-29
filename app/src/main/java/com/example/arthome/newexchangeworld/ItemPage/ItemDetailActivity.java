package com.example.arthome.newexchangeworld.ItemPage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.Constant;
import com.example.arthome.newexchangeworld.CustomViews.CustomMyGoodsDialog;
import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.ItemDetailAdapter;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.QueueRequestModel;
import com.example.arthome.newexchangeworld.Models.QueueResponseModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;
import com.example.arthome.newexchangeworld.util.StringTool;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity {

    ImageView goodsImage;
    TextView ownerName;
    TextView goodsDescription;
    TextView goodsName;
    LinearLayout quequeLayout;
    RecyclerView imageRecyclerView;
    User user = null;
    private GoodsModel goodsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        if (RealmManager.INSTANCE.retrieveUser().size() != 0)
            user = RealmManager.INSTANCE.retrieveUser().get(0);

        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // recovering data from MainActivity, sent via intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String json = bundle.getString(Constant.INTENT_GOODS);
            goodsModel = new Gson().fromJson(json, GoodsModel.class);

            setUpUIView();
            imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
            imageRecyclerView.setAdapter(new ItemDetailAdapter(goodsModel.getPhoto_path(), this));

            ownerName.setText(goodsModel.getOwner().getName());
            goodsName.setText(goodsModel.getName());
            goodsDescription.setText(goodsModel.getDescription());

            if (StringTool.INSTANCE.getAllPhotoURL(goodsModel.getPhoto_path()).size() > 1)
                Toast.makeText(this, "向右滑動可顯示多張圖片", Toast.LENGTH_SHORT).show();
        }

        if (user != null) {    //有登入才能執行的功能
            quequeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                showQueueDialog();
                }
            });
        }
    }

    public void setUpUIView() {
        ownerName = (TextView) findViewById(R.id.owner_name_detail);
        goodsDescription = (TextView) findViewById(R.id.goods_description_detail);
        goodsName = (TextView) findViewById(R.id.goods_name_detail);
        imageRecyclerView = (RecyclerView) findViewById(R.id.item_detail_recycler_view);
        quequeLayout = (LinearLayout) findViewById(R.id.item_detail_queue_layout);
    }

    private void showQueueDialog() {
        Call<List<GoodsModel>> call = new RestClient().getExchangeService().downloadMyGoods(user.getUid());
        call.enqueue(new Callback<List<GoodsModel>>() {
            @Override
            public void onResponse(Call<List<GoodsModel>> call, final Response<List<GoodsModel>> response) {
                if (response.code() == 200) {
                    List<GoodsModel> goodsModels = new ArrayList<GoodsModel>();

                    for (int i = 0; i < response.body().size(); i++) {    //把尚未交換的物品加入要顯示的List
                        if (response.body().get(i).getExchanged() == 0) {
                            goodsModels.add(response.body().get(i));
                        }
                    }
                    final CustomMyGoodsDialog dialog = new CustomMyGoodsDialog(ItemDetailActivity.this, goodsModels);
                    dialog.setMyGoodsDialogListener(new CustomMyGoodsDialog.MyGoodsDialogListener() {
                        @Override
                        public void quequeClicked(int gid) {
                            quequeRequest(gid);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "下載物品失敗 status code錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GoodsModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "下載物品失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void quequeRequest(int quequerGid){
        Call<QueueResponseModel> call = new RestClient().getExchangeService().quequeGoods(user.getExToken(),new QueueRequestModel(goodsModel.getGid(),quequerGid));
        call.enqueue(new Callback<QueueResponseModel>() {
            @Override
            public void onResponse(Call<QueueResponseModel> call, Response<QueueResponseModel> response) {
                if(response.code()==200){
                    Toast.makeText(getApplicationContext(),"已發出排請求",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"失敗 response code錯誤",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QueueResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"失敗 onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //go back arrow
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
