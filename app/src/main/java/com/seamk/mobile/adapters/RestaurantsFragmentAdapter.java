package com.seamk.mobile.adapters;

import android.content.Context;
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

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class RestaurantsFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Restaurant> items;
    private Context context;


    public RestaurantsFragmentAdapter(List<Restaurant> items, Context context) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurant,parent,false);
        return new RestaurantViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        final Restaurant item = items.get(position);

        ((RestaurantViewHolder) viewHolder).bind(item);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new RestaurantChosenEvent(item.getIDNO()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{

        TextView restaurantName;
        TextView restaurantLocation;
        TextView restaurantOpeningHours;

        public RestaurantViewHolder(View itemView){
            super(itemView);

            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantLocation = itemView.findViewById(R.id.restaurant_location);
            restaurantOpeningHours = itemView.findViewById(R.id.restaurant_opening_hours);
        }
        public void bind(Restaurant restaurant) {

            restaurantName.setText(restaurant.name);
            restaurantLocation.setText(restaurant.location);
            restaurantOpeningHours.setText(restaurant.openingHours);
        }
    }
}
