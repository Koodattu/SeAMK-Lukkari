package com.seamk.mobile.adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.RestaurantChosenEvent;
import com.seamk.mobile.objects.Restaurant;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ChooseRestaurantAdapter extends RecyclerView.Adapter<ChooseRestaurantAdapter.RestaurantViewHolder> {

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView restaurantName;
        TextView restaurantLocation;
        TextView restaurantOpeningHours;

        RestaurantViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantLocation = itemView.findViewById(R.id.restaurant_location);
            restaurantOpeningHours = itemView.findViewById(R.id.restaurant_opening_hours);
        }
    }

    List<Restaurant> restaurants;
    Context context;
    public ChooseRestaurantAdapter(List<Restaurant> restaurants, Context context){
        this.restaurants = restaurants;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_restaurant, viewGroup, false);
        RestaurantViewHolder rvh = new RestaurantViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(final RestaurantViewHolder restaurantViewHolder, int i) {
        restaurantViewHolder.restaurantName.setText(restaurants.get(i).name);
        restaurantViewHolder.restaurantLocation.setText(restaurants.get(i).location);
        restaurantViewHolder.restaurantOpeningHours.setText(restaurants.get(i).openingHours);

        restaurantViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new RestaurantChosenEvent(restaurants.get(restaurantViewHolder.getAdapterPosition()).getIDNO()));
            }
        }) ;
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
