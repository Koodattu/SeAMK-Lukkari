package com.seamk.mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.objects.Classroom;
import com.seamk.mobile.search.RootActivityClassroom;

import java.util.ArrayList;
import java.util.List;

public class ClassroomsAdapter extends RecyclerView.Adapter<ClassroomsAdapter.ClassroomViewHolder> {

    public static class ClassroomViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView classroomName;
        TextView classroomDesc;

        ClassroomViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view_classroom);
            classroomName = itemView.findViewById(R.id.card_classroom_name);
            classroomDesc = itemView.findViewById(R.id.card_classroom_desc);
        }
    }

    public void filter(String text) {
        classrooms.clear();
        if(text.isEmpty()){
            classrooms.addAll(classroomsCopy);
        } else{
            text = text.toLowerCase();
            for(Classroom item: classroomsCopy){
                if(item.getClassRoomFullName().toLowerCase().contains(text)){
                    classrooms.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    List<Classroom> classrooms = new ArrayList<>();
    List<Classroom> classroomsCopy;
    Context context;
    public ClassroomsAdapter(List<Classroom> classrooms, Context context){
        this.classroomsCopy = classrooms;
        this.classrooms.addAll(this.classroomsCopy);
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ClassroomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_classroom, viewGroup, false);
        ClassroomViewHolder rvh = new ClassroomViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(final ClassroomViewHolder classroomViewHolder, int i) {
        classroomViewHolder.classroomName.setText(classrooms.get(i).getClassRoomName());
        classroomViewHolder.classroomDesc.setText(classrooms.get(i).getClassRoomDescription());

        classroomViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(context, RootActivityClassroom.class);
                intent.putExtra("classroomName", classrooms.get(classroomViewHolder.getAdapterPosition()).getClassRoomName());
                intent.putExtra("classroomCode", classrooms.get(classroomViewHolder.getAdapterPosition()).getClassRoomCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classrooms.size();
    }
}
