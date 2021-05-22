package com.seamk.mobile.adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.objects.BasketRealization;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class StudentGroupRealizationBasketAdapter extends RecyclerView.Adapter<StudentGroupRealizationBasketAdapter.ViewHolder> {

    private Context context;
    private List<BasketRealization> basketRealizations;

    public StudentGroupRealizationBasketAdapter(Context context, List<BasketRealization> basketRealizations) {
        this.context = context;
        this.basketRealizations = basketRealizations;
    }

    public List<BasketRealization> getBasketRealizations() {
        return basketRealizations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basket_realization_child,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(basketRealizations.get(position).getRealizationNameFi());
        holder.code.setText(basketRealizations.get(position).getRealizationCode());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (basketRealizations.get(position).isShown()){
                    basketRealizations.get(position).setShown(false);
                } else {
                    basketRealizations.get(position).setShown(true);
                }
                Calendar c = Calendar.getInstance();
                basketRealizations.get(position).setLastSavedTime(c.getTimeInMillis());
                notifyDataSetChanged();
            }
        });

        if (basketRealizations.get(position).isShown()){
            holder.button.setText(context.getResources().getString(R.string.hide));
            holder.cardView.setAlpha(1f);
        } else {
            holder.button.setText(context.getResources().getString(R.string.show));
            holder.cardView.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return basketRealizations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public TextView name;
        public TextView code;
        public Button button;

        public ViewHolder(View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            name = itemView.findViewById(R.id.subject);
            code = itemView.findViewById(R.id.realization);
            button = itemView.findViewById(R.id.button_hide_show);
        }
    }
}
