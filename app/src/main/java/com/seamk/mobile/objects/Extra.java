package com.seamk.mobile.objects;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class Extra extends AbstractItem<Extra, Extra.ViewHolder> {

    private String extraName;
    private String extraDesc;
    private String extraText;
    private String extraIcon;
    private String extraColor;

    public Extra(String extraName, String extraDesc, String extraText, String extraIcon, String extraColor) {
        this.extraName = extraName;
        this.extraDesc = extraDesc;
        this.extraText = extraText;
        this.extraIcon = extraIcon;
        this.extraColor = extraColor;
    }

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    public String getExtraDesc() {
        return extraDesc;
    }

    public void setExtraDesc(String extraDesc) {
        this.extraDesc = extraDesc;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }

    public String getExtraIcon() {
        return extraIcon;
    }

    public void setExtraIcon(String extraIcon) {
        this.extraIcon = extraIcon;
    }

    public String getExtraColor() {
        return extraColor;
    }

    public void setExtraColor(String extraColor) {
        this.extraColor = extraColor;
    }

    @Override
    public int getType() {
        return R.id.fa_extra_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_extra;
    }

    @Override
    public void bindView(@NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);
        Context context = holder.itemView.getContext();

        holder.extraTitle.setText(extraName);

        int colorId = context.getResources().getIdentifier(extraColor, "color", context.getPackageName());
        holder.relativeLayout.setBackgroundColor(context.getResources().getColor(colorId));

        int drawableId = context.getResources().getIdentifier(extraIcon, "drawable", context.getPackageName());
        holder.extraTitle.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
        holder.extraDesc.setText(extraDesc);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.extraTitle.setText(null);
        holder.extraDesc.setText(null);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.extra_title_with_icon) TextViewRichDrawable extraTitle;
        @BindView(R.id.extra_desc) TextView extraDesc;
        @BindView(R.id.extra_relative_layout) RelativeLayout relativeLayout;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
