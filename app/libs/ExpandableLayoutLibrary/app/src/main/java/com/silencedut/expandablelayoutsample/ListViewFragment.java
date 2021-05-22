package com.silencedut.expandablelayoutsample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by SilenceDut on 16/6/7.
 */
public class ListViewFragment extends Fragment {
    public static ListViewFragment newInstance() {
        ListViewFragment listViewFragment = new ListViewFragment();
        return listViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_listview,container,false);
        ListView summonerList;
        summonerList = (ListView)rootView.findViewById(R.id.listView);
        summonerList.setAdapter(new ListViewAdapter(getActivity()));
        return rootView;
    }
}
