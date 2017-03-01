package com.patryk1007.viewheader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.patryk1007.zoomviewpager.ZoomHeaderViewPager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ZoomHeaderViewPager mZoomHeaderViewPager;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewPager();
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mZoomHeaderViewPager = (ZoomHeaderViewPager) findViewById(R.id.zoom_header_view_pager);
        mZoomHeaderViewPager.setTabOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tab_hall:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tab_private_letter:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tab_setting:
                mViewPager.setCurrentItem(0);
                break;
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return PageFragment.newInstance("Fragment1", "#FF0000");
                case 1:
                    return PageFragment.newInstance("Fragment2", "#00FF00");
                case 2:
                    return PageFragment.newInstance("Fragment3", "#0000FF");
                case 3:
                    return PageFragment.newInstance("Frag4", "#FFFFFF");
                case 4:
                    return PageFragment.newInstance("Frag5", "#FFFFFF");
                default:
                    return PageFragment.newInstance("Frag6", "#FFFFFF");
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "设置";
                case 1:
                    return "聊天室";
                case 2:
                    return "私信";
                default:
                    return "Title default";

            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
