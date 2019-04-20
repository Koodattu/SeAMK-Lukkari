package com.seamk.mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.objects.Course;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class RestaurantDayAdapter extends RecyclerView.Adapter<RestaurantDayAdapter.ViewHolder> {

    private Context context;
    private List<Course> courses;

    public RestaurantDayAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titlefi.setText(courses.get(position).getTitlefi());
        holder.titleen.setText(courses.get(position).getTitleen());
        holder.category.setText(courses.get(position).getCategory());
        holder.price.setText(courses.get(position).getPrices());
        holder.properties.setText(courses.get(position).getProperties());

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView titlefi;
        public TextView titleen;
        public TextView category;
        public TextView price;
        public TextView properties;

        public ViewHolder(View itemView){
            super(itemView);
            titlefi = itemView.findViewById(R.id.titlefi);
            titleen = itemView.findViewById(R.id.titleen);
            category = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            properties = itemView.findViewById(R.id.properties);
        }
    }
}
