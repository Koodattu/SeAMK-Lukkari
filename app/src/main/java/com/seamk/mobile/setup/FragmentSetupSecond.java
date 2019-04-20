package com.seamk.mobile.setup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seamk.mobile.R;
import com.seamk.mobile.adapters.RestaurantSetupAdapter;
import com.seamk.mobile.eventbusevents.NextSlideEvent;
import com.seamk.mobile.eventbusevents.RestaurantSetupChosenEvent;
import com.seamk.mobile.objects.RestaurantSetup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juha Ala-Rantala on 18.9.2017.
 */

// valitse ruokalista tai älä valitse

public class FragmentSetupSecond extends Fragment {

    RecyclerView recyclerView;
    RestaurantSetupAdapter adapter;
    List<RestaurantSetup> restaurantSetups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setup_second, container, false);

        restaurantSetups = new ArrayList<>();

        restaurantSetups.add(new RestaurantSetup(getString(R.string.dont_choose_restaurant), "", "0"));
        restaurantSetups.add(new RestaurantSetup("Frami F", "Sodexo", "873"));
        restaurantSetups.add(new RestaurantSetup("Eventti", "Seinäjoki Areena", "69"));
        restaurantSetups.add(new RestaurantSetup("Ilmajoentie", "Sodexo", "1404"));
        restaurantSetups.add(new RestaurantSetup("Koskenalantie", "Sodexo", "1401"));
        restaurantSetups.add(new RestaurantSetup("Kampustalo", "Sodexo", "874"));

        recyclerView = v.findViewById(R.id.rv);

        GridLayoutManager glm = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        adapter = new RestaurantSetupAdapter(restaurantSetups, getContext());
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onRestaurantSetupChosen(RestaurantSetupChosenEvent event) {
        SharedPreferences preferences = getActivity().getSharedPreferences("ApplicationPreferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("restaurantCode", event.restaurantCode);
        editor.apply();
        EventBus.getDefault().post(new NextSlideEvent());
    }
}