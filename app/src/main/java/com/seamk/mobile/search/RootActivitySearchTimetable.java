package com.seamk.mobile.search;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.MessageEvent;
import com.seamk.mobile.objects.ReservationOld;
import com.seamk.mobile.util.Common;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.seamk.mobile.util.Common.stringToString;

public class RootActivitySearchTimetable extends AppCompatActivity {

    TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar toolbar;

    String studentGroup;
    String startDate;
    String endDate;
    String url;
    String credential = Credentials.basic(stringToString(this),"");
    int selectTab = 2;

    private List<ReservationOld> reservationOlds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_timetable);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        studentGroup = getIntent().getStringExtra("studentGroupCode");
        reservationOlds = new ArrayList<>();
        setTitle(getResources().getString(R.string.week) + ", " + studentGroup);

        fetchReservations = new FetchReservations();
        fetchReservations.execute();
        //setContentView(R.layout.activity_sodexo);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(30);

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_search_timetable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (canRefresh){
                    viewPager.invalidate();
                    canRefresh = false;
                    fetchReservations = new FetchReservations();
                    fetchReservations.execute();

                    Handler h = new Handler();
                    h.postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            canRefresh = true;
                        }
                    },2500);
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ViewPagerAdapter adapter;
    boolean canRefresh = true;
    boolean firstRun = true;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.WEEK_OF_YEAR, -2);
/*
        adapter.addFrag(new FragmentGroupTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), -2);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentGroupTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), -1);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentGroupTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 0);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentGroupTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 1);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentGroupTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 2);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentGroupTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 3);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentGroupTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 4);
*/
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetchReservations != null){
            fetchReservations.cancel(true);
        }
    }

    final Handler handler = new Handler();
    boolean errorOccurred;

    private FetchReservations fetchReservations;

    private class FetchReservations extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {
            errorOccurred = false;
            handler.postDelayed(pdRunnable, 500);
            MediaType JSONData = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();

            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String startTime = "T00:00:00";
            String endTime = "T23:59:59";

            url = "https://opendata.seamk.fi:443/r1/reservationOld/search?l=fi";
            calendar.add(Calendar.WEEK_OF_YEAR, -2);
            startDate = format.format(calendar.getTime()) + startTime;
            calendar.add(Calendar.WEEK_OF_YEAR, 7);
            endDate = format.format(calendar.getTime()) + endTime;

            String JSONPost = "{'startDate':'" + startDate + "'," + "'endDate':'" + endDate + "'," + "'studentGroup':[" + "'" + studentGroup + "'" + "]}";

            RequestBody body = RequestBody.create(JSONData, JSONPost);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Authorization", credential)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray JSONReservations = jsonObject.getJSONArray("reservationOlds");

                reservationOlds = Common.reservationsFromJson(jsonObject);

            } catch (IOException e){
                e.printStackTrace();
                errorOccurred = true;
            } catch (JSONException e){
                System.out.println("End of content");
            }

            return null;
        }

        final Runnable pdRunnable = new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(RootActivitySearchTimetable.this);
                progressDialog.setMessage("Lataa...");
                progressDialog.setIndeterminate(true);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(true);
                if (!isFinishing()){
                    progressDialog.show();
                }

            }
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reservationOlds = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(Void aVoid){
            handler.removeCallbacks(pdRunnable);

            if (progressDialog != null)
            {
                progressDialog.dismiss();
            }

            EventBus.getDefault().post(new MessageEvent(reservationOlds));

            if (adapter != null){
                adapter.notifyDataSetChanged();
            }

            if (firstRun){
                viewPager.setCurrentItem(selectTab, true);
                tabLayout.getTabAt(selectTab).select();
                tabLayout.setScrollPosition(selectTab, 0f, false);
                firstRun = false;
            }
            else {
                Handler h = new Handler();
                h.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        tabLayout.setScrollPosition(viewPager.getCurrentItem(), 0f, false);
                    }
                },100);
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public int getItemPosition(Object object) {
            EventBus.getDefault().post(new MessageEvent(reservationOlds));
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title, int weekIndex) {
            Bundle args = new Bundle();
            args.putInt("weekIndex", weekIndex);
            //args.putParcelableArrayList("reservationOlds", reservationOlds);
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
