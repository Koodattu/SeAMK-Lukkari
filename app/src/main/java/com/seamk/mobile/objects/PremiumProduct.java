package com.seamk.mobile.objects;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.seamk.mobile.R;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 28.3.2018.
 */

public class PremiumProduct extends AbstractItem<PremiumProduct, PremiumProduct.ViewHolder> {

    String premiumName;
    String premiumDesc;
    String premiumPrice;
    String premiumColor;

    public PremiumProduct(String premiumName, String premiumDesc, String premiumPrice, String premiumColor) {
        this.premiumName = premiumName;
        this.premiumDesc = premiumDesc;
        this.premiumPrice = premiumPrice;
        this.premiumColor = premiumColor;
    }

    public String getPremiumName() {
        return premiumName;
    }

    public void setPremiumName(String premiumName) {
        this.premiumName = premiumName;
    }

    public String getPremiumDesc() {
        return premiumDesc;
    }

    public void setPremiumDesc(String premiumDesc) {
        this.premiumDesc = premiumDesc;
    }

    public String getPremiumPrice() {
        return premiumPrice;
    }

    public void setPremiumPrice(String premiumPrice) {
        this.premiumPrice = premiumPrice;
    }

    public String getPremiumColor() {
        return premiumColor;
    }

    public void setPremiumColor(String premiumColor) {
        this.premiumColor = premiumColor;
    }

    @Override
    public int getType() {
        return R.id.fa_premium_product_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_premium_product;
    }

    @Override
    public void bindView(@NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);
        Context context = holder.itemView.getContext();

        holder.textViewPrice.setText(premiumPrice);
        holder.textViewRichDrawable.setText(premiumName);
        holder.textViewDesc.setText(premiumDesc);

        int colorId = context.getResources().getIdentifier(premiumColor, "color", context.getPackageName());
        holder.relativeLayout.setBackgroundColor(context.getResources().getColor(colorId));
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

        @BindView(R.id.extra_relative_layout) RelativeLayout relativeLayout;
        @BindView(R.id.extra_title_with_icon) TextViewRichDrawable textViewRichDrawable;
        @BindView(R.id.price) TextView textViewPrice;
        @BindView(R.id.extra_desc) TextView textViewDesc;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}