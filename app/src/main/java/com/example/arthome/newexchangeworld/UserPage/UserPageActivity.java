package com.example.arthome.newexchangeworld.UserPage;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.Constant;
import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.MessageActivity;
import com.example.arthome.newexchangeworld.Models.ChatRoomModel;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.Owner;
import com.example.arthome.newexchangeworld.Models.UserModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;
import com.example.arthome.newexchangeworld.databinding.ActivityUserPageBinding;
import com.example.arthome.newexchangeworld.oneFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPageActivity extends AppCompatActivity {
    private Owner owner;
    private User user = null;
    private ActivityUserPageBinding binding;
    private PagerAdapter pagerAdapter;
    private UserModel userModel;
    private UserWishListFragment userWishListFragment = UserWishListFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_page);

        owner = new Gson().fromJson(getIntent().getStringExtra(Constant.INTENT_OWNER), Owner.class);
        binding.setOwner(owner);

        pagerAdapter = new myTabPagerAdapter(getSupportFragmentManager());
        binding.userpageViewpager.setAdapter(pagerAdapter);
        binding.userpageViewpager.setOffscreenPageLimit(2);
        binding.userpageTabLayout.setupWithViewPager(binding.userpageViewpager);


        binding.userpageSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChatRoomInfo();
            }
        });
    }

    private void getChatRoomInfo() {
        Call<ChatRoomModel> call = new RestClient().getExchangeService().getChatRoom(Integer.toString(owner.getUid()), user.getExToken());
        call.enqueue(new Callback<ChatRoomModel>() {
            @Override
            public void onResponse(Call<ChatRoomModel> call, Response<ChatRoomModel> response) {
                if (response.code() == 200) {
                    Intent intent = new Intent(UserPageActivity.this, MessageActivity.class);
                    intent.putExtra(Constant.INTENT_CHATROOM_MODEL, new Gson().toJson(response.body()));
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "開啟聊天室失敗 status code錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatRoomModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "開啟聊天室失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class myTabPagerAdapter extends FragmentPagerAdapter {

        public myTabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override//幾個tab?
        public int getCount() {
            return 3;
        }

        @Override//設定tab對應的fragment
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return userWishListFragment;
                case 1:
                    return oneFragment.newInstance("", "");
                case 2:
                    return oneFragment.newInstance("", "");
                default:
                    return null;
            }
        }

        @Override // tab顯示的text
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "願望清單";
                case 1:
                    return "等待交換";
                case 2:
                    return "歷史交換";
                default:
                    return null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        user = null;
        if (RealmManager.INSTANCE.retrieveUser().size() != 0) {
            user = RealmManager.INSTANCE.retrieveUser().get(0);
        }
        downloadUserInfo();
    }

    public void downloadUserInfo() {
        Call<UserModel> call = new RestClient().getExchangeService().getUserInfo(owner.getUid(), "");
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == 200) {
                    userModel = response.body();
                    List<GoodsModel> goodsModelList = new ArrayList<GoodsModel>();
                    goodsModelList.clear();
                    for(int i = 0; i<userModel.getStar_starring_user().size();i++){
                        goodsModelList.add(userModel.getStar_starring_user().get(i).getGoods());
                    }
                    userWishListFragment.refreshGoodsModelList(goodsModelList);
                } else {
                    Toast.makeText(getApplicationContext(), "下載物品失敗 status code錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "下載物品失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
