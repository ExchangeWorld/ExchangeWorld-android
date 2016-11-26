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

import com.example.arthome.newexchangeworld.Constant;
import com.example.arthome.newexchangeworld.CustomViews.CustomWhoQueueDialog;
import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.ItemDetailAdapter;
import com.example.arthome.newexchangeworld.Models.EditGoodsModel;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.QueueOfGoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;
import com.example.arthome.newexchangeworld.util.StringTool;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyItemDetailActivity extends AppCompatActivity {

    private TextView goodsDescription;
    private TextView goodsName;
    private EditText goodsDescriptionEditText, goodsNameEditText;
    private LinearLayout whoQueueLayout, deleteLayout, editLayout, shareLayout, infoLayout, categoryLayout;
    private RecyclerView imageRecyclerView;
    private Button saveEditButton, categoryEditButton;
    private User user;
    private GoodsModel goodsModel;
    private String[] categoryName;
    private String[] categoryValue;
    private int categoryPicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_detail);

        categoryName = getResources().getStringArray(R.array.classChinese);
        categoryValue = getResources().getStringArray(R.array.classType);

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
            String json = bundle.getString(Constant.INTENT_GOODS);
            goodsModel = new Gson().fromJson(json, GoodsModel.class);

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

            categoryEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    android.app.AlertDialog.Builder classList = new android.app.AlertDialog.Builder(MyItemDetailActivity.this);
                    classList.setTitle("選擇類別");
                    classList.setItems(categoryName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            categoryEditButton.setText(categoryName[which]);
                            categoryPicked = which;
                        }
                    });
                    classList.show();
                }
            });

            saveEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editGoodsAPI(goodsModel);
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

            whoQueueLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lookWhosQuequeing(goodsModel.getGid());
                }
            });
        }
    }

    private void deleteGoods(GoodsModel goodsModel) {
        Call<ResponseBody> deleteCall = new RestClient().getExchangeService().deleteGoods(goodsModel.getGid(), user.getExToken());
        deleteCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "刪除成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "刪除失敗 status code錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "刪除失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editGoodsAPI(final GoodsModel goodsModel) {
        EditGoodsModel editGoodsModel = new EditGoodsModel(goodsModel.getGid(), goodsModel.getPosition_x(), goodsModel.getPosition_y(), goodsModel.getPhoto_path(), goodsModel.getCategory());
        final String editedName = goodsNameEditText.getText().toString();
        final String editedDescription = goodsDescriptionEditText.getText().toString();

        editGoodsModel.setName(editedName);
        editGoodsModel.setDescription(editedDescription);
        editGoodsModel.setCategory(categoryValue[categoryPicked]);
        Call<ResponseBody> editCall = new RestClient().getExchangeService().editGoods(user.getExToken(), editGoodsModel);
        editCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "編輯成功", Toast.LENGTH_SHORT).show();
                    goodsName.setText(editedName);
                    goodsDescription.setText(editedDescription);
                    goodsModel.setDescription(editedName);
                    goodsModel.setDescription(editedDescription);
                    goodsModel.setCategory(categoryValue[categoryPicked]);
                } else {
                    Toast.makeText(getApplicationContext(), "編輯失敗 status code錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "編輯失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void lookWhosQuequeing(int gid){
        Call<List<QueueOfGoodsModel>> call = new RestClient().getExchangeService().quequeOfGoods(gid,user.getExToken());
        call.enqueue(new Callback<List<QueueOfGoodsModel>>() {
            @Override
            public void onResponse(Call<List<QueueOfGoodsModel>> call, Response<List<QueueOfGoodsModel>> response) {
                if (response.code() == 200) {
                    List<QueueOfGoodsModel> queueOfGoodsModelList = response.body();
                    if(queueOfGoodsModelList.size()==0){
                        //TODO 11/11沒人排
                    }else {
                        //TODO 11/11跳出排的人
                        CustomWhoQueueDialog dialog = new CustomWhoQueueDialog(MyItemDetailActivity.this,queueOfGoodsModelList);

                        new CustomWhoQueueDialog(MyItemDetailActivity.this,queueOfGoodsModelList).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "看誰排失敗 status code錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QueueOfGoodsModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "看誰排失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setNormalView() {
        goodsName.setVisibility(View.VISIBLE);
        goodsDescription.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.VISIBLE);

        saveEditButton.setVisibility(View.INVISIBLE);
        goodsDescriptionEditText.setVisibility(View.INVISIBLE);
        categoryLayout.setVisibility(View.INVISIBLE);
        goodsNameEditText.setVisibility(View.INVISIBLE);
    }

    private void setEditView() {
        goodsName.setVisibility(View.INVISIBLE);
        goodsDescription.setVisibility(View.INVISIBLE);
        infoLayout.setVisibility(View.INVISIBLE);

        saveEditButton.setVisibility(View.VISIBLE);
        goodsDescriptionEditText.setVisibility(View.VISIBLE);
        categoryLayout.setVisibility(View.VISIBLE);
        goodsNameEditText.setVisibility(View.VISIBLE);

        goodsNameEditText.setText(goodsName.getText());
        goodsDescriptionEditText.setText(goodsDescription.getText());
        int categoryIndex = Arrays.asList(categoryValue).indexOf(goodsModel.getCategory());
        categoryIndex = (categoryIndex==-1)?0:categoryIndex;    //防止物品沒有category導致回傳-1crash
        categoryPicked = categoryIndex;
        categoryEditButton.setText(categoryName[categoryIndex]);
    }

    public void findUIViews() {
        goodsDescription = (TextView) findViewById(R.id.my_goods_description_detail);
        goodsName = (TextView) findViewById(R.id.my_goods_name_detail);
        imageRecyclerView = (RecyclerView) findViewById(R.id.my_item_detail_recycler_view);
        infoLayout = (LinearLayout) findViewById(R.id.item_detail_info_layout);
        shareLayout = (LinearLayout) findViewById(R.id.my_item_detail_share_layout);
        deleteLayout = (LinearLayout) findViewById(R.id.my_item_detail_delete_layout);
        categoryLayout = (LinearLayout) findViewById(R.id.my_item_detail_category_layout);
        editLayout = (LinearLayout) findViewById(R.id.my_item_detail_edit_layout);
        whoQueueLayout = (LinearLayout) findViewById(R.id.my_item_detail_who_queue_layout);
        goodsDescriptionEditText = (EditText) findViewById(R.id.my_goods_description_detail_edit_text);
        goodsNameEditText = (EditText) findViewById(R.id.my_goods_name_detail_edit_text);
        saveEditButton = (Button) findViewById(R.id.my_item_detail_save_edit_button);
        categoryEditButton = (Button) findViewById(R.id.my_item_detail_category_button);
    }
}
