package com.seamk.mobile.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.DeleteEvent;
import com.seamk.mobile.objects.BasketRealization;
import com.seamk.mobile.objects.BasketStudentGroup;
import com.seamk.mobile.objects.BasketTeacher;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class StudyBasketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;
    private Context context;
    private static final int ITEM_TYPE_STUDENT_GROUP = 0;
    private static final int ITEM_TYPE_REALIZATION = 1;
    private static final int ITEM_TYPE_TEACHER = 3;

    public StudyBasketAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_STUDENT_GROUP) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basket_parent, parent,false);

            return new StudentGroupViewHolder(itemView);
        } else if (viewType == ITEM_TYPE_REALIZATION) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basket_realization, parent,false);

            return new RealizationViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basket_parent, parent,false);

            return new TeacherViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof BasketStudentGroup) {
            return ITEM_TYPE_STUDENT_GROUP;
        } else if (items.get(position) instanceof BasketRealization){
            return ITEM_TYPE_REALIZATION;
        } else {
            return ITEM_TYPE_TEACHER;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {

        final Object item = items.get(position);

        if (viewHolder instanceof StudentGroupViewHolder) {
            ((StudentGroupViewHolder) viewHolder).bind((BasketStudentGroup)item, context, this);
            ((StudentGroupViewHolder) viewHolder).imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new DeleteEvent(item));
                    lastDeletedItem(viewHolder.getAdapterPosition());
                }
            });
        } else if (viewHolder instanceof RealizationViewHolder) {
            ((RealizationViewHolder) viewHolder).bind((BasketRealization) item, this, context);
            ((RealizationViewHolder) viewHolder).imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new DeleteEvent(item));
                    lastDeletedItem(viewHolder.getAdapterPosition());
                }
            });
        } else if (viewHolder instanceof TeacherViewHolder) {
            ((TeacherViewHolder) viewHolder).bind((BasketTeacher) item, context, this);
            ((TeacherViewHolder) viewHolder).imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new DeleteEvent(item));
                    lastDeletedItem(viewHolder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int lastDeletedPosition;
    private Object lastDeletedObject;

    private void lastDeletedItem(int position){
        lastDeletedObject = items.get(position);
        lastDeletedPosition = position;
        items.remove(lastDeletedObject);
        notifyDataSetChanged();
    }

    public void undoLastRemove(){
        items.add(lastDeletedPosition, lastDeletedObject);
        notifyDataSetChanged();
    }

    public List<Object> getModifiedList(){
        return items;
    }

    static class RealizationViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.card_view) CardView cardView;
        @BindView(R.id.subject) TextView name;
        @BindView(R.id.realization) TextView code;
        @BindView(R.id.button_hide_show) Button button;
        @BindView(R.id.button_delete) ImageButton imageButton;
        @BindView(R.id.realization_duplicate_warning) TextViewRichDrawable TVWarning;
        @BindView(R.id.realization_no_reservations_info) TextViewRichDrawable TVInfo;

        RealizationViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final BasketRealization basketRealization, final StudyBasketAdapter studyBasketAdapter, Context context) {
            name.setText(basketRealization.getRealizationNameFi());
            code.setText(basketRealization.getRealizationCode());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    basketRealization.setShown(!basketRealization.isShown());
                    Calendar c = Calendar.getInstance();
                    basketRealization.setLastSavedTime(c.getTimeInMillis());
                    studyBasketAdapter.notifyDataSetChanged();
                }
            });

            if (basketRealization.isShown()){
                button.setText(context.getResources().getString(R.string.hide));
                cardView.setAlpha(1f);
            } else {
                button.setText(context.getResources().getString(R.string.show));
                cardView.setAlpha(0.5f);
            }

            TVWarning.setVisibility(basketRealization.isDuplicateWarning() ? View.VISIBLE : View.GONE);
            TVInfo.setVisibility(basketRealization.isNoReservations() ? View.VISIBLE : View.GONE);
        }
    }

    static class StudentGroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view) CardView cardView;
        @BindView(R.id.student_group_code) TextView textView;
        @BindView(R.id.button_hide_show) AppCompatButton button;
        @BindView(R.id.expand_collapse) AppCompatImageView imageView;
        @BindView(R.id.expandable_layout) ExpandableLayout expandableLayout;
        @BindView(R.id.rv_student_group_realization) RecyclerView recyclerView;
        @BindView(R.id.button_delete) AppCompatImageButton imageButton;


        StudentGroupViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(final BasketStudentGroup studentGroupBasket, final Context context, final StudyBasketAdapter studyBasketAdapter) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);

            StudentGroupRealizationBasketAdapter studentGroupRealizationBasketAdapter = new StudentGroupRealizationBasketAdapter(context, studentGroupBasket.getBasketRealizations());
            recyclerView.setAdapter(studentGroupRealizationBasketAdapter);

            textView.setText(studentGroupBasket.getStudentGroupCode());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (studentGroupBasket.isShown()){
                        studentGroupBasket.setShown(false);
                    } else {
                        studentGroupBasket.setShown(true);
                    }
                    studyBasketAdapter.notifyDataSetChanged();
                }
            });

            if (studentGroupBasket.isShown()){
                button.setText(context.getResources().getString(R.string.hide));
                cardView.setAlpha(1f);
                expandableLayout.setAlpha(1f);
            } else {
                button.setText(context.getResources().getString(R.string.show));
                cardView.setAlpha(0.5f);
                expandableLayout.setAlpha(0.5f);
            }

            if (studentGroupBasket.getBasketRealizations().size() == 0){
                Drawable img = context.getResources().getDrawable(R.drawable.ic_empty_line);
                imageView.setImageDrawable(img);
            } else {
                if (expandableLayout.isExpanded()){
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
                    if (studentGroupBasket.getBasketRealizations().size() != 0){
                        expandableLayout.toggle();
                        if (expandableLayout.isExpanded()){
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

        @BindView(R.id.card_view) CardView cardView;
        @BindView(R.id.student_group_code) TextView textView;
        @BindView(R.id.button_hide_show) AppCompatButton button;
        @BindView(R.id.expand_collapse) AppCompatImageView imageView;
        @BindView(R.id.expandable_layout) ExpandableLayout expandableLayout;
        @BindView(R.id.rv_student_group_realization) RecyclerView recyclerView;
        @BindView(R.id.button_delete) AppCompatImageButton imageButton;

        TeacherViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(final BasketTeacher basketTeacher, final Context context, final StudyBasketAdapter studyBasketAdapter) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            StudentGroupRealizationBasketAdapter studentGroupRealizationBasketAdapter = new StudentGroupRealizationBasketAdapter(context, basketTeacher.getBasketRealizations());
            recyclerView.setAdapter(studentGroupRealizationBasketAdapter);

            textView.setText(basketTeacher.getTeacherName());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    basketTeacher.setShown(!basketTeacher.isShown());
                    studyBasketAdapter.notifyDataSetChanged();
                }
            });

            if (basketTeacher.isShown()){
                button.setText(context.getResources().getString(R.string.hide));
                cardView.setAlpha(1f);
                expandableLayout.setAlpha(1f);
            } else {
                button.setText(context.getResources().getString(R.string.show));
                cardView.setAlpha(0.5f);
                expandableLayout.setAlpha(0.5f);
            }

            if (basketTeacher.getBasketRealizations().size() == 0){
                Drawable img = context.getResources().getDrawable(R.drawable.ic_empty_line);
                imageView.setImageDrawable(img);
            } else {
                if (expandableLayout.isExpanded()){
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
                    if (basketTeacher.getBasketRealizations().size() != 0){
                        expandableLayout.toggle();
                        if (expandableLayout.isExpanded()){
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
