package com.seamk.mobile.search;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seamk.mobile.R;
import com.seamk.mobile.UtilityFragment;
import com.seamk.mobile.adapters.SearchOptionsAdapter;
import com.seamk.mobile.eventbusevents.SearchChosenEvent;
import com.seamk.mobile.objects.SearchOption;
import com.seamk.mobile.roomsearch.RootFragmentSearchRoom;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juha Ala-Rantala on 31.8.2017.
 */

public class RootFragmentSearch extends UtilityFragment {

    RecyclerView recyclerView;
    SearchOptionsAdapter searchOptionsAdapter;
    List<SearchOption> searchOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_restaurants, container, false);

        recyclerView = v.findViewById(R.id.rv);
        setHasOptionsMenu(true);
        GridLayoutManager glm = new GridLayoutManager(v.getContext(),1);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        searchOptionsAdapter = new SearchOptionsAdapter(searchOptions, getContext());
        recyclerView.setAdapter(searchOptionsAdapter);
        return v;
    }

    Toast mToast;

    @Subscribe
    public void onSearchChosen(SearchChosenEvent event) {
        int type = event.getSearchType();
        if (type == 1){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.frame, new RootFragmentSearchRoom(), "SEARCH_EMPTY_ROOM");
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (type == 2){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.frame, new RootFragmentSearchStudentGroup(), "SEARCH_STUDENT_GROUP");
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (type == 3){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.frame, new RootFragmentClassrooms(), "SEARCH_ROOM_TIMETABLE");
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (type == 4){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.frame, new FragmentSearchGroupsToday(), "SEARCH_GROUPS_TODAY");
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getContext(), "Tätä hakua ei ole vielä implementoitu", Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume(){
        getActivity().setTitle(getResources().getString(R.string.search));
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchOptions = new ArrayList<>();
        searchOptions.add(new SearchOption("Etsi tyhjä tila", "Tyhjän tilan pika-, perus- ja laajahaku", 1));
        searchOptions.add(new SearchOption("Hae tilan lukujärjestys", "Hae lukujärjestys tilalle", 3));
        searchOptions.add(new SearchOption("Hae ryhmän lukujärjestys", "Hae lukujärjestys ryhmälle", 2));
        searchOptions.add(new SearchOption("Hae toteutus", "Hae toteutus tunnuksella", 0));
        searchOptions.add(new SearchOption("Tänään paikalla", "Hae tänään koulussa olevat ryhmät", 4));
        //searchOptions.add(new SearchOption("Hae opintojakso", "Hae opintojakso tunnuksella", 0));
        //searchOptions.add(new SearchOption("Hae opintojaksototeutus", "Hae opintojaksototeutus tunnuksella", 0));
        //searchOptions.add(new SearchOption("Hae opetussuunnitelma", "Hae opetussuunnitelma tunnuksella", 0));
        //searchOptions.add(new SearchOption("Työnhaku", "Tämä haku on suunnattu oskari lopille", 2));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}