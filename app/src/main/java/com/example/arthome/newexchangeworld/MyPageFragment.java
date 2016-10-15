package com.example.arthome.newexchangeworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;


/**
 * Created by arthome on 2016/4/9.
 */
public class MyPageFragment extends Fragment {
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;

    public MyPageFragment(){
        //require empty constructor
    }
    public static MyPageFragment newInstance() {
        MyPageFragment fragment = new MyPageFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mypage, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        //for circle head view
        ImageView head = (ImageView) view.findViewById(R.id.imageView3);
        Bitmap bitHead = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.myhead);
        head.setImageBitmap(MainActivity.getCroppedBitmap(bitHead));

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
            return 5;
        }
        @Override//設定tab對應的fragment
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    return oneFragment.newInstance("1","2");
                case 1:
                    return twoFragment.newInstance("1","2");
                case 2:
                    return oneFragment.newInstance("1","2");
                case 3:
                    return twoFragment.newInstance("1","2");
                case 4:
                    return oneFragment.newInstance("1","2");
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
                    return "交換成功";
                case 3:
                    return "交換請求";
                case 4:
                    return "歷史交換";
                default:
                    return null;
            }
        }
    }
}
