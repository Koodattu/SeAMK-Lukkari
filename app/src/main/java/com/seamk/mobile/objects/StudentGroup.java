package com.seamk.mobile.objects;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

public class StudentGroup extends AbstractItem<StudentGroup, StudentGroup.ViewHolder> {

    private String StudentGroupCode;
    private int StudentGroupID;

    public StudentGroup(String studentGroupCode, int studentGroupID) {
        StudentGroupCode = studentGroupCode;
        StudentGroupID = studentGroupID;
    }

    public StudentGroup(String studentGroupCode) {
        StudentGroupCode = studentGroupCode;
    }

    public String getStudentGroupCode() {
        return StudentGroupCode;
    }

    public void setStudentGroupCode(String studentGroupCode) {
        StudentGroupCode = studentGroupCode;
    }

    public int getStudentGroupID() {
        return StudentGroupID;
    }

    public void setStudentGroupID(int studentGroupID) {
        StudentGroupID = studentGroupID;
    }

    @Override
    public int getType() {
        return R.id.fa_student_group_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_general_blue;
    }

    @Override
    public void bindView(@NonNull StudentGroup.ViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);
        Context context = holder.itemView.getContext();
        holder.studentGroupName.setText(StudentGroupCode);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);

        holder.studentGroupName.setText(null);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_card_general) TextView studentGroupName;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}