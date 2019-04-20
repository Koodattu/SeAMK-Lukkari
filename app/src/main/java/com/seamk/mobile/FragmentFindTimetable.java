package com.seamk.mobile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.seamk.mobile.elasticsearch.Bucket;
import com.seamk.mobile.elasticsearch.BucketRooms;
import com.seamk.mobile.elasticsearch.ElasticRooms;
import com.seamk.mobile.elasticsearch.ElasticStudentGroups;
import com.seamk.mobile.elasticsearch.ElasticTeachers;
import com.seamk.mobile.interfaces.callback.ElasticRoomsCallback;
import com.seamk.mobile.interfaces.callback.ElasticStudentGroupsCallback;
import com.seamk.mobile.interfaces.callback.ElasticTeachersCallback;
import com.seamk.mobile.objects.Room;
import com.seamk.mobile.objects.StudentGroup;
import com.seamk.mobile.objects.Teacher;
import com.seamk.mobile.retrofit.RetroFitters;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentFindTimetable extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.retry) AppCompatButton button;
    @BindView(R.id.text_view) TextView textView;

    ItemAdapter<IItem> itemAdapter;
    FastAdapter<IItem> fastAdapter;

    int type = -1;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_with_progress, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);

        switch (type){
            case -1:
                getActivity().setTitle(getString(R.string.error_occurred));
                break;
            case 0:
                getActivity().setTitle(getString(R.string.student_group));
                break;
            case 1:
                getActivity().setTitle(getString(R.string.teacher));
                break;
            case 2:
                getActivity().setTitle(getString(R.string.rooms));
                break;
        }

        recyclerView.setItemAnimator(null);
        GridLayoutManager glm = new GridLayoutManager(getContext(), type == 1 ? 2 : 3);
        recyclerView.setLayoutManager(glm);
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, @NonNull IAdapter<IItem> adapter, @NonNull IItem item, int position) {
                Fragment fragment = new FragmentFindTimetableGrid();
                Bundle bundle = new Bundle();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

                if (item instanceof StudentGroup) {
                    bundle.putString("word", ((StudentGroup) item).getStudentGroupCode());
                } else if (item instanceof Teacher) {
                    bundle.putString("word", ((Teacher) item).getName());
                } else if (item instanceof Room) {
                    bundle.putString("word", ((Room) item).getRoomCode());
                }

                fragment.setArguments(bundle);
                transaction.replace(R.id.frame, fragment, "FIND_TIMETABLE_GRID");
                transaction.addToBackStack("TAG");
                transaction.commit();

                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (NullPointerException e ){
                    e.printStackTrace();
                }

                return true;
            }
        });

        itemAdapter.getItemFilter().withFilterPredicate(new IItemAdapter.Predicate<IItem>() {
            @Override
            public boolean filter(@NonNull IItem item, CharSequence constraint) {
                if (item instanceof StudentGroup) {
                    return ((StudentGroup)item).getStudentGroupCode().toUpperCase().contains(String.valueOf(constraint).toUpperCase());
                } else if (item instanceof Teacher) {
                    return ((Teacher)item).getName().toUpperCase().contains(String.valueOf(constraint).toUpperCase());
                } else if (item instanceof Room) {
                    return ((Room)item).getRoomName().toUpperCase().contains(String.valueOf(constraint).toUpperCase()) || ((Room)item).getParentBuilding().toUpperCase().contains(String.valueOf(constraint).toUpperCase()) || ((Room) item).getRoomDesc().toUpperCase().contains(String.valueOf(constraint).toUpperCase());
                } else {
                    return false;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startFetching();
                } catch (IOException | JSONException e) {
                    updateVisibilities(2, e.toString());
                }
            }
        });

        try {
            startFetching();
        } catch (IOException | JSONException e) {
            updateVisibilities(2, e.toString());
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", -1);
        }
    }

    @Override
    public void onPause() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e ){
            e.printStackTrace();
        }
        super.onPause();
    }

    void startFetching() throws IOException, JSONException {

        updateVisibilities(0, "");

        switch (type){
            case -1:
                progressBar.setVisibility(View.GONE);
                textView.setText(R.string.error_occurred_neg_one);
                break;
            case 0:
                RetroFitters.fetchElasticStudentGroups(getContext(), 90, new ElasticStudentGroupsCallback() {
                    @Override
                    public void onSuccess(ElasticStudentGroups elasticStudentGroups) {
                        finishedFetching(elasticStudentGroups);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        updateVisibilities(2, throwable.toString());
                    }
                });
                break;
            case 1:
                RetroFitters.fetchElasticTeachers(getContext(), 90, new ElasticTeachersCallback() {
                    @Override
                    public void onSuccess(ElasticTeachers elasticTeachers) {
                        finishedFetching(elasticTeachers);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        updateVisibilities(2, throwable.toString());
                    }
                });
                break;
            case 2:
                RetroFitters.fetchElasticRooms(getContext(), 90, new ElasticRoomsCallback() {
                    @Override
                    public void onSuccess(ElasticRooms elasticRooms) {
                        finishedFetching(elasticRooms);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        updateVisibilities(2, throwable.toString());
                    }
                });
                break;
        }
    }

    void finishedFetching(ElasticStudentGroups elasticStudentGroups) {
        if (elasticStudentGroups.getAggregations().getMostReservedStudentGroups().getBuckets().size() == 0){
            updateVisibilities(2, getString(R.string.error_server_didnt_return_groups));
        } else {
            List<IItem> itemList = new ArrayList<>();

            for (Bucket bucket : elasticStudentGroups.getAggregations().getMostReservedStudentGroups().getBuckets()) {
                itemList.add(new StudentGroup(bucket.getKey()));
            }

            Collections.sort(itemList, new Comparator<Object>() {
                public int compare(Object obj1, Object obj2) {
                    return ((StudentGroup) obj1).getStudentGroupCode().compareToIgnoreCase(((StudentGroup) obj2).getStudentGroupCode());
                }
            });

            itemAdapter.add(itemList);

            updateVisibilities(1, "");
        }
    }

    void finishedFetching(ElasticTeachers elasticTeachers) {
        if (elasticTeachers.getAggregations().getMostReservedTeachers().getBuckets().size() == 0){
            updateVisibilities(2, getString(R.string.error_server_didnt_return_teachers));
        } else {
            List<IItem> itemList = new ArrayList<>();

            for (Bucket bucket : elasticTeachers.getAggregations().getMostReservedTeachers().getBuckets()) {
                itemList.add(new Teacher(bucket.getKey()));
            }

            Collections.sort(itemList, new Comparator<Object>() {
                public int compare(Object obj1, Object obj2) {
                    return ((Teacher) obj1).getName().compareToIgnoreCase(((Teacher) obj2).getName());
                }
            });

            itemAdapter.add(itemList);

            updateVisibilities(1, "");
        }
    }

    void finishedFetching(ElasticRooms elasticRooms) {
        if (elasticRooms.getAggregations().getMostReservedRooms().getBuckets().size() == 0){
            updateVisibilities(2, getString(R.string.error_server_didnt_return_rooms));
        } else {
            List<IItem> itemList = new ArrayList<>();

            for (BucketRooms bucket : elasticRooms.getAggregations().getMostReservedRooms().getBuckets()) {
                String code = bucket.getKey();
                String name = "";
                String building = "";
                if (bucket.getMostReservedRoomsDescriptions().getBuckets().size() > 0) {
                    name = bucket.getMostReservedRoomsDescriptions().getBuckets().get(0).getKey();
                }
                if (bucket.getMostReservedBuildings().getBuckets().size() > 0) {
                    building = bucket.getMostReservedBuildings().getBuckets().get(0).getKey();
                }

                Room room = new Room(code, name, building);

                if (!room.getRoomName().isEmpty()) {
                    itemList.add(room);
                }
            }

            Collections.sort(itemList, new Comparator<Object>() {
                public int compare(Object obj1, Object obj2) {
                    return ((Room) obj1).getRoomCode().compareToIgnoreCase(((Room) obj2).getRoomCode());
                }
            });

            itemAdapter.add(itemList);

            updateVisibilities(1, "");
        }
    }

    void updateVisibilities(int state, String error) {
        switch (state){
            case 0: // aloitetaan haku
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                break;
            case 1: // haku onnistui
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                break;
            case 2: // haku epäonnistui, asetetaan syy
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                if (error.contains("SocketTimeoutException")) {
                    textView.setText(R.string.search_error_time_out);
                } else if (error.contains("UnknownHostException")) {
                    textView.setText(R.string.search_error_no_internet);
                } else {
                    textView.setText(getString(R.string.error_occurred) + error);
                }
                break;
        }
    }
}
