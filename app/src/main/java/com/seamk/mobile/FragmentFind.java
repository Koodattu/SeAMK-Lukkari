package com.seamk.mobile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.seamk.mobile.objects.Find;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 28.3.2018.
 */

public class FragmentFind extends UtilityFragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    ItemAdapter<IItem> itemAdapter;
    FastAdapter<IItem> fastAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, v);

        getActivity().setTitle(getString(R.string.find));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
        itemAdapter.clear();
        itemAdapter.add(createFinds());

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, @NonNull IAdapter<IItem> adapter, @NonNull IItem item, int position) {

                Fragment fragment;
                Bundle bundle = new Bundle();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

                switch (position){
                    case 0:
                        transaction.replace(R.id.frame, new FragmentFindEmptyRoom(), "FIND_EMPTY_ROOM_FRAGMENT");
                        break;
                    case 1:
                        fragment = new FragmentFindTimetable();
                        bundle.putInt("type", 0);
                        bundle.putBoolean("today", false);
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.frame, fragment, "FIND_TIMETABLE_STUDENT_GROUP_FRAGMENT");
                        break;
                    case 2:
                        fragment = new FragmentFindTimetable();
                        bundle.putInt("type", 1);
                        bundle.putBoolean("today", false);
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.frame, fragment, "FIND_TIMETABLE_TEACHER_FRAGMENT");
                        break;
                    case 3:
                        fragment = new FragmentFindTimetable();
                        bundle.putInt("type", 2);
                        bundle.putBoolean("today", false);
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.frame, fragment, "FIND_TIMETABLE_ROOM_FRAGMENT");
                        break;
                    case 4:
                        transaction.replace(R.id.frame, new FragmentFindRealization(), "FIND_REALIZATION_FRAGMENT");
                        break;
                }

                transaction.addToBackStack("TAG");
                transaction.commit();

                return true;
            }
        });

        return v;
    }

    private List<IItem> createFinds(){
        List<IItem> finds = new ArrayList<>();

        finds.add(new Find("Löydä tyhjä tila", "Etsi ja löydä tyhjä tila"));
        finds.add(new Find("Ryhmän lukujärjestys", "Tarkastele ryhmien lukujärjestyksiä"));
        finds.add(new Find("Opettajan lukujärjestys", "Tarkastele opettajien lukujärjestyksiä"));
        finds.add(new Find("Tilan lukujärjestys", "Tarkastele tilojen lukujärjestyksiä"));
        finds.add(new Find("Toteutuksen tiedot", "Hae toteutus nimellä tai tunnuksella"));

        return finds;
    }
}
