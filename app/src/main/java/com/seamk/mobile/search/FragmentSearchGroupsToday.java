package com.seamk.mobile.search;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.seamk.mobile.R;
import com.seamk.mobile.adapters.StudentGroupsAdapter;
import com.seamk.mobile.eventbusevents.StudentGroupEvent;
import com.seamk.mobile.objects.ReservationOld;
import com.seamk.mobile.objects.StudentGroup;
import com.seamk.mobile.util.Common;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.seamk.mobile.util.Common.stringToString;

/**
 * Created by Juha Ala-Rantala on 26.4.2017.
 */

public class FragmentSearchGroupsToday extends Fragment implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    LinearLayout linearLayoutEmptyView;
    LinearLayout linearLayoutProgressBar;
    StudentGroupsAdapter adapter;
    FetchReservations fetchReservations;
    List<StudentGroup> studentGroups;
    List<ReservationOld> reservationOlds;
    String studentGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups_today, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle(getResources().getString(R.string.today_at_school));
        recyclerView = v.findViewById(R.id.rv);
        linearLayoutEmptyView = v.findViewById(R.id.LLemptyView);
        linearLayoutProgressBar = v.findViewById(R.id.LLprogressBar);

        //recyclerView.setVisibility(View.INVISIBLE);
        linearLayoutEmptyView.setVisibility(View.INVISIBLE);
        linearLayoutProgressBar.setVisibility(View.VISIBLE);

        GridLayoutManager glm = new GridLayoutManager(getContext() ,3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        adapter = new StudentGroupsAdapter(studentGroups, getContext());
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentGroups = new ArrayList<>();
        reservationOlds = new ArrayList<>();
    }

    SearchView searchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_studentgroups, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                fetchReservations = new FetchReservations();
                fetchReservations.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onStudentGroupEvent(StudentGroupEvent event) {
        studentGroup = event.studentGroupCode;
        startTimetableActivity();
    }

    public void startTimetableActivity(){
        Intent intent = new Intent(getContext(), RootActivitySearchTimetable.class);
        intent.putExtra("studentGroupCode", studentGroup);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        fetchReservations = new FetchReservations();
        fetchReservations.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return true;
    }

    private class FetchReservations extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            MediaType JSONData = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();

            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            //calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String startTime = "T00:00:00";
            String endTime = "T23:59:59";

            String url = "https://opendata.seamk.fi:443/r1/reservationOld/search?l=fi";

            String startDate = format.format(calendar.getTime()) + startTime;
            String endDate = format.format(calendar.getTime()) + endTime;

            String JSONPost = "{'startDate':'" + startDate + "'," + "'endDate':'" + endDate + "'}";

            RequestBody body = RequestBody.create(JSONData, JSONPost);

            String credential = Credentials.basic(stringToString(getContext()),"");

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
            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reservationOlds.clear();
            studentGroups.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (reservationOlds.size() == 0){
                //recyclerView.setVisibility(View.INVISIBLE);
                linearLayoutEmptyView.setVisibility(View.VISIBLE);
                linearLayoutProgressBar.setVisibility(View.INVISIBLE);
            } else {
                //recyclerView.setVisibility(View.VISIBLE);
                linearLayoutEmptyView.setVisibility(View.INVISIBLE);
                linearLayoutProgressBar.setVisibility(View.INVISIBLE);

                List<String> tempStrings = new ArrayList<>();

                for (int i = 0; i < reservationOlds.size(); i++) {
                    if (!reservationOlds.get(i).getStudentGroupId().get(0).equals("")) {
                        if (!tempStrings.contains(reservationOlds.get(i).getStudentGroupCode().get(0))) {
                            tempStrings.add(reservationOlds.get(i).getStudentGroupCode().get(0));
                        }
                    }
                }

                for (int i = 0; i < tempStrings.size(); i++){
                    studentGroups.add(new StudentGroup(tempStrings.get(i), 0));
                }

                Collections.sort(studentGroups, new Comparator<StudentGroup>(){
                    public int compare(StudentGroup obj1, StudentGroup obj2) {
                        return obj1.getStudentGroupCode().compareToIgnoreCase(obj2.getStudentGroupCode());
                    }
                });

                adapter = new StudentGroupsAdapter(studentGroups, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
