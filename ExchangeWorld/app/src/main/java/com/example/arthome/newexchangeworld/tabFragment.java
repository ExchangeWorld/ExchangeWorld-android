package com.example.arthome.newexchangeworld;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthome.newexchangeworld.ItemPage.ItemFragment;

/**
 * Created by arthome on 2016/4/9.
 */
public class tabFragment extends Fragment {
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;

    public tabFragment(){
        //require empty constructor
    }
    public static tabFragment newInstance() {
        tabFragment fragment = new tabFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        FragmentManager childFragmentManager = getChildFragmentManager();//fragment in frament
        //set tab to fragment
        pagerAdapter =new myTabPagerAdapter(childFragmentManager);
        viewPager = (ViewPager) view.findViewById(R.id.Viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.TabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    class myTabPagerAdapter extends FragmentPagerAdapter {

        public  myTabPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override//幾個tab?
        public int getCount(){
            return 3;
        }
        @Override//設定tab對應的fragment
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    return oneFragment.newInstance("1","2");
                case 1:
                    return ItemFragment.newInstance();
                case 2:
                    return oneFragment.newInstance("1","2");
                default:
                    return null;
            }
        }

        @Override // tab顯示的text
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "地圖搜尋";
                case 1:
                    return "物件分類";
                case 2:
                    return "地區分類";
                default:
                    return null;
            }
        }
    }
}