package com.seamk.mobile.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.ItemAddedEvent;
import com.seamk.mobile.objects.BasketRealization;
import com.seamk.mobile.objects.BasketStudentGroup;
import com.seamk.mobile.objects.BasketTeacher;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 25.3.2018.
 */

public class StudyBasketSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;
    private Context context;
    private static final int ITEM_TYPE_STUDENT_GROUP = 0;
    private static final int ITEM_TYPE_REALIZATION = 1;
    private static final int ITEM_TYPE_TEACHER = 3;


    public StudyBasketSearchAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_STUDENT_GROUP) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basket_search_parent, parent, false);

            return new StudyBasketSearchAdapter.StudentGroupViewHolder(itemView);
        } else if (viewType == ITEM_TYPE_REALIZATION) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basket_search_realization, parent, false);

            return new StudyBasketSearchAdapter.RealizationViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basket_search_parent, parent, false);

            return new StudyBasketSearchAdapter.TeacherViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof BasketStudentGroup) {
            return ITEM_TYPE_STUDENT_GROUP;
        } else if (items.get(position) instanceof BasketRealization) {
            return ITEM_TYPE_REALIZATION;
        } else {
            return ITEM_TYPE_TEACHER;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {

        final Object item = items.get(position);

        if (viewHolder instanceof StudyBasketSearchAdapter.StudentGroupViewHolder) {
            ((StudyBasketSearchAdapter.StudentGroupViewHolder) viewHolder).bind((BasketStudentGroup) item, context, this);
        } else if (viewHolder instanceof StudyBasketSearchAdapter.RealizationViewHolder) {
            ((StudyBasketSearchAdapter.RealizationViewHolder) viewHolder).bind((BasketRealization) item, context, this);
        } else if (viewHolder instanceof StudyBasketSearchAdapter.TeacherViewHolder) {
            ((StudyBasketSearchAdapter.TeacherViewHolder) viewHolder).bind((BasketTeacher) item, context, this);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RealizationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.subject) TextView name;
        @BindView(R.id.realization) TextView code;
        @BindView(R.id.button_hide_show) AppCompatButton button;

        RealizationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("RestrictedApi")
        public void bind(final BasketRealization basketRealization, Context context, final StudyBasketSearchAdapter studyBasketSearchAdapter) {
            name.setText(basketRealization.getRealizationNameFi());
            code.setText(basketRealization.getRealizationCode());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    basketRealization.setAdded(!basketRealization.isAdded());
                    EventBus.getDefault().post(new ItemAddedEvent(basketRealization));
                    studyBasketSearchAdapter.notifyDataSetChanged();
                }
            });

            if (basketRealization.isAdded()) {
                button.setText(context.getResources().getString(R.string.added));
                ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor("#1fb43a")});
                button.setSupportBackgroundTintList(csl);
                Drawable img = context.getResources().getDrawable(R.drawable.ic_added);
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            } else {
                button.setText(context.getResources().getString(R.string.add));
                ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor("#03ADFA")});
                button.setSupportBackgroundTintList(csl);
                Drawable img = context.getResources().getDrawable(R.drawable.ic_add);
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            }
        }
    }

    static class StudentGroupViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.student_group_code) TextView textView;
        @BindView(R.id.button_hide_show) AppCompatButton button;
        @BindView(R.id.expand_collapse) AppCompatImageView imageView;
        @BindView(R.id.expandable_layout) ExpandableLayout expandableLayout;
        @BindView(R.id.rv_student_group_realization) RecyclerView recyclerView;

        StudentGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("RestrictedApi")
        public void bind(final BasketStudentGroup studentGroupBasket, final Context context, final StudyBasketSearchAdapter studyBasketSearchAdapter) {

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            RealizationResultAdapter realizationResultAdapter = new RealizationResultAdapter(context, studentGroupBasket.getBasketRealizations());
            recyclerView.setAdapter(realizationResultAdapter);

            textView.setText(studentGroupBasket.getStudentGroupCode());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    studentGroupBasket.setAdded(!studentGroupBasket.isAdded());
                    EventBus.getDefault().post(new ItemAddedEvent(studentGroupBasket));
                    studyBasketSearchAdapter.notifyDataSetChanged();
                }
            });

            if (studentGroupBasket.isAdded()) {
                button.setText(context.getResources().getString(R.string.added));
                ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor("#1fb43a")});
                button.setSupportBackgroundTintList(csl);
                Drawable img = context.getResources().getDrawable(R.drawable.ic_added);
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            } else {
                button.setText(context.getResources().getString(R.string.add));
                ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor("#03ADFA")});
                button.setSupportBackgroundTintList(csl);
                Drawable img = context.getResources().getDrawable(R.drawable.ic_add);
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            }

            if (studentGroupBasket.getBasketRealizations().size() == 0) {
                Drawable img = context.getResources().getDrawable(R.drawable.ic_empty_line);
                imageView.setImageDrawable(img);
            } else {
                if (expandableLayout.isExpanded()) {
                    Drawable img = context.getResources().getDrawable(R.drawable.ic_collapse);
                    imageView.setImageDrawable(img);
                } else {
                    Drawable img = context.getResources().getDrawable(R.drawable.ic_expand);
                    imageView.setImageDrawable(img);
                }
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (studentGroupBasket.getBasketRealizations().size() != 0) {
                        expandableLayout.toggle();
                        if (expandableLayout.isExpanded()) {
                            Drawable img = context.getResources().getDrawable(R.drawable.ic_collapse);
                            imageView.setImageDrawable(img);
                        } else {
                            Drawable img = context.getResources().getDrawable(R.drawable.ic_expand);
                            imageView.setImageDrawable(img);
                        }
                        imageView.requestLayout();
                    }
                }
            });
        }
    }

    static class TeacherViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.student_group_code) TextView textView;
        @BindView(R.id.button_hide_show) AppCompatButton button;
        @BindView(R.id.expand_collapse) AppCompatImageView imageView;
        @BindView(R.id.expandable_layout) ExpandableLayout expandableLayout;
        @BindView(R.id.rv_student_group_realization) RecyclerView recyclerView;

        TeacherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("RestrictedApi")
        public void bind(final BasketTeacher basketTeacher, final Context context, final StudyBasketSearchAdapter studyBasketSearchAdapter) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            RealizationResultAdapter realizationResultAdapter = new RealizationResultAdapter(context, basketTeacher.getBasketRealizations());
            recyclerView.setAdapter(realizationResultAdapter);

            textView.setText(basketTeacher.getTeacherName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    basketTeacher.setAdded(!basketTeacher.isAdded());
                    EventBus.getDefault().post(new ItemAddedEvent(basketTeacher));
                    studyBasketSearchAdapter.notifyDataSetChanged();
                }
            });

            if (basketTeacher.isAdded()) {
                button.setText(context.getResources().getString(R.string.added));
                ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor("#1fb43a")});
                button.setSupportBackgroundTintList(csl);
                Drawable img = context.getResources().getDrawable(R.drawable.ic_added);
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            } else {
                button.setText(context.getResources().getString(R.string.add));
                ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor("#03ADFA")});
                button.setSupportBackgroundTintList(csl);
                Drawable img = context.getResources().getDrawable(R.drawable.ic_add);
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            }

            if (basketTeacher.getBasketRealizations().size() == 0) {
                Drawable img = context.getResources().getDrawable(R.drawable.ic_empty_line);
                imageView.setImageDrawable(img);
            } else {
                if (expandableLayout.isExpanded()) {
                    Drawable img = context.getResources().getDrawable(R.drawable.ic_collapse);
                    imageView.setImageDrawable(img);
                } else {
                    Drawable img = context.getResources().getDrawable(R.drawable.ic_expand);
                    imageView.setImageDrawable(img);
                }
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (basketTeacher.getBasketRealizations().size() != 0) {
                        expandableLayout.toggle();
                        if (expandableLayout.isExpanded()) {
                            Drawable img = context.getResources().getDrawable(R.drawable.ic_collapse);
                            imageView.setImageDrawable(img);
                        } else {
                            Drawable img = context.getResources().getDrawable(R.drawable.ic_expand);
                            imageView.setImageDrawable(img);
                        }
                        imageView.requestLayout();
                    }
                }
            });
        }
    }
}

