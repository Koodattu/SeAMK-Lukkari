package com.seamk.mobile.objects;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.seamk.mobile.R;
import com.seamk.mobile.elasticsearch.StudentGroup;
import com.seamk.mobile.elasticsearch.Teacher;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 24.3.2018.
 */

public class Realization extends AbstractItem<Realization, Realization.ViewHolder> {

    String idno;
    String name;
    String code;
    String teacher;
    String groups;
    String time;

    public Realization(String idno, String name, String code, List<Teacher> teachers, List<StudentGroup> studentGroups, String start, String end) {
        this.idno = idno;
        this.name = name;
        this.code = code;

        String string = "";
        List<String> list = new ArrayList<>();
        if (teachers != null) {
            for (Teacher teacher : teachers){
                list.add(teacher.getName());
            }
            string = listToShortString(list);
        }
        this.teacher = string;

        string = "";
        if (studentGroups != null) {
            list = new ArrayList<>();
            for (StudentGroup studentGroup : studentGroups) {
                list.add(studentGroup.getCode());
            }
            string = listToShortString(list);
        }
        this.groups = string;

        this.time = start.substring(0, start.indexOf("T")) + " — " + end.substring(0, end.indexOf("T"));
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String listToLongString(List<String> stringList) {
        String tmpString = "";
        if (stringList.size() > 0) {
            for (String s : stringList) {
                tmpString = tmpString + ", " + s;
            }
            tmpString = tmpString.substring(2);
        }
        return tmpString;
    }

    private String listToShortString(List<String> stringList) {
        String tmpString = "";
        if (stringList.size() > 0) {
            if (stringList.size() > 2) {
                tmpString = stringList.get(0) + " ja " + (stringList.size() - 1) + " muuta";
            } else if (stringList.size() == 1) {
                tmpString = stringList.get(0);
            } else if (stringList.size() == 2){
                tmpString = stringList.get(0) + " ja " + stringList.get(1);
            }
        }
        return tmpString;
    }

    @Override
    public int getType() {
        return R.id.fa_search_realization_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_search_realization;
    }

    @Override
    public void bindView(@NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);
        holder.name.setText(name);
        holder.code.setText(code);
        holder.group.setText(groups);
        holder.teacher.setText(teacher);
        holder.time.setText(time);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
        holder.code.setText(null);
        holder.group.setText(null);
        holder.teacher.setText(null);
        holder.time.setText(null);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.code) TextViewRichDrawable code;
        @BindView(R.id.group) TextViewRichDrawable group;
        @BindView(R.id.teacher) TextViewRichDrawable teacher;
        @BindView(R.id.time) TextViewRichDrawable time;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
