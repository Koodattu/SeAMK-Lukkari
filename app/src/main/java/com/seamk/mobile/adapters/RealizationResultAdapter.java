package com.seamk.mobile.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.ItemAddedEvent;
import com.seamk.mobile.objects.BasketRealization;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class RealizationResultAdapter extends RecyclerView.Adapter<RealizationResultAdapter.ViewHolder> {

    private Context context;
    private List<BasketRealization> basketRealizations;

    public RealizationResultAdapter(Context context, List<BasketRealization> basketRealizations) {
        this.context = context;
        this.basketRealizations = basketRealizations;
    }

    public List<BasketRealization> getBasketRealizations() {
        return basketRealizations;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basket_search_realization,parent,false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(basketRealizations.get(position).getRealizationNameFi());
        holder.code.setText(basketRealizations.get(position).getRealizationCode());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basketRealizations.get(position).setAdded(!basketRealizations.get(position).isAdded());
                EventBus.getDefault().post(new ItemAddedEvent(basketRealizations.get(position)));
                notifyDataSetChanged();
            }
        });

        if (basketRealizations.get(position).isAdded()){
            holder.button.setText(context.getResources().getString(R.string.added));
            ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor("#1fb43a")});
            holder.button.setSupportBackgroundTintList(csl);
            Drawable img = context.getResources().getDrawable(R.drawable.ic_added);
            holder.button.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        } else {
            holder.button.setText(context.getResources().getString(R.string.add));
            ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor("#03ADFA")});
            holder.button.setSupportBackgroundTintList(csl);
            Drawable img = context.getResources().getDrawable(R.drawable.ic_add);
            holder.button.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        }
    }

    @Override
    public int getItemCount() {
        return basketRealizations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.subject) TextView name;
        @BindView(R.id.realization) TextView code;
        @BindView(R.id.button_hide_show) AppCompatButton button;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
