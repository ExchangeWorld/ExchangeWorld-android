package me.amiee.nicetab.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.amiee.nicetab.NiceTabLayout;
import me.amiee.nicetab.NiceTabStrip;
//這頁下方有右上角menu的code, show  tab icon or text

//tab設計
public class DemoFragment extends Fragment {
    private boolean mIosStyleIcon = false;

    public static DemoFragment newInstance( ) {
        DemoFragment fragment = new DemoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DemoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { //set tab Title and icons here
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
     //   Toast.makeText(getContext(),"DemoFragment",Toast.LENGTH_SHORT).show();
        mTabs.add(new SamplePagerItem(
                "男性服飾", // Title
                R.drawable.ic_home, // Icon 連到.xml ios tab style 有tab selected跟not select差別
                R.drawable.clothes, // Icon android icon
                getResources().getColor(R.color.gray), // Indicator color
                getResources().getColor(R.color.gray) // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                "電腦用品", // Title
                R.drawable.ic_activity, // Icon
                R.drawable.computer, // Icon
                getResources().getColor(R.color.gray), // Indicator color
                getResources().getColor(R.color.gray) // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.makeup), // Title
                R.drawable.ic_search, // Icon
                R.drawable.ic_makeup, // Icon
                getResources().getColor(R.color.gray), // Indicator color
                getResources().getColor(R.color.gray) // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.book), // Title
                R.drawable.ic_me, // Icon
                R.drawable.ic_book, // Icon
                getResources().getColor(R.color.gray), // Indicator color
                getResources().getColor(R.color.gray) // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.threec), // Title
                R.drawable.ic_coffee, // Icon
                R.drawable.ic_threec, // Icon
                getResources().getColor(R.color.gray), // Indicator color
                getResources().getColor(R.color.gray) // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.women_cloth), // Title
                R.drawable.ic_coffee, // Icon
                R.drawable.ic_womencloth, // Icon
                getResources().getColor(R.color.gray), // Indicator color
                getResources().getColor(R.color.gray) // Divider color
        ));
    }//OnCreate

    static class SamplePagerItem {//class for tabs?
        private final CharSequence mTitle;
        private final int mIosIconResId;
        private final int mAndroidIconResId;
        private final int mIndicatorColor;
        private final int mDividerColor;

        SamplePagerItem(CharSequence title, int iosIconResId, int androidIconResId, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIosIconResId = iosIconResId;
            mAndroidIconResId = androidIconResId;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        Fragment createFragment() {
            return AppsFragment.newInstance();
        } //這裡設定tab對應的fragment

        CharSequence getTitle() {
            return mTitle;
        }

        int getIosIconResId() {
            return mIosIconResId;
        }

        int getAndroidIconResId() {
            return mAndroidIconResId;
        }

        int getIndicatorColor() {
            return mIndicatorColor;
        }

        int getDividerColor() {
            return mDividerColor;
        }
    }//SamplePager

    private NiceTabLayout mNiceTabLayout;
    private FrameLayout mWrapFl;
    private ViewPager mViewPager;
    private Toolbar mToolbar;

    private List<SamplePagerItem> mTabs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_demo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mWrapFl = (FrameLayout) view.findViewById(R.id.wrap_fl);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager()));

        mNiceTabLayout = (NiceTabLayout) view.findViewById(R.id.sliding_tabs);
        mNiceTabLayout.setViewPager(mViewPager);

        mNiceTabLayout.setTabStripColorize(new NiceTabStrip.TabStripColorize() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }
        });

        mNiceTabLayout.setTabColorize(new NiceTabLayout.TabColorize() {

            @Override
            public int getDefaultTabColor(int position) {
                if (isAdded()) {
                    return getResources().getColor(android.R.color.white);
                } else {
                    return Color.WHITE;
                }
            }

            @Override
            public int getSelectedTabColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }
        });

        mNiceTabLayout.setOnIndicatorColorChangedListener((MainActivity) getActivity());

        //bottom menu
        /*
        mToolbar = (Toolbar) view.findViewById(R.id.demo_toolbar);
        mToolbar.inflateMenu(R.menu.demo_bottom);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.indicator_underline_divider: {
                        mNiceTabLayout.setDrawOrder(NiceTabStrip.DrawOrder.INDICATOR_UNDERLINE_DIVIDER);
                        return true;
                    }
                    case R.id.indicator_divider_underline: {
                        mNiceTabLayout.setDrawOrder(NiceTabStrip.DrawOrder.INDICATOR_DIVIDER_UNDERLINE);
                        return true;
                    }
                    case R.id.underline_indicator_divider: {
                        mNiceTabLayout.setDrawOrder(NiceTabStrip.DrawOrder.UNDERLINE_INDICATOR_DIVIDER);
                        return true;
                    }
                    case R.id.underline_divider_indicator: {
                        mNiceTabLayout.setDrawOrder(NiceTabStrip.DrawOrder.UNDERLINE_DIVIDER_INDICATOR);
                        return true;
                    }
                    case R.id.divider_indicator_underline: {
                        mNiceTabLayout.setDrawOrder(NiceTabStrip.DrawOrder.DIVIDER_INDICATOR_UNDERLINE);
                        return true;
                    }
                    case R.id.divider_underline_indicator: {
                        mNiceTabLayout.setDrawOrder(NiceTabStrip.DrawOrder.DIVIDER_UNDERLINE_INDICATOR);
                        return true;
                    }
                    //////////
                    case R.id.show_underline: {
                        mNiceTabLayout.setShowUnderline(true);
                        return true;
                    }
                    case R.id.hide_underline: {
                        mNiceTabLayout.setShowUnderline(false);
                        return true;
                    }
                    case R.id.show_divider: {
                        mNiceTabLayout.setShowDivider(true);
                        return true;
                    }
                    case R.id.hide_divider: {
                        mNiceTabLayout.setShowDivider(false);
                        return true;
                    }
                    case R.id.show_indicator: {
                        mNiceTabLayout.setShowIndicator(true);
                        return true;
                    }
                    case R.id.hide_indicator: {
                        mNiceTabLayout.setShowIndicator(false);
                        return true;
                    }
                    //////////
                    case R.id.fit_content_width: {
                        mNiceTabLayout.setDistributeEvenly(false);
                        return true;
                    }
                    case R.id.distribute_evenly: {
                        mNiceTabLayout.setDistributeEvenly(true);
                        return true;
                    }
                    //////////
                    case R.id.left: {
                        mNiceTabLayout.setTabSelectedCenter(false);
                        return true;
                    }
                    case R.id.center: {
                        mNiceTabLayout.setTabSelectedCenter(true);
                        return true;
                    }
                    //////////
                    case R.id.test_badge: {
                        testBadge();
                        return true;
                    }
                    case R.id.clear_badge: {
                        clearBadge();
                        return true;
                    }
                    //////////
                    case R.id.ios: {
                        mNiceTabLayout.setBlurredView(mWrapFl, Color.WHITE); // white is window's background color
                        mIosStyleIcon = true;
                        mNiceTabLayout.setViewPager(mViewPager);
                        return true;
                    }
                    case R.id.android: {
                        mNiceTabLayout.setBlurredView(null, 0);
                        mIosStyleIcon = false;
                        mNiceTabLayout.setViewPager(mViewPager);
                        return true;
                    }
                }
                return false;
            }
        });
        */ //bottom menu
    }

    private void testBadge() {
        mNiceTabLayout.setBadge(0, "1");
        mNiceTabLayout.setBadge(1, "12");
        mNiceTabLayout.setBadge(2, "123");
        mNiceTabLayout.setBadge(3, "1234567");
        mNiceTabLayout.setBadgeSmall(4);

        Log.d("DemoFragment", mNiceTabLayout.getBadgeText(3));
    }

    private void clearBadge() {
        mNiceTabLayout.clearBadge();
    }

    //show title and icon bottoms

   /* 上方tab顏色選擇 tab顯示icon或Title或both menu拿掉
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.demo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_title_only: {
                mNiceTabLayout.setTabMode(NiceTabLayout.TabMode.TITLE_ONLY);
                return true;
            }
            case R.id.action_icon_only: {
                mNiceTabLayout.setTabMode(NiceTabLayout.TabMode.ICON_ONLY);
                return true;
            }
            case R.id.action_both: {
                mNiceTabLayout.setTabMode(NiceTabLayout.TabMode.BOTH);
                return true;
            }
            case R.id.action_none: {
                mNiceTabLayout.setTabColorBlendMode(NiceTabLayout.TabColorBlendMode.NONE);
                return true;
            }
            case R.id.action_default_selected: {
                mNiceTabLayout.setTabColorBlendMode(NiceTabLayout.TabColorBlendMode.DEFAULT_SELECTED);
                return true;
            }
            case R.id.action_next_selected: {
                mNiceTabLayout.setTabColorBlendMode(NiceTabLayout.TabColorBlendMode.NEXT_SELECTED);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onDetach() {
        super.onDetach();
        mNiceTabLayout.setOnIndicatorColorChangedListener(null);
    }

    class SampleFragmentPagerAdapter extends FragmentPagerAdapter implements NiceTabLayout.IconTabProvider {

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).getTitle();
        }

        @Override
        public int getPageIconResId(int position) {
            return mIosStyleIcon ? mTabs.get(position).getIosIconResId() : mTabs.get(position).getAndroidIconResId();
        }
    }

    public void invalidateBlur() {
        mNiceTabLayout.invalidateBlur();
    }
}