package com.seamk.mobile.objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.seamk.mobile.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 28.3.2018.
 */

public class PremiumInfo extends AbstractItem<PremiumInfo, PremiumInfo.ViewHolder> {

    public PremiumInfo() {}

    @Override
    public int getType() {
        return R.id.fa_premium_info_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_premium_info;
    }

    @Override
    public void bindView(@NonNull final ViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.expandableLayout.toggle();
            }
        });

    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view) CardView cardView;
        @BindView(R.id.expandable_layout) ExpandableLayout expandableLayout;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
