package com.seamk.mobile.adapters;

import androidx.recyclerview.widget.RecyclerView;
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

public class RestaurantWeekAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;
    private static final int ITEM_TYPE_COURSE = 0;
    private static final int ITEM_TYPE_DATE = 1;

    public RestaurantWeekAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Course) {
            return ITEM_TYPE_COURSE;
        } else {
            return ITEM_TYPE_DATE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_COURSE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course,parent,false);

            return new CourseViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pvm,parent,false);

            return new DateViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Object item = items.get(position);

        if (viewHolder instanceof CourseViewHolder) {
            ((CourseViewHolder) viewHolder).bind((Course) item);
        } else {
            ((DateViewHolder) viewHolder).bind((String) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class DateViewHolder extends RecyclerView.ViewHolder{

        public TextView datePvm;

        public DateViewHolder(View itemView){
            super(itemView);
            datePvm = itemView.findViewById(R.id.card_pvm_textview);
        }

        public void bind(String date) {
            datePvm.setText(date);
        }
    }

    private static class CourseViewHolder extends RecyclerView.ViewHolder{

        public TextView titlefi;
        public TextView titleen;
        public TextView category;
        public TextView price;
        public TextView properties;

        public CourseViewHolder(View itemView){
            super(itemView);
            titlefi = itemView.findViewById(R.id.titlefi);
            titleen = itemView.findViewById(R.id.titleen);
            category = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            properties = itemView.findViewById(R.id.properties);
        }

        public void bind(Course course) {
            titlefi.setText(course.getTitlefi());
            titleen.setText(course.getTitleen());
            category.setText(course.getCategory());
            price.setText(course.getPrices());
            properties.setText(course.getProperties());
        }
    }
}
