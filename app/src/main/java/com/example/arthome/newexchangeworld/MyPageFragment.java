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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.MyPage.ExchangeRequestFragment;
import com.example.arthome.newexchangeworld.MyPage.HistoryExchangeFragment;
import com.example.arthome.newexchangeworld.MyPage.MyItemFragment;
import com.squareup.picasso.Picasso;


/**
 * Created by arthome on 2016/4/9.
 */
public class MyPageFragment extends Fragment {
    private static final String BUNDLE = "BUNDLE";
    private TextView userName;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;

    public MyPageFragment() {
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
        ImageView head = (ImageView) view.findViewById(R.id.fragment_mypage_user_photo);
        userName = (TextView) view.findViewById(R.id.fragment_mypage_user_name);
        if (RealmManager.INSTANCE.retrieveUser().size() != 0) {
            User user = RealmManager.INSTANCE.retrieveUser().get(0);
            Picasso.with(getContext()).load(user.getPhotoPath()).transform(new CircleTransform()).into(head);
            userName.setText(user.getUserName());
        }
        FragmentManager childFragmentManager = getChildFragmentManager();//fragment in frament
        //set tab to fragment
        pagerAdapter = new myTabPagerAdapter(childFragmentManager);
        viewPager = (ViewPager) view.findViewById(R.id.Viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.TabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    class myTabPagerAdapter extends FragmentPagerAdapter {

        public myTabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override//幾個tab?
        public int getCount() {
            return 4;
        }

        @Override//設定tab對應的fragment
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return oneFragment.newInstance("1", "2");
                case 1:
                    return MyItemFragment.newInstance();
                case 2:
                return ExchangeRequestFragment.newInstance();
                case 3:
                    return HistoryExchangeFragment.newInstance();
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
                    return "交換請求";
                case 3:
                    return "歷史交換";
                default:
                    return null;
            }
        }
    }
}
