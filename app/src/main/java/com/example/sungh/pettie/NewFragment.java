/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.example.sungh.pettie;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewFragment extends Fragment implements ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{
    public static NewFragment newInstance() {
        NewFragment fragment = new NewFragment();
        return fragment;
    }

//    private android.support.design.widget.TabLayout mTabs;
//    private ViewPager mViewPager;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private Viewpager viewpager = new Viewpager();
    private ViewpagerOne viewpagerOne = new ViewpagerOne();
    private ViewpagerTwo viewpagerTwo = new ViewpagerTwo();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View pagerView =  inflater.inflate(R.layout.fragment_new, container, false);

        viewPager = (ViewPager) pagerView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) pagerView.findViewById(R.id.tabLayout);

        //註冊
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);


        //viewPager 加入 Fragment
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
               public Fragment getItem(int position) {
                    switch (position) {
                        case 0:
                            return viewpager;
                        case 1:
                            return viewpagerOne;
                        case 2:
                            return viewpagerTwo;
                    }
                return null;
            }
            @Override
            public int getCount() {
            return 3;
        }
    }
    );
        return pagerView;

}

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //TabLayout里的TabItem被选中的时候触发
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //viewPager滑动之后显示触发
        tabLayout.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}








