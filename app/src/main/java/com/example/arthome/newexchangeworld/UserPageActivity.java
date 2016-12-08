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

import com.example.arthome.newexchangeworld.Models.Owner;
import com.example.arthome.newexchangeworld.databinding.ActivityUserPageBinding;
import com.google.gson.Gson;

public class UserPageActivity extends AppCompatActivity {
    private Owner owner;
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

        //TODO exwd.csie.org:43002/api/user/8 call這個解決三個頁面
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
                    return oneFragment.newInstance("","");
                case 1:
                    return oneFragment.newInstance("","");
                case 2:
                    return oneFragment.newInstance("","");
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

}
