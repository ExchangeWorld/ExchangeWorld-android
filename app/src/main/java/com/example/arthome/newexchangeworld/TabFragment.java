package com.example.arthome.newexchangeworld;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ItemPage.BlankFragment;
import com.example.arthome.newexchangeworld.SearchTab.AreaFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by arthome on 2016/4/9.
 */
public class TabFragment extends Fragment implements AreaFragment.AreaSelectedListener{
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;

    public TabFragment(){
        //require empty constructor
    }
    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();

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


        FragmentManager childFragmentManager = getChildFragmentManager();//fragment in fragment
        //set tab to fragment
        pagerAdapter =new myTabPagerAdapter(childFragmentManager);
        viewPager = (ViewPager) view.findViewById(R.id.Viewpager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2); //create the two fragment beside, more memory needed
        tabLayout = (TabLayout) view.findViewById(R.id.TabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        switch (tab.getPosition()){
                            case 1 : //item_categorypage reselected
                                FragmentManager fm = getChildFragmentManager();
                                if(fm.getBackStackEntryCount()>0) { //popback to category page
                                    fm.popBackStack();
                                }
                                break;
                        }
                        super.onTabReselected(tab);
                    }
                });
    }

    @Override
    public void MapZooming(LatLng latLng, int zoomSize) {
        //spend five hour finding, viewpager autoTag the fragments Tag name "android:switcher:" + R.id.Viewpager + ":0"
        MapFragment mapF = (MapFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.Viewpager + ":0");
        if(mapF!=null){
            mapF.move(latLng,zoomSize);
        }
        else {
            Toast.makeText(getContext(),"not suceess",Toast.LENGTH_SHORT).show();
        }
    }

    public MapFragment getMapFragment(){
        MapFragment mapF = (MapFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.Viewpager + ":0");
        return mapF;
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
                    return MapFragment.newInstance();
                //oneFragment.newInstance("1","2")
                case 1:
                    return BlankFragment.newInstance();
                case 2:
                    return AreaFragment.newInstance();
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
