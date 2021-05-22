package com.seamk.mobile.objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.seamk.mobile.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Find extends AbstractItem<Find, Find.ViewHolder> {

    private String findTitle;
    private String findDescription;

    public Find(String findTitle, String findDescription) {
        this.findTitle = findTitle;
        this.findDescription = findDescription;
    }

    public String getFindTitle() {
        return findTitle;
    }

    public void setFindTitle(String findTitle) {
        this.findTitle = findTitle;
    }

    public String getFindDescription() {
        return findDescription;
    }

    public void setFindDescription(String findDescription) {
        this.findDescription = findDescription;
    }

    @Override
    public int getType() {
        return R.id.fa_find_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_search_option;
    }

    @Override
    public void bindView(@NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);
        holder.findTitle.setText(findTitle);
        holder.findDesc.setText(findDescription);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.findTitle.setText(null);
        holder.findDesc.setText(null);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView findTitle;
        @BindView(R.id.desc) TextView findDesc;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
