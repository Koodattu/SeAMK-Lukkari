package com.seamk.mobile.restaurant;

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
import com.seamk.mobile.adapters.RestaurantsFragmentAdapter;
import com.seamk.mobile.eventbusevents.RestaurantChosenEvent;
import com.seamk.mobile.objects.Restaurant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class RootFragmentRestaurants extends UtilityFragment {

    private List<Restaurant> restaurants;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_restaurants, container, false);

        getActivity().setTitle(getString(R.string.restaurants));

        recyclerView = v.findViewById(R.id.rv);
        setHasOptionsMenu(true);
        GridLayoutManager glm = new GridLayoutManager(v.getContext(),1);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        initializeData();
        RestaurantsFragmentAdapter adapter = new RestaurantsFragmentAdapter(restaurants, getContext());
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
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    Toast mToast;

    @Subscribe
    public void onSearchChosen(RestaurantChosenEvent event) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Bundle bundle = new Bundle();
        bundle.putInt("restaurantCode", event.restaurantCode);
        RootFragmentRestaurant rootFragmentRestaurant = new RootFragmentRestaurant();
        rootFragmentRestaurant.setArguments(bundle);
        transaction.replace(R.id.frame, rootFragmentRestaurant, "RESTAURANT_FRAGMENT");
        transaction.addToBackStack(null);
        transaction.commit();

        /*
        int type = event.restaurantCode;
        if (type == 873){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.frame, new RootFragmentRestaurant(), "RESTAURANT_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (type == 0){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.frame, new RootFragmentRestaurant(), "RESTAURANT_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (type == 1404){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.frame, new RootFragmentRestaurant(), "RESTAURANT_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (type == 1401){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.frame, new RootFragmentRestaurant(), "RESTAURANT_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (type == 874){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.frame, new RootFragmentRestaurant(), "RESTAURANT_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getContext(), "Tätä hakua ei ole vielä implementoitu", Toast.LENGTH_SHORT);
            mToast.show();
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    private void initializeData(){
        restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("SeAMK Frami Sodexo", "Frami F", "Ma-Pe 10:30-14:00 | La 11:00-12:30", 873));
        restaurants.add(new Restaurant("Seinäjoki Areena (B-halli)", "Eventti", "Ma-Pe 10:00-14:00", 69));
        restaurants.add(new Restaurant("Sedu Ilmajoentie Sodexo", "Ilmajoentie", "Ma-Pe 10:30-12:30", 1401));
        restaurants.add(new Restaurant("Sedu Koskenalantie Sodexo", "Koskenalantie", "Ma-Pe 10:30-13:00", 1404));
        restaurants.add(new Restaurant("SeAMK Kampustalo Sodexo", "Kampustalo", "Ma-Pe 10:30-13:00", 874));
    }
}