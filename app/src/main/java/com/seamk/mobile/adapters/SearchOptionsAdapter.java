package com.seamk.mobile.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.eventbusevents.SearchChosenEvent;
import com.seamk.mobile.objects.SearchOption;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class SearchOptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SearchOption> items;
    private Context context;


    public SearchOptionsAdapter(List<SearchOption> items, Context context) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_option,parent,false);
            return new SearchOptionViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        final SearchOption item = items.get(position);

            ((SearchOptionViewHolder) viewHolder).bind(item);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new SearchChosenEvent(item.getSearchType()));
                }
            });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class SearchOptionViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView desc;

        public SearchOptionViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
        }
        public void bind(SearchOption searchOption) {

            title.setText(searchOption.getSearchTitle());
            desc.setText(searchOption.getSearchDescription());
        }
    }
}
