package com.seamk.mobile.adapters;

import android.content.Context;
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

public class NewSummaryCoursesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;
    private Context context;
    private static final int ITEM_TYPE_COURSE = 0;
    private static final int ITEM_TYPE_DATE = 1;

    public NewSummaryCoursesAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_COURSE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_course,parent,false);

            return new CourseViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_course_day,parent,false);

            return new DateViewHolder(itemView);
        }
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
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        Object item = items.get(position);

        if (viewHolder instanceof CourseViewHolder) {
            ((CourseViewHolder) viewHolder).bind((Course) item);
        } else if (viewHolder instanceof DateViewHolder) {
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
            datePvm = itemView.findViewById(R.id.tv_text);
        }

        public void bind(String date) {
            datePvm.setText(date);
        }
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{

        public TextView titlefi;
        public TextView category;

        public CourseViewHolder(View itemView){
            super(itemView);
            titlefi = itemView.findViewById(R.id.titlefi_course);
            category = itemView.findViewById(R.id.category_course);
        }
        public void bind(Course course) {

            titlefi.setText(course.getTitlefi());
            category.setText(course.getCategory());
        }
    }
}
