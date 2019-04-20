package com.seamk.mobile.objects;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.seamk.mobile.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 11.3.2018.
 */

public class Teacher extends AbstractItem<Teacher, Teacher.ViewHolder> {

    private String name;

    public Teacher(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return R.id.fa_teacher_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_general_blue;
    }

    @Override
    public void bindView(@NonNull Teacher.ViewHolder viewHolder, @NonNull List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        Context context = viewHolder.itemView.getContext();
        viewHolder.teacherName.setText(name);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);

        holder.teacherName.setText(null);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_card_general) TextView teacherName;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}