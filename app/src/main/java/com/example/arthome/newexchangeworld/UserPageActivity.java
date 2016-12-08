package com.example.arthome.newexchangeworld;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.CustomViews.CustomMessageDialog;
import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.ChatRoomModel;
import com.example.arthome.newexchangeworld.Models.Owner;
import com.example.arthome.newexchangeworld.databinding.ActivityUserPageBinding;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPageActivity extends AppCompatActivity {
    private Owner owner;
    private User user = null;
    private ActivityUserPageBinding binding;
    private PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_page);

        owner = new Gson().fromJson(getIntent().getStringExtra(Constant.INTENT_OWNER), Owner.class);
        binding.setOwner(owner);

        pagerAdapter = new myTabPagerAdapter(getSupportFragmentManager());
        binding.userpageViewpager.setAdapter(pagerAdapter);
        binding.userpageTabLayout.setupWithViewPager(binding.userpageViewpager);


        binding.userpageSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChatRoomInfo();
            }
        });


        //TODO exwd.csie.org:43002/api/user/8 call這個解決三個頁面
    }

    private void getChatRoomInfo() {
        Call<ChatRoomModel> call = new RestClient().getExchangeService().getChatRoom(Integer.toString(owner.getUid()), user.getExToken());
        call.enqueue(new Callback<ChatRoomModel>() {
            @Override
            public void onResponse(Call<ChatRoomModel> call, Response<ChatRoomModel> response) {
                if (response.code() == 200) {
                    CustomMessageDialog dialog = new CustomMessageDialog(UserPageActivity.this, user, response.body());
                    dialog.show();
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
                    return oneFragment.newInstance("", "");
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
    }
}
