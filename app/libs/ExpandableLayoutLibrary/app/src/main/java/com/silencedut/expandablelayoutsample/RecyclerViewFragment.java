package com.silencedut.expandablelayoutsample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SilenceDut on 16/6/7.
 */
public class RecyclerViewFragment extends Fragment {

    public static RecyclerViewFragment newInstance() {
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        return recyclerViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_recyclerview,container,false);
        RecyclerView summonerList;
        summonerList = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        summonerList.setLayoutManager(linearLayoutManager);
        SummonerAdapter summonerAdapter = new SummonerAdapter(getActivity());
        summonerList.setAdapter(summonerAdapter);
        return rootView;
    }
}
