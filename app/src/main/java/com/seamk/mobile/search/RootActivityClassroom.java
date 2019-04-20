package com.seamk.mobile.search;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.seamk.mobile.objects.ListOfReservations;
import com.seamk.mobile.objects.ReservationOld;
import com.seamk.mobile.util.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

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

public class RootActivityClassroom extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String classroomName;
    private String classroomCode;
    ViewPagerAdapter adapter;
    boolean firstRun = true;
    String startDate;
    String endDate;
    String url;
    String credential = Credentials.basic(stringToString(this),"");
    int selectTab = 2;
    private List<ReservationOld> reservationOlds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        Bundle extras = getIntent().getExtras();
        classroomName = extras.getString("classroomName");
        classroomCode = extras.getString("classroomCode");
        setTitle(classroomName);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reservationOlds = new ArrayList<>();

        fetchReservations = new FetchReservations();
        fetchReservations.execute();
    }

    public void onRefreshAction(MenuItem mi) {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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
                handler.postDelayed(pdRunnable, 100);
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
                calendar.add(Calendar.WEEK_OF_YEAR, 6);
                endDate = format.format(calendar.getTime()) + endTime;

                String JSONPost = "{'startDate':'" + startDate + "'," + "'endDate':'" + endDate + "'," + "'room':[" + "'" + classroomCode + "'" + "]}";

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
                        progressDialog = new ProgressDialog(RootActivityClassroom.this);
                        progressDialog.setMessage("Lataa...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(true);
                        progressDialog.show();
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
                viewPager = findViewById(R.id.viewpager);
                setupViewPager(viewPager);

                tabLayout = findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);

                viewPager.setOffscreenPageLimit(30);

                adapter.notifyDataSetChanged();


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

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.WEEK_OF_YEAR, -2);

        adapter.addFrag(new FragmentClassroomTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), -2);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentClassroomTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), -1);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentClassroomTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 0);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentClassroomTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 1);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentClassroomTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 2);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentClassroomTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 3);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        adapter.addFrag(new FragmentClassroomTimetableWeekTab(), "Viikko " + c.get(Calendar.WEEK_OF_YEAR), 4);

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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

        public void addFrag(Fragment fragment, String title, int weekIndex) {
            Bundle args = new Bundle();
            args.putInt("weekIndex", weekIndex);
            args.putBoolean("errorOccurred", errorOccurred);
            ArrayList<ReservationOld> reservationOldArrayList = new ArrayList<>();
            reservationOldArrayList.addAll(reservationOlds);
            ListOfReservations listOfReservations = new ListOfReservations(reservationOldArrayList);
            args.putParcelable("reservationOlds", Parcels.wrap(listOfReservations));
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


