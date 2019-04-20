package com.seamk.mobile.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.StudentGroupEvent;
import com.seamk.mobile.objects.StudentGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class StudentGroupsAdapter extends RecyclerView.Adapter<StudentGroupsAdapter.StudentGroupsViewHolder> {

    public static class StudentGroupsViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView studentGroupCode;

        StudentGroupsViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view_student_group);
            studentGroupCode = itemView.findViewById(R.id.card_student_group_code);
        }
    }

    public void filter(String text) {
        studentGroups.clear();
        if(text.isEmpty()){
            studentGroups.addAll(studentGroupsCopy);
        } else{
            text = text.toLowerCase();
            for(StudentGroup item: studentGroupsCopy){
                if(item.getStudentGroupCode().toLowerCase().contains(text)){
                    studentGroups.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    List<StudentGroup> studentGroups = new ArrayList<>();
    List<StudentGroup> studentGroupsCopy;
    Context context;
    public StudentGroupsAdapter(List<StudentGroup> studentGroups, Context context){
        this.studentGroupsCopy = studentGroups;
        this.studentGroups.addAll(this.studentGroupsCopy);
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public StudentGroupsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_student_group, viewGroup, false);
        StudentGroupsViewHolder rvh = new StudentGroupsViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(final StudentGroupsViewHolder studentGroupsViewHolder, int i) {
        studentGroupsViewHolder.studentGroupCode.setText(studentGroups.get(i).getStudentGroupCode());

        studentGroupsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StudentGroupEvent(studentGroups.get(studentGroupsViewHolder.getAdapterPosition()).getStudentGroupCode()));
            }
        }) ;
    }

    @Override
    public int getItemCount() {
        return studentGroups.size();
    }
}
