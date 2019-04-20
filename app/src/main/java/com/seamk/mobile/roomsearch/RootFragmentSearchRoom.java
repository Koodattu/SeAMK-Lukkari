package com.seamk.mobile.roomsearch;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seamk.mobile.R;

import java.util.ArrayList;
import java.util.List;

public class RootFragmentSearchRoom extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search_room_tabs, container, false);

        getActivity().setTitle(getResources().getString(R.string.find_empty_room));

        setHasOptionsMenu(true);
        viewPager = v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);
        tabLayout = v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    ViewPagerAdapter adapter;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.addFragment(new FragmentSearchInstant(), "Pika");
        adapter.addFragment(new FragmentSearchBasic(), "Perus");
        adapter.addFragment(new FragmentSearchAdvanced(), "Laaja");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
