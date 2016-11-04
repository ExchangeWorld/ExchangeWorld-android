package com.example.arthome.newexchangeworld.MyPage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.ItemDetailAdapter;
import com.example.arthome.newexchangeworld.Models.EditGoodsModel;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;
import com.example.arthome.newexchangeworld.util.StringTool;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyItemDetailActivity extends AppCompatActivity {

    TextView goodsDescription;
    TextView goodsName;
    EditText goodsDescriptionEditText, goodsNameEditText;
    LinearLayout whoQueueLayout, deleteLayout, editLayout, shareLayout, infoLayout;
    RecyclerView imageRecyclerView;
    Button saveEditButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_detail);

        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (RealmManager.INSTANCE.retrieveUser().size() != 0) {
            user = RealmManager.INSTANCE.retrieveUser().get(0);
        }
        findUIViews();
        setNormalView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String json = bundle.getString("goodModel");
            final GoodsModel goodsModel = new Gson().fromJson(json, GoodsModel.class);

            imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
            imageRecyclerView.setAdapter(new ItemDetailAdapter(goodsModel.getPhoto_path(), this));

            goodsName.setText(goodsModel.getName());
            goodsDescription.setText(goodsModel.getDescription());

            if (StringTool.INSTANCE.getAllPhotoURL(goodsModel.getPhoto_path()).size() > 1)
                Toast.makeText(this, "向右滑動可顯示多張圖片", Toast.LENGTH_SHORT).show();

            editLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEditView();
                }
            });

            saveEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editGoodsAPI(goodsModel);
                    //TODO  call api
                    setNormalView();
                }
            });

            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyItemDetailActivity.this);
                    alertDialogBuilder.setTitle("刪除物品");
                    alertDialogBuilder.setMessage("是否要刪除物品?");
                    alertDialogBuilder.setPositiveButton("確認刪除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteGoods(goodsModel);
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.create().show();
                }
            });
        }
    }

    private void deleteGoods(GoodsModel goodsModel) {
        Call<ResponseBody> deleteCall = new RestClient().getExchangeService().deleteGoods(goodsModel.getGid(), user.getExToken());
        deleteCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    Toast.makeText(getApplicationContext(),"刪除成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"刪除失敗 status code錯誤",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"刪除失敗 onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editGoodsAPI(GoodsModel goodsModel){
        EditGoodsModel editGoodsModel = new EditGoodsModel(goodsModel.getGid(),goodsModel.getPosition_x(),goodsModel.getPosition_y(),goodsModel.getPhoto_path(),goodsModel.getCategory());
        editGoodsModel.setName(goodsNameEditText.getText().toString());
        editGoodsModel.setDescription(goodsDescriptionEditText.getText().toString());
        Call<ResponseBody> editCall = new RestClient().getExchangeService().editGoods(user.getExToken(), editGoodsModel);
        editCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    Toast.makeText(getApplicationContext(),"編輯成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"編輯失敗 status code錯誤",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"編輯失敗 onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNormalView(){
        goodsName.setVisibility(View.VISIBLE);
        goodsDescription.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.VISIBLE);

        saveEditButton.setVisibility(View.INVISIBLE);
        goodsDescriptionEditText.setVisibility(View.INVISIBLE);
        goodsNameEditText.setVisibility(View.INVISIBLE);
    }

    private void setEditView(){
        goodsName.setVisibility(View.INVISIBLE);
        goodsDescription.setVisibility(View.INVISIBLE);
        infoLayout.setVisibility(View.INVISIBLE);

        saveEditButton.setVisibility(View.VISIBLE);
        goodsDescriptionEditText.setVisibility(View.VISIBLE);
        goodsNameEditText.setVisibility(View.VISIBLE);

        goodsNameEditText.setText(goodsName.getText());
        goodsDescriptionEditText.setText(goodsDescription.getText());
    }

    public void findUIViews() {
        goodsDescription = (TextView) findViewById(R.id.my_goods_description_detail);
        goodsName = (TextView) findViewById(R.id.my_goods_name_detail);
        imageRecyclerView = (RecyclerView) findViewById(R.id.my_item_detail_recycler_view);
        infoLayout = (LinearLayout) findViewById(R.id.item_detail_info_layout);
        shareLayout = (LinearLayout) findViewById(R.id.my_item_detail_share_layout);
        deleteLayout = (LinearLayout) findViewById(R.id.my_item_detail_delete_layout);
        editLayout = (LinearLayout) findViewById(R.id.my_item_detail_edit_layout);
        whoQueueLayout = (LinearLayout) findViewById(R.id.my_item_detail_who_queue_layout);
        goodsDescriptionEditText = (EditText) findViewById(R.id.my_goods_description_detail_edit_text);
        goodsNameEditText = (EditText) findViewById(R.id.my_goods_name_detail_edit_text);
        saveEditButton = (Button) findViewById(R.id.my_item_detail_save_edit_button);
    }
}
