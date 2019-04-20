package com.seamk.mobile.restaurant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.seamk.mobile.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RootFragmentRestaurant extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    int locationIndex = 1337;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_lukkari_week, container, false);
        setHasOptionsMenu(true);
        Bundle extras = getArguments();

        if (extras != null && extras.containsKey("restaurantCode")){
            locationIndex = extras.getInt("restaurantCode");
        } else {
            SharedPreferences preferences = getContext().getSharedPreferences("ApplicationPreferences", 0);
            locationIndex = Integer.valueOf(preferences.getString("restaurantCode", "0"));
        }

        switch(locationIndex){
            case 873:
                getActivity().setTitle("Frami F");
                break;

            case 69:
                getActivity().setTitle("Eventti");
                break;

            case 874:
                getActivity().setTitle("Kampustalo");
                break;

            case 1404:
                getActivity().setTitle("Koskenalantie");
                break;

            case 1401:
                getActivity().setTitle("Ilmajoentie");
                break;
        }

        viewPager = v.findViewById(R.id.week_viewpager);
        viewPager.setOffscreenPageLimit(10);
        setupViewPager(viewPager);

        tabLayout = v.findViewById(R.id.week_tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                {
                    viewPager.setCurrentItem(7);
                }
                else{
                    viewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
                }
            }
        }, 100);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refreshFragment:
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_sodexo, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        DateFormat format = new SimpleDateFormat("dd.MM.");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        String[] days = new String[7];
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Calendar c = Calendar.getInstance();
        adapter.addFrag(new FragmentRestaurantWeekTab(), getString(R.string.week) + " " + c.get(Calendar.WEEK_OF_YEAR), 0, locationIndex);

        adapter.addFrag(new FragmentRestaurantDayTab(), getString(R.string.monday) + " " + days[0], 0, locationIndex);
        adapter.addFrag(new FragmentRestaurantDayTab(), getString(R.string.tuesday) + " " + days[1], 1, locationIndex);
        adapter.addFrag(new FragmentRestaurantDayTab(), getString(R.string.wednesday) + " " + days[2], 2, locationIndex);
        adapter.addFrag(new FragmentRestaurantDayTab(), getString(R.string.thursday) + " " + days[3], 3, locationIndex);
        adapter.addFrag(new FragmentRestaurantDayTab(), getString(R.string.friday) + " " + days[4], 4, locationIndex);
        if (locationIndex != 69){
            c.add(Calendar.WEEK_OF_YEAR, 1);
            adapter.addFrag(new FragmentRestaurantWeekTab(), getString(R.string.week) + " " + c.get(Calendar.WEEK_OF_YEAR), 1, locationIndex);
        }
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

        public void addFrag(Fragment fragment, String title, int dayIndex, int placeIndex) {
            Bundle args = new Bundle();
            args.putInt("dayIndex", dayIndex);
            args.putInt("placeIndex", placeIndex);
            fragment.setArguments(args);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
