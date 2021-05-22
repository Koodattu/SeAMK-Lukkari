package com.seamk.mobile.objects;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.seamk.mobile.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 1.11.2017.
 */

public class StringColorIconItem extends AbstractItem<StringColorIconItem, StringColorIconItem.ViewHolder> {

    private String title;
    private String desc;
    private String extraText;
    private String iconResName;
    private String colorResName;
    private int typeOfItem;
    private int spanSize;

    public StringColorIconItem(String title, String desc, String extraText, String iconResName, String colorResName, int typeOfItem, int spanSize) {
        this.title = title;
        this.desc = desc;
        this.extraText = extraText;
        this.iconResName = iconResName;
        this.colorResName = colorResName;
        this.typeOfItem = typeOfItem;
        this.spanSize = spanSize;
    }

    public StringColorIconItem(String title, String desc, String extraText, String iconResName, String colorResName, int typeOfItem) {
        this.title = title;
        this.desc = desc;
        this.extraText = extraText;
        this.iconResName = iconResName;
        this.colorResName = colorResName;
        this.typeOfItem = typeOfItem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }

    public String getIconResName() {
        return iconResName;
    }

    public void setIconResName(String iconResName) {
        this.iconResName = iconResName;
    }

    public String getColorResName() {
        return colorResName;
    }

    public void setColorResName(String colorResName) {
        this.colorResName = colorResName;
    }

    public int getTypeOfItem() {
        return typeOfItem;
    }

    public void setTypeOfItem(int typeOfItem) {
        this.typeOfItem = typeOfItem;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }


    @Override
    public int getType() {
        return R.id.fa_string_color_icon_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_reservation_action;
    }

    @Override
    public void bindView(@NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);
        Context context = holder.itemView.getContext();

        int colorId = context.getResources().getIdentifier(colorResName, "color", context.getPackageName());
        holder.background.setBackgroundColor(context.getResources().getColor(colorId));
        int drawableId = context.getResources().getIdentifier(iconResName, "drawable", context.getPackageName());
        Drawable img = context.getResources().getDrawable(drawableId);
        holder.background.setImageDrawable(img);
        holder.title.setText(title);
        int colorId2 = context.getResources().getIdentifier(colorResName + "Dark", "color", context.getPackageName());
        holder.darkerBackground.setBackgroundColor(context.getResources().getColor(colorId2));
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

        @BindView(R.id.text)
        TextView title;
        @BindView(R.id.iv_icon_background)
        ImageView background;
        @BindView(R.id.iv_text_background)
        ImageView darkerBackground;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
