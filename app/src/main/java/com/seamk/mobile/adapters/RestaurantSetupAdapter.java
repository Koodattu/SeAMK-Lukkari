package com.seamk.mobile.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.RestaurantSetupChosenEvent;
import com.seamk.mobile.objects.RestaurantSetup;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class RestaurantSetupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RestaurantSetup> items;
    private Context context;


    public RestaurantSetupAdapter(List<RestaurantSetup> items, Context context) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurant_radio,parent,false);
            return new RestaurantSetupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        final RestaurantSetup item = items.get(position);

            ((RestaurantSetupViewHolder) viewHolder).bind(item);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new RestaurantSetupChosenEvent(items.get(position).getIDNumber()));
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).setSelected(false);
                    }
                    items.get(position).setSelected(true);
                    notifyDataSetChanged();
                }
            });
        }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class RestaurantSetupViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView location;
        public TextView general;
        public RadioButton radioButton;

        public RestaurantSetupViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurant_name);
            location = itemView.findViewById(R.id.restaurant_location);
            general = itemView.findViewById(R.id.restaurant_no_name);
            radioButton = itemView.findViewById(R.id.radioButtonRestaurant);
        }

        public void bind(RestaurantSetup restaurantSetup) {
            if (restaurantSetup.getLocation().equals("")){
                name.setVisibility(View.GONE);
                location.setVisibility(View.GONE);
                general.setText(restaurantSetup.getName());
            } else {
                name.setText(restaurantSetup.getName());
                location.setText(restaurantSetup.getLocation());
            }
            if (restaurantSetup.isSelected()) {
                radioButton.performClick();
            } else {
                radioButton.setChecked(false);
            }
        }
    }
}
